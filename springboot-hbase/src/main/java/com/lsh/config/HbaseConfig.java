package com.lsh.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/23 11:48 下午
 * @desc ：配置Hbase
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

    @Bean
    public HBaseAdmin hBaseAdmin() throws IOException {
        return (HBaseAdmin) getConnection().getAdmin();
    }
}