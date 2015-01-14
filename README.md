[PSQueueServer](https://github.com/wjw465150/PSQueueServer)的Java客户端库!
#### PSQueueServer 具有以下特征：

+ [x] 非常简单，基于 HTTP GET/POST 协议。PHP、Java、Perl、Shell、Python、Ruby等支持HTTP协议的编程语言均可调用。
+ [x] 完善的JMX管理接口,所有方法全部可以由JMX来管理.HTTP协议方法只暴露了add,poll,view,status这几个方法!
+ [x] 每个队列支持任意多消费者。
+ [x] 非常快速，入队列、出队列速度超过40000次/秒。
+ [x] 高并发，支持5K以上的并发连接。
+ [x] 支持多队列。
+ [x] 队列个数无限制,只要系统的磁盘空间够用(缺省单个队列占用磁盘空间是2G)。
+ [x] 低内存消耗，海量数据存储，存储几十GB的数据只需不到200MB的物理内存缓冲区。
+ [x] 可以实时查看指定队列状态（未读队列数量）。
+ [x] 可以查看指定队列,指定消费者的内容，包括未出、已出的队列内容。
+ [x] 查看队列内容时，支持多字符集编码。

#### 用法:
```bash
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
	//数据入队列
	public void test_add() {
		ResAdd rest = instance.add(queue_name, "你好PSQueue");
		if (rest.status.code == ResultCode.SUCCESS.code) { //成功
			System.out.println("成功:" + rest.toString());
		} else {
			System.out.println("失败:" + rest.toString());
		}
	}

	@Test
	//数据出队列
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
}
```