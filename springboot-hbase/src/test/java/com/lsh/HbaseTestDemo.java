package com.lsh;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/25 10:13 下午
 * @desc ：
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class HbaseTestDemo {
    //表名
    TableName tableName = TableName.valueOf("phone");

    @Autowired
    HBaseAdmin hBaseAdmin;

    @Test
    public void createTable() throws IOException {
        //在高版本中HTableDescriptor，已经过时了，使用 HTableDescriptorBuilder
        //1.创建表
        HTableDescriptor table = new HTableDescriptor(tableName);
        //2.创建列族
        //在高版本中创建列族使用ColumnFamilyDescriptorBuilder.newBuilder("cf".getBytes())
        table.addFamily(new HColumnDescriptor("cf").setCompressionType(Compression.Algorithm.SNAPPY));
        System.out.print("Creating table. ");
        createOrOverwrite(hBaseAdmin, table);
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
