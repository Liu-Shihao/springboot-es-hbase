package com.lsh.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/23 11:48 下午
 * @desc ：配置Hbase
 * @Hbase客户端代码连接详解 ：https://blog.csdn.net/m0_38003171/article/details/80225331
 * HBase中Connection类已经实现了对连接的管理功能，所以我们不需要自己在Connection之上再做额外的管理。
 * 另外，Connection是线程安全的，而Table和Admin则不是线程安全的，
 * 因此正确的做法是一个进程共用一个Connection对象，而在不同线程中使用单独的Table和Admin对象。
 */

@Configuration
@EnableConfigurationProperties(HbaseProperties.class)
public class HbaseConfig {

    private final HbaseProperties props;

    public HbaseConfig(HbaseProperties props) {
        this.props = props;
    }

    @Bean
    public org.apache.hadoop.conf.Configuration configuration(){
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        Map<String, String> config = props.getConfig();
        config.forEach(conf::set);
        return conf;
    }

    @Bean
    public Connection getConnection() throws IOException{
        return ConnectionFactory.createConnection(configuration());
    }
}