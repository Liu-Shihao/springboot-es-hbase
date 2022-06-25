package com.lsh.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/25 3:54 下午
 * @desc ：使用Hbase Client API  版本 1.2.0
 */
public class HbaseDemo {

    //配置
    Configuration conf = null;

    //连接
    Connection conn = null;

    //表的管理对象
    Admin admin = null;

    //表名
    TableName tableName = TableName.valueOf("phone");

    /**
     * 初始化连接
     * @throws IOException
     */
    @Before
    public void init() throws IOException {
        conf = HBaseConfiguration.create();
        //hbase.zookeeper.quorum: 47.100.241.202
//        conf.set("hbase.zookeeper.quorum","node01,node02,node03");
//        conf.set("hbase.zookeeper.quorum","47.100.241.202");
        conf.set("hbase.zookeeper.quorum","zhangqi");
        conf.set("hbase.zookeeper.port","2181");
        //获得连接
        conn = ConnectionFactory.createConnection(conf);
        admin = conn.getAdmin();

    }

    /**
     * 创建表
     */
    @Test
    public void createTable() throws IOException {
        //在高版本中HTableDescriptor，已经过时了，使用 HTableDescriptorBuilder
        //1.创建表
        HTableDescriptor table = new HTableDescriptor(tableName);
        //2.创建列族
        //在高版本中创建列族使用ColumnFamilyDescriptorBuilder.newBuilder("cf".getBytes())
        table.addFamily(new HColumnDescriptor("cf").setCompressionType(Compression.Algorithm.SNAPPY));
        System.out.print("Creating table. ");
        createOrOverwrite(admin, table);
        System.out.println(" Done.");
    }
    public void createOrOverwrite(Admin admin, HTableDescriptor table) throws IOException {
        if (admin.tableExists(table.getTableName())) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
        }
        admin.createTable(table);
    }

}
