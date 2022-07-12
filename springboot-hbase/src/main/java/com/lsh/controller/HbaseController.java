package com.lsh.controller;

import com.lsh.R;
import com.lsh.test.HbaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: LiuShihao
 * Date: 2022/7/12 17:18
 * Desc:
 */
@RequestMapping("/hbase")
@RestController
public class HbaseController {

    String tableName = "user";

    @Autowired
    HbaseClient hbaseClient;

//    @GetMapping("/getOneRowData")
//    public R getOneRowData(){
//        hbaseClient.
//    }
}
