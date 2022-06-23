package com.lsh.service.impl;

import com.lsh.mq.MyMQConfig;
import com.lsh.service.MqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/23 5:35 下午
 * @desc ：
 */
@Service
@EnableBinding(MyMQConfig.class)
public class MqServiceImpl implements MqService {

    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @Autowired
    MyMQConfig myMQConfig;

    @Override
    public void sendRabbit() {
        Message<String> message = MessageBuilder.withPayload(timestamp).build();
        myMQConfig.sendRabbit().send(message);
    }
}
