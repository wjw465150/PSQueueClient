package wjw.psqueue.client.jmx;

import wjw.psqueue.msg.ResAdd;
import wjw.psqueue.msg.ResData;
import wjw.psqueue.msg.ResList;
import wjw.psqueue.msg.ResQueueStatus;
import wjw.psqueue.msg.ResSubStatus;
import wjw.psqueue.msg.ResultCode;

public interface AppMXBean {
	public ResultCode gc();

	public ResultCode createQueue(String queueName,
			long dbFileMaxSize,
	    final String user,
	    final String pass);

	public ResultCode createSub(String queueName,
	    String subName,
	    final String user,
	    String pass);

	public ResultCode removeQueue(String queueName,
	    final String user,
	    final String pass);

	public ResultCode removeSub(String queueName,
	    String subName,
	    final String user,
	    final String pass);

	public ResQueueStatus status(String queueName);

	public ResSubStatus statusForSub(String queueName,
	    String subName);

	public ResList queueNames();

	public ResList subNames(String queueName);

	public ResultCode resetQueue(String queueName,
	    final String user,
	    final String pass);

	public ResAdd add(String queueName,
	    final String data);

	public ResData poll(String queueName,
	    String subName);

	public ResData view(String queueName,
	    final long pos);

}
