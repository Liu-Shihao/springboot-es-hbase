package com.lsh.mq.controller;

import com.lsh.service.MqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/23 5:33 下午
 * @desc ：
 */
@RequestMapping("/mq")
@RestController
public class MqController {

    @Autowired
    MqService mqService;

    @GetMapping("/sendRabbit")
    public Map<String,Object> sendRabbitMq(){
        HashMap<String, Object> hashMap = new HashMap<>();
        mqService.sendRabbit();
        hashMap.put("code","200");
        hashMap.put("msg","success");
        return hashMap;
    }
}
