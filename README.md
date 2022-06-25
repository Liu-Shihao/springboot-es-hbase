# Elasticsearch
注意docker 启动hbase 后 es报错，应该是9300端口冲突
```text
[2022-06-23T09:54:46,094][WARN ][o.e.t.TcpTransport       ] [dm9fv_7] exception caught on transport layer [Netty4TcpChannel{localAddress=/172.17.0.6:9300, remoteAddress=/139.224.136.79:36968}], closing connection
io.netty.handler.codec.DecoderException: java.io.StreamCorruptedException: invalid internal transport message format, got (16,3,1,2)
        at io.netty.handler.codec.ByteToMessageDecoder.callDecode(ByteToMessageDecoder.java:472) ~[netty-codec-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.handler.codec.ByteToMessageDecoder.channelInputClosed(ByteToMessageDecoder.java:405) ~[netty-codec-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.handler.codec.ByteToMessageDecoder.channelInputClosed(ByteToMessageDecoder.java:372) ~[netty-codec-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.handler.codec.ByteToMessageDecoder.channelInactive(ByteToMessageDecoder.java:355) ~[netty-codec-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelInactive(AbstractChannelHandlerContext.java:245) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelInactive(AbstractChannelHandlerContext.java:231) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.AbstractChannelHandlerContext.fireChannelInactive(AbstractChannelHandlerContext.java:224) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.handler.logging.LoggingHandler.channelInactive(LoggingHandler.java:167) [netty-handler-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelInactive(AbstractChannelHandlerContext.java:245) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelInactive(AbstractChannelHandlerContext.java:231) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.AbstractChannelHandlerContext.fireChannelInactive(AbstractChannelHandlerContext.java:224) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.DefaultChannelPipeline$HeadContext.channelInactive(DefaultChannelPipeline.java:1429) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelInactive(AbstractChannelHandlerContext.java:245) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelInactive(AbstractChannelHandlerContext.java:231) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.DefaultChannelPipeline.fireChannelInactive(DefaultChannelPipeline.java:947) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.AbstractChannel$AbstractUnsafe$8.run(AbstractChannel.java:826) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:163) [netty-common-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:404) [netty-common-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:474) [netty-transport-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:909) [netty-common-4.1.32.Final.jar:4.1.32.Final]
        at java.lang.Thread.run(Thread.java:835) [?:?]
Caused by: java.io.StreamCorruptedException: invalid internal transport message format, got (16,3,1,2)
        at org.elasticsearch.transport.TcpTransport.readHeaderBuffer(TcpTransport.java:851) ~[elasticsearch-6.8.3.jar:6.8.3]
        at org.elasticsearch.transport.TcpTransport.readMessageLength(TcpTransport.java:837) ~[elasticsearch-6.8.3.jar:6.8.3]
        at org.elasticsearch.transport.netty4.Netty4SizeHeaderFrameDecoder.decode(Netty4SizeHeaderFrameDecoder.java:40) ~[transport-netty4-client-6.8.3.jar:6.8.3]
        at io.netty.handler.codec.ByteToMessageDecoder.decodeRemovalReentryProtection(ByteToMessageDecoder.java:502) ~[netty-codec-4.1.32.Final.jar:4.1.32.Final]
        at io.netty.handler.codec.ByteToMessageDecoder.callDecode(ByteToMessageDecoder.java:441) ~[netty-codec-4.1.32.Final.jar:4.1.32.Final]
        ... 20 more
```

```json
{
    "name": "dm9fv_7",
    "cluster_name": "elasticsearch",
    "cluster_uuid": "hg2Fn5ODQaSUP9kmhlk7FA",
    "version": {
        "number": "6.8.3",
        "build_flavor": "default",
        "build_type": "docker",
        "build_hash": "0c48c0e",
        "build_date": "2019-08-29T19:05:24.312154Z",
        "build_snapshot": false,
        "lucene_version": "7.7.0",
        "minimum_wire_compatibility_version": "5.6.0",
        "minimum_index_compatibility_version": "5.0.0"
    },
    "tagline": "You Know, for Search"
}
```
Docker 部署es 和es-head
```shell script
docker pull elasticsearch:6.8.3

find / -name jvm.options
vim /var/lib/docker/overlay2/630f5d517332ee20106a0d82a04e308dde5397e73a00ec8a533f9e573bd67991/diff/usr/share/elasticsearch/config/jvm.options
sysctl -w vm.max_map_count=262144

# 将容器中的配置文件挂载主机的/data/es/elasticsearch.yml，注意需要提前创建好elasticsearch.yml文件
docker run -di --name=es -p 9200:9200 -p 9300:9300 -v /data/es/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml elasticsearch:6.8.3

docker exec -it es /bin/bash

# elasticsearch.yml  文件
http.host: 0.0.0.0
transport.host: 0.0.0.0
network.bind_host: 0.0.0.0
http.cors.enabled: true
http.cors.allow-origin: "*"

# 拉取head镜像 只有5版本
docker pull mobz/elasticsearch-head:5
# 创建es-head容器
docker run -di --name head -p 9100:9100 mobz/elasticsearch-head:5
```

# Hbase
docker启动的hbase，使用了容器ID作为host，所以如果是外部程序要接入hbase，需要在本机hosts中加入容器ID对应的主机映射。 
## Docker 部署Hbase
```shell script


```



# SpringCloud Stream RabbitMQ
1. 引入SpringCloud
2. 引入SpringCloud Stream相关依赖
3. 定义绑定接口: 消息生产者(Output…Binding) 、消息消费者(Input…Binding)
4. @EnableBinding 在对应类上进行定义
5. @StreamListener 在对应方法上创建监听用来消费消息
6. 调用output的send()方法生产消息