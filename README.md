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
