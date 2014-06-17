package wjw.psqueue.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.wjw.efjson.JsonObject;

import wjw.psqueue.msg.ResAdd;
import wjw.psqueue.msg.ResData;
import wjw.psqueue.msg.ResSubStatus;
import wjw.psqueue.msg.ResultCode;

public class PSQueueClient {
	static {
		System.setProperty("http.keepAlive", "true");
		System.setProperty("http.maxConnections", "200");
		System.setProperty("sun.net.http.errorstream.enableBuffering", "true");
	}

	private String server; //服务器IP地址
	private int port; //服务器端口号
	private String charset; //HTTP请求字符集
	private int connectTimeout = 0; //连接超时
	private int readTimeout = 0; //读超时

	/**
	 * 建立PSQueue Client
	 * 
	 * @param server 服务器IP地址
	 * @param port 服务器端口号
	 * @param charset HTTP请求字符集
	 * @param connectTimeout 连接超时(毫秒)
	 * @param readTimeout 读超时(毫秒)
	 */
	public PSQueueClient(String server, int port, String charset, int connectTimeout, int readTimeout) {
		this.server = server;
		this.port = port;
		this.charset = charset;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	/**
	 * 处理HTTP的GET请求,如果不需要BASIC验证,把user以及pass设置为null值
	 * 
	 * @param urlstr 请求的URL
	 * @param user 用户名
	 * @param pass 口令
	 * @return 服务器的返回信息
	 * @throws IOException
	 */
	private String doGetProcess(String urlstr) throws IOException {
		URL url = new URL(urlstr);

		HttpURLConnection conn = null;
		BufferedReader reader = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setUseCaches(false);
			conn.setDoOutput(false);
			conn.setDoInput(true);
			conn.setRequestProperty("Accept", "*/*");

			conn.connect();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			} else {
				reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), charset));
			}
			String line;
			StringBuilder result = new StringBuilder();

			int i = 0;
			while ((line = reader.readLine()) != null) {
				i++;
				if (i != 1) {
					result.append("\n");
				}
				result.append(line);
			}
			return result.toString();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}

			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception ex) {
				}
			}
		}
	}

	/**
	 * 处理HTTP的POST请求,如果不需要BASIC验证,把user以及pass设置为null值
	 * 
	 * @param urlstr 请求的URL
	 * @param data 入队列的消息内容
	 * @param user 用户名
	 * @param pass 口令
	 * @return 服务器的返回信息
	 * @throws IOException
	 */
	private String doPostProcess(String urlstr, String data) throws IOException {
		URL url = new URL(urlstr);

		HttpURLConnection conn = null;
		BufferedReader reader = null;
		OutputStreamWriter writer = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Content-Type", "application/json;charset=" + charset);

			conn.connect();

			writer = new OutputStreamWriter(conn.getOutputStream(), charset);
			writer.write(data);
			writer.flush();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			} else {
				reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), charset));
			}
			String line;
			StringBuilder result = new StringBuilder();

			int i = 0;
			while ((line = reader.readLine()) != null) {
				i++;
				if (i != 1) {
					result.append("\n");
				}
				result.append(line);
			}
			return result.toString();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}

			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
				}
			}

			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception ex) {
				}
			}
		}
	}

	/**
	 * 查看队列状态
	 * 
	 * @param queueName 队列名
	 * @param subName 订阅名
	 * @return
	 */
	public ResSubStatus status(String queueName, String subName) {
		String strResult = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?charset=" + this.charset + "&qname=" + URLEncoder.encode(queueName, charset) + "&sname=" + URLEncoder.encode(subName, charset) + "&opt=status";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResSubStatus.class);
		} catch (Exception ex) {
			return new ResSubStatus(new ResultCode(-1, ex.getMessage()), queueName, subName);
		}
	}

	/**
	 * 入队列
	 * 
	 * @param queueName 队列名
	 * @param data 入队列的消息内容
	 * @return
	 */
	public ResAdd add(final String queueName, final String data) {
		String strResult = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?charset=" + this.charset + "&qname=" + URLEncoder.encode(queueName, charset) + "&opt=add";

			strResult = this.doPostProcess(urlstr, data);
			return JsonObject.fromJson(strResult, ResAdd.class);
		} catch (Exception ex) {
			return new ResAdd(new ResultCode(-1, ex.getMessage()));
		}
	}

	public ResData poll(final String queueName, final String subName) {
		String strResult = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?charset=" + this.charset + "&qname=" + URLEncoder.encode(queueName, charset) + "&sname=" + URLEncoder.encode(subName, charset) + "&opt=poll";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResData.class);
		} catch (Exception ex) {
			return new ResData(new ResultCode(-1, ex.getMessage()));
		}
	}

	public ResData view(final String queueName, final long pos) {
		String strResult = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?charset=" + this.charset + "&qname=" + URLEncoder.encode(queueName, charset) + "&pos=" + pos + "&opt=view";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResData.class);
		} catch (Exception ex) {
			return new ResData(new ResultCode(-1, ex.getMessage()));
		}
	}
}
