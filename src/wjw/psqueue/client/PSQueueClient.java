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
		System.setProperty("sun.net.http.retryPost", "false");  //���HttpURLConnection������SocketTimeoutException�쳣ʱ���Լ�����BUG!
	}

	private String server; //������IP��ַ
	private int port; //�������˿ں�
	private String charset; //HTTP�����ַ���
	private int connectTimeout = 0; //���ӳ�ʱ
	private int readTimeout = 0; //����ʱ

	private String basrUrl;

	/**
	 * ����PSQueue Client
	 * 
	 * @param server ������IP��ַ
	 * @param port �������˿ں�
	 * @param charset HTTP�����ַ���
	 * @param connectTimeout ���ӳ�ʱ(����)
	 * @param readTimeout ����ʱ(����)
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
	 * ����HTTP��GET����
	 * 
	 * @param urlstr �����URL
	 * @return �������ķ�����Ϣ
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
	 * ����HTTP��POST����
	 * 
	 * @param urlstr �����URL
	 * @param data ����е���Ϣ����
	 * @return �������ķ�����Ϣ
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
	 * ��������
	 * 
	 * @param queueName ������
	 * @param capacity ���е�����,1����10��֮�����ֵ
	 * @param user �û���
	 * @param pass ����
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
	 * ���ö�������
	 * 
	 * @param queueName ������
	 * @param capacity ���е�����,1����10��֮�����ֵ
	 * @param user �û���
	 * @param pass ����
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
	 * ����ָ�����е�ָ������
	 * 
	 * @param queueName ������
	 * @param subName ��������
	 * @param user �û���
	 * @param pass ����
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
	 * ɾ��ָ������
	 * 
	 * @param queueName ������
	 * @param user �û���
	 * @param pass ����
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
	 * ɾ��ָ�����е�ָ��������
	 * 
	 * @param queueName ������
	 * @param subName ��������
	 * @param user �û���
	 * @param pass ����
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
	 * �鿴����״̬
	 * 
	 * @param queueName ������
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
	 * �鿴ָ������ָ�������ߵ���Ϣ״̬
	 * 
	 * @param queueName ������
	 * @param subName ��������
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
	 * ��ȡȫ��������
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
	 * ��ȡָ����������ȫ��������
	 * 
	 * @param queueName ������
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
	 * ���ö���
	 * 
	 * @param queueName ������
	 * @param user �û���
	 * @param pass ����
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
	 * �����
	 * 
	 * @param queueName ������
	 * @param data ����е���Ϣ����
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
	 * ������
	 * 
	 * @param queueName ������
	 * @param subName ��������
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
	 * �鿴ָ����������ָ��λ�õ���Ϣ
	 * 
	 * @param queueName ������
	 * @param pos λ��
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
	 * ����ָ�����е�ָ�������ߵ�������ʼλ��
	 * 
	 * @param queueName ������
	 * @param subName ��������
	 * @param pos tail��λ��
	 * @param user �û���
	 * @param pass ����
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
