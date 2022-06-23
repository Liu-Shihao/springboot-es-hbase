package com.lsh.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/23 3:00 下午
 * @desc ：程序通过 inputs 或者 outputs 来与 Spring Cloud Stream 中binder 交互
 * Binder 是 Spring Cloud Stream 的一个抽象概念，是应用与消息中间件之间的粘合剂。目前 Spring Cloud Stream 实现了 Kafka 和 Rabbit MQ 的binder。
 *通过 binder ，可以很方便的连接中间件，可以动态的改变消息的destinations（对应于 Kafka 的topic，Rabbit MQ 的 exchanges），这些都可以通过外部配置项来做到。
 *
 *
 * @Input("warn-data-push") input注解 监听消息
 *
 *
 * @Output("warn-data-for-manual") output注解为  投递消息
 *
 */
public interface MyMQConfig {

    String OUT_WARN_DATA_FOR_MANUAL = "out-warn-data-for-manual";

    String IN_WARN_DATA_FOR_MANUAL = "in-warn-data-for-manual";

//    @Input("in-warn-data-for-manual")
//    SubscribableChannel intput();

    @Output("out-warn-data-for-manual")
    MessageChannel sendRabbit();



}
