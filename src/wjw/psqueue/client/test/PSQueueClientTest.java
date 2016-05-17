package wjw.psqueue.client.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wjw.psqueue.client.PSQueueClient;
import wjw.psqueue.msg.ResAdd;
import wjw.psqueue.msg.ResData;
import wjw.psqueue.msg.ResList;
import wjw.psqueue.msg.ResQueueStatus;
import wjw.psqueue.msg.ResSubStatus;
import wjw.psqueue.msg.ResultCode;

public class PSQueueClientTest {
	String queue_name = "test_queue";
	String sub_name = "test_sub";
	String user = "admin";
	String pass = "123456";
	PSQueueClient instance = new PSQueueClient("127.0.0.1", 1818, "GBK", 60 * 1000, 60 * 1000);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_createQueue() {
		ResultCode rest = instance.createQueue(queue_name, 10000000L, user, pass);
		if (rest.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_setQueueCapacity() {
		ResultCode rest = instance.setQueueCapacity(queue_name, 10000000L, user, pass);
		if (rest.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_createSub() {
		ResultCode rest = instance.createSub(queue_name, sub_name, user, pass);
		if (rest.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_removeQueue() {
		ResultCode rest = instance.removeQueue(queue_name, user, pass);
		if (rest.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_removeSub() {
		ResultCode rest = instance.removeSub(queue_name, sub_name, user, pass);
		if (rest.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_status() {
		ResQueueStatus rest = instance.status(queue_name);
		if (rest.status.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_statusForSub() {
		ResSubStatus rest = instance.statusForSub(queue_name, sub_name);
		if (rest.status.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_queueNames() {
		ResList rest = instance.queueNames();
		if (rest.status.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_subNames() {
		ResList rest = instance.subNames(queue_name);
		if (rest.status.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_resetQueue() {
		ResultCode rest = instance.resetQueue(queue_name, user, pass);
		if (rest.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_add() {
		ResAdd rest = instance.add(queue_name, "你好PSQueue+0800");
		if (rest.status.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_poll() {
		ResData rest = instance.poll(queue_name, sub_name);
		if (rest.status.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_view() {
		ResData rest = instance.view(queue_name, 0);
		if (rest.status.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	public void test_setSubTailPos() {
		ResultCode rest = instance.setSubTailPos(queue_name, sub_name, 1, user, pass);
		if (rest.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}
}
