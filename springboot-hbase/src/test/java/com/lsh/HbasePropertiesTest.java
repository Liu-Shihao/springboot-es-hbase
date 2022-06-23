package com.lsh;

import com.lsh.config.HbaseProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/23 11:52 下午
 * @desc ：
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class HbasePropertiesTest {
    @Autowired
    HbaseProperties hbaseProperties;
    @Test
    public void test(){
        Map<String, String> config = hbaseProperties.getConfig();
        System.out.println(config.toString());
    }
}
