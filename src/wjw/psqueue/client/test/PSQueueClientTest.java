package wjw.psqueue.client.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wjw.psqueue.client.PSQueueClient;
import wjw.psqueue.msg.ResAdd;
import wjw.psqueue.msg.ResData;
import wjw.psqueue.msg.ResSubStatus;

public class PSQueueClientTest {
	String queue_name = "test_queue";
	String sub_name = "test_sub";
	PSQueueClient instance = new PSQueueClient("127.0.0.1", 1818, "GBK", 60 * 1000, 60 * 1000);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStatus() {
		ResSubStatus rest = instance.status(queue_name, sub_name);
		if (rest.status.code == 0) { //成功
			System.out.println(rest.toString());
		} else {
			System.out.println(rest.toString());
		}
	}

	@Test
	public void testAdd() {
		ResAdd rest = instance.add(queue_name, "你好PSQueue");
		if (rest.status.code == 0) { //成功
			System.out.println(rest.toString());
		} else {
			System.out.println(rest.toString());
		}
	}

	@Test
	public void testPoll() {
		ResData rest = instance.poll(queue_name, sub_name);
		if (rest.status.code == 0) { //成功
			System.out.println(rest.toString());
		} else {
			System.out.println(rest.toString());
		}
	}

	@Test
	public void testView() {
		ResData rest = instance.view(queue_name, 0);
		if (rest.status.code == 0) { //成功
			System.out.println(rest.toString());
		} else {
			System.out.println(rest.toString());
		}
	}
}
