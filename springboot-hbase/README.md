Hbase版本1.2.0
Hbase 是一个分布式的、可扩展的、支持海量数据存储的NoSQL数据库

使用Docker搭建：
```shell script
# 拉取镜像
docker pull harisekhon/hbase:1.2
# 创建容器
docker run -d --name hbase -p 2181:2181 -p 16010:16010 -p 16020:16020 -p 16030:16030 harisekhon/hbase:1.2
```

出现一下报错是因为，IDEA不知道Hbase的镜像ID，需要添加docker的ID所在虚拟机的IP即可
在/etc/hosts添加以下规则（自己docker所在的虚拟机的IP+docker镜像ID）：


```text
java.net.UnknownHostException: 4f21ada9d787
	at java.net.InetAddress.getAllByName0(InetAddress.java:1281) ~[na:1.8.0_211]
	at java.net.InetAddress.getAllByName(InetAddress.java:1193) ~[na:1.8.0_211]
	at java.net.InetAddress.getAllByName(InetAddress.java:1127) ~[na:1.8.0_211]
	at java.net.InetAddress.getByName(InetAddress.java:1077) ~[na:1.8.0_211]
	at org.apache.hadoop.hbase.client.ConnectionUtils.getStubKey(ConnectionUtils.java:238) ~[hbase-client-2.4.3.jar:2.4.3]
	at org.apache.hadoop.hbase.client.ConnectionImplementation.getClient(ConnectionImplementation.java:1324) [hbase-client-2.4.3.jar:2.4.3]
	at org.apache.hadoop.hbase.client.ReversedScannerCallable.prepare(ReversedScannerCallable.java:97) [hbase-client-2.4.3.jar:2.4.3]
	at org.apache.hadoop.hbase.client.ScannerCallableWithReplicas$RetryingRPC.prepare(ScannerCallableWithReplicas.java:407) [hbase-client-2.4.3.jar:2.4.3]
	at org.apache.hadoop.hbase.client.RpcRetryingCallerImpl.callWithRetries(RpcRetryingCallerImpl.java:106) [hbase-client-2.4.3.jar:2.4.3]
	at org.apache.hadoop.hbase.client.ResultBoundedCompletionService$QueueingFuture.run(ResultBoundedCompletionService.java:80) [hbase-client-2.4.3.jar:2.4.3]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_211]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_211]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_211]

```
开放 16010、16020、16030端口，将所有集群中的主机名保持跟hosts文件的ip映射名一致
```text
2022-06-24 00:27:22.878  INFO 8311 --- [           main] o.a.h.h.client.RpcRetryingCallerImpl     : Call exception, tries=8, retries=16, started=69303 ms ago, cancelled=false, msg=java.net.ConnectException: Call to address=4f21ada9d787/47.100.241.202:16000null failed on connection exception: org.apache.hbase.thirdparty.io.netty.channel.ConnectTimeoutException: connection timed out: 4f21ada9d787/47.100.241.202:16000, details=, see https://s.apache.org/timeout

```