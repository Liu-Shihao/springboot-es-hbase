package com.lsh.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/22 9:01 下午
 * @desc ：
 */
@RestController
public class IndexController {

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping
    public String index(){
        return applicationName;
    }
}
