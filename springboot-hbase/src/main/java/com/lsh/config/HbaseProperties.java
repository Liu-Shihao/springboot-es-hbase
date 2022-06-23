package com.lsh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/23 11:47 下午
 * @desc ：读取配置文件hbase的配置
 */
@ConfigurationProperties(prefix = "hbase")
public class HbaseProperties {
    private Map<String,String> config;

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public Map<String, String> getConfig() {
        return config;
    }
}