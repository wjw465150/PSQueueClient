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
import wjw.psqueue.msg.ResList;
import wjw.psqueue.msg.ResQueueStatus;
import wjw.psqueue.msg.ResSubStatus;
import wjw.psqueue.msg.ResultCode;

/**
 * 
 * @author wjw465150@gmail.com
 * 
 */
public class PSQueueClient {
	static {
		System.setProperty("http.keepAlive", "true");
		System.setProperty("http.maxConnections", "200");
		System.setProperty("sun.net.http.errorstream.enableBuffering", "true");
		System.setProperty("sun.net.http.retryPost", "false");  //解决HttpURLConnection当发生SocketTimeoutException异常时的自己重试BUG!
	}

	private String server; //服务器IP地址
	private int port; //服务器端口号
	private String charset; //HTTP请求字符集
	private int connectTimeout = 0; //连接超时
	private int readTimeout = 0; //读超时

	private String basrUrl;

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

		this.basrUrl = "http://" + this.server + ":" + this.port + "/?charset=" + this.charset;
	}

	/**
	 * 处理HTTP的GET请求
	 * 
	 * @param urlstr 请求的URL
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
	 * 处理HTTP的POST请求
	 * 
	 * @param urlstr 请求的URL
	 * @param data 入队列的消息内容
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
	 * 创建队列
	 * 
	 * @param queueName 队列名
	 * @param capacity 队列的容量,1百万到10亿之间的数值
	 * @param user 用户名
	 * @param pass 口令
	 * @return ResultCode
	 */
	public ResultCode createQueue(final String queueName, final long capacity, final String user, final String pass) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&capacity=" + capacity
			    + "&user=" + URLEncoder.encode(user, charset)
			    + "&pass=" + URLEncoder.encode(pass, charset)
			    + "&opt=createQueue";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResultCode.class);
		} catch (Exception ex) {
			return new ResultCode(-1, ex.getMessage());
		}
	}

	/**
	 * 设置队列容量
	 * 
	 * @param queueName 队列名
	 * @param capacity 队列的容量,1百万到10亿之间的数值
	 * @param user 用户名
	 * @param pass 口令
	 * @return ResultCode
	 */
	public ResultCode setQueueCapacity(final String queueName, final long capacity, final String user, final String pass) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&capacity=" + capacity
			    + "&user=" + URLEncoder.encode(user, charset)
			    + "&pass=" + URLEncoder.encode(pass, charset)
			    + "&opt=setCapacity";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResultCode.class);
		} catch (Exception ex) {
			return new ResultCode(-1, ex.getMessage());
		}
	}

	/**
	 * 创建指定队列的指定消费
	 * 
	 * @param queueName 队列名
	 * @param subName 订阅者名
	 * @param user 用户名
	 * @param pass 口令
	 * @return ResultCode
	 */
	public ResultCode createSub(String queueName, String subName, final String user, final String pass) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&sname=" + URLEncoder.encode(subName, charset)
			    + "&user=" + URLEncoder.encode(user, charset)
			    + "&pass=" + URLEncoder.encode(pass, charset)
			    + "&opt=createSub";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResultCode.class);
		} catch (Exception ex) {
			return new ResultCode(-1, ex.getMessage());
		}
	}

	/**
	 * 删除指定队列
	 * 
	 * @param queueName 队列名
	 * @param user 用户名
	 * @param pass 口令
	 * @return ResultCode
	 */
	public ResultCode removeQueue(String queueName, final String user, final String pass) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&user=" + URLEncoder.encode(user, charset)
			    + "&pass=" + URLEncoder.encode(pass, charset)
			    + "&opt=removeQueue";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResultCode.class);
		} catch (Exception ex) {
			return new ResultCode(-1, ex.getMessage());
		}
	}

	/**
	 * 删除指定队列的指定订阅者
	 * 
	 * @param queueName 队列名
	 * @param subName 订阅者名
	 * @param user 用户名
	 * @param pass 口令
	 * @return ResultCode
	 */
	public ResultCode removeSub(String queueName, String subName, final String user, final String pass) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&sname=" + URLEncoder.encode(subName, charset)
			    + "&user=" + URLEncoder.encode(user, charset)
			    + "&pass=" + URLEncoder.encode(pass, charset)
			    + "&opt=removeSub";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResultCode.class);
		} catch (Exception ex) {
			return new ResultCode(-1, ex.getMessage());
		}
	}

	/**
	 * 查看队列状态
	 * 
	 * @param queueName 队列名
	 * @return ResQueueStatus
	 */
	public ResQueueStatus status(String queueName) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&opt=status";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResQueueStatus.class);
		} catch (Exception ex) {
			return new ResQueueStatus(new ResultCode(-1, ex.getMessage()), queueName);
		}
	}

	/**
	 * 查看指定队列指定订阅者的消息状态
	 * 
	 * @param queueName 队列名
	 * @param subName 订阅者名
	 * @return ResSubStatus
	 */
	public ResSubStatus statusForSub(String queueName, String subName) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&sname=" + URLEncoder.encode(subName, charset)
			    + "&opt=statusForSub";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResSubStatus.class);
		} catch (Exception ex) {
			return new ResSubStatus(new ResultCode(-1, ex.getMessage()), queueName, subName);
		}
	}

	/**
	 * 获取全部队列名
	 * 
	 * @return ResList
	 */
	public ResList queueNames() {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&opt=queueNames";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResList.class);
		} catch (Exception ex) {
			return new ResList(new ResultCode(-1, ex.getMessage()));
		}
	}

	/**
	 * 获取指定队列名的全部订阅者
	 * 
	 * @param queueName 队列名
	 * @return ResList
	 */
	public ResList subNames(String queueName) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&opt=subNames";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResList.class);
		} catch (Exception ex) {
			return new ResList(new ResultCode(-1, ex.getMessage()));
		}
	}

	/**
	 * 重置队列
	 * 
	 * @param queueName 队列名
	 * @param user 用户名
	 * @param pass 口令
	 * @return ResultCode
	 */
	public ResultCode resetQueue(String queueName, final String user, final String pass) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&user=" + URLEncoder.encode(user, charset)
			    + "&pass=" + URLEncoder.encode(pass, charset)
			    + "&opt=resetQueue";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResultCode.class);
		} catch (Exception ex) {
			return new ResultCode(-1, ex.getMessage());
		}
	}

	/**
	 * 入队列
	 * 
	 * @param queueName 队列名
	 * @param data 入队列的消息内容
	 * @return ResAdd
	 */
	public ResAdd add(final String queueName, final String data) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&opt=add";

			strResult = this.doPostProcess(urlstr, data);
			return JsonObject.fromJson(strResult, ResAdd.class);
		} catch (Exception ex) {
			return new ResAdd(new ResultCode(-1, ex.getMessage()));
		}
	}

	/**
	 * 出队列
	 * 
	 * @param queueName 队列名
	 * @param subName 订阅者名
	 * @return ResData
	 */
	public ResData poll(final String queueName, final String subName) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&sname=" + URLEncoder.encode(subName, charset)
			    + "&opt=poll";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResData.class);
		} catch (Exception ex) {
			return new ResData(new ResultCode(-1, ex.getMessage()));
		}
	}

	/**
	 * 查看指定队列名和指定位置的消息
	 * 
	 * @param queueName 队列名
	 * @param pos 位置
	 * @return ResData
	 */
	public ResData view(final String queueName, final long pos) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&pos=" + pos + "&opt=view";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResData.class);
		} catch (Exception ex) {
			return new ResData(new ResultCode(-1, ex.getMessage()));
		}
	}

	/**
	 * 设置指定队列的指定订阅者的索引起始位置
	 * 
	 * @param queueName 队列名
	 * @param subName 订阅者名
	 * @param pos tail的位置
	 * @param user 用户名
	 * @param pass 口令
	 * @return ResultCode
	 */
	public ResultCode setSubTailPos(String queueName, String subName, final long pos, final String user, final String pass) {
		String strResult = null;
		try {
			String urlstr = this.basrUrl
			    + "&qname=" + URLEncoder.encode(queueName, charset)
			    + "&sname=" + URLEncoder.encode(subName, charset)
			    + "&pos=" + pos
			    + "&user=" + URLEncoder.encode(user, charset)
			    + "&pass=" + URLEncoder.encode(pass, charset)
			    + "&opt=setSubTailPos";

			strResult = this.doGetProcess(urlstr);
			return JsonObject.fromJson(strResult, ResultCode.class);
		} catch (Exception ex) {
			return new ResultCode(-1, ex.getMessage());
		}
	}

}
