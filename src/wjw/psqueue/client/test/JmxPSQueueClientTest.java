package wjw.psqueue.client.test;

import java.util.HashMap;
import java.util.Map;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wjw.psqueue.client.jmx.AppMXBean;
import wjw.psqueue.msg.ResAdd;
import wjw.psqueue.msg.ResData;
import wjw.psqueue.msg.ResList;
import wjw.psqueue.msg.ResQueueStatus;
import wjw.psqueue.msg.ResSubStatus;
import wjw.psqueue.msg.ResultCode;

public class JmxPSQueueClientTest {
	String queue_name = "test_queue";
	String sub_name = "test_sub";
	String psqHost = "127.0.0.1";
	int jmxPort = 1819;
	String adminUser = "admin";
	String adminPass = "123456";
	String psqJmxName = "wjw.psqueue:type=PSQueueServer";

	JMXConnector jmxc;
	AppMXBean proxy;

	public JMXConnector createJMXConnector(String host, int port, String user, String pass) throws Exception {
		Map<String, Object> environment = new HashMap<String, Object>();
		String[] credentials = new String[] { user, pass };
		environment.put("jmx.remote.credentials", credentials);
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi");
		JMXConnector jmxc = JMXConnectorFactory.connect(url, environment);

		return jmxc;
	}

	public AppMXBean getAppMXBean(JMXConnector jmxc) throws Exception {
		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

		ObjectName name = new ObjectName(psqJmxName);
		return JMX.newMXBeanProxy(mbsc, name, AppMXBean.class);
	}

	@Before
	public void setUp() throws Exception {
		jmxc = createJMXConnector(psqHost, jmxPort, adminUser, adminPass);
		proxy = getAppMXBean(jmxc);
	}

	@After
	public void tearDown() throws Exception {
		jmxc.close();
	}

	@Test
	public void test_createQueue() {
		ResultCode rest = proxy.createQueue(queue_name, 2 * 1024 * 1024 * 1024, adminUser, adminPass);
		if (rest.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_createSub() {
		ResultCode rest = proxy.createSub(queue_name, sub_name, adminUser, adminPass);
		if (rest.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_removeQueue() {
		ResultCode rest = proxy.removeQueue(queue_name, adminUser, adminPass);
		if (rest.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_removeSub() {
		ResultCode rest = proxy.removeSub(queue_name, sub_name, adminUser, adminPass);
		if (rest.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_status() {
		ResQueueStatus rest = proxy.status(queue_name);
		if (rest.status.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_statusForSub() {
		ResSubStatus rest = proxy.statusForSub(queue_name, sub_name);
		if (rest.status.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_queueNames() {
		ResList rest = proxy.queueNames();
		if (rest.status.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_subNames() {
		ResList rest = proxy.subNames(queue_name);
		if (rest.status.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_resetQueue() {
		ResultCode rest = proxy.resetQueue(queue_name, adminUser, adminPass);
		if (rest.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_add() {
		ResAdd rest = proxy.add(queue_name, "你好PSQueue");
		if (rest.status.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_poll() {
		ResData rest = proxy.poll(queue_name, sub_name);
		if (rest.status.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_view() {
		ResData rest = proxy.view(queue_name, 0);
		if (rest.status.code == 0) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

}
