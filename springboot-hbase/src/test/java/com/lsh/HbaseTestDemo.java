package com.lsh;

import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/25 10:13 下午
 * @desc ：
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class HbaseTestDemo {
    //表名
    TableName tableName = TableName.valueOf("user");

    @Autowired
    Connection connection;

    @Test
    public void createTable() throws IOException {
        Admin hBaseAdmin = connection.getAdmin();
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
        //如果表存在，则删除表
        if (admin.tableExists(table.getTableName())) {
            //删除表之前，需要将表禁用
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
        }
        admin.createTable(table);
    }

    @Test
    public void deleteTable() throws IOException {
        Admin hBaseAdmin = connection.getAdmin();
        if (hBaseAdmin.tableExists(tableName)){
            hBaseAdmin.disableTable(tableName);
            hBaseAdmin.deleteTable(tableName);
        }
        System.out.println("delete table done");
    }

    /**
     * 插入数据
     * @throws IOException
     */
    @Test
    public void put() throws IOException {
        Table table = connection.getTable(tableName);
        Put put = new Put(Bytes.toBytes("2"));
        put.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("name"),Bytes.toBytes("lisi"));
        put.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("age"),Bytes.toBytes("341"));
        put.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("sex"),Bytes.toBytes("women"));
        table.put(put);
        System.out.println("data put done");
    }
    @Test
    public void get() throws IOException {
        Table table = connection.getTable(tableName);
        Get get = new Get(Bytes.toBytes("1"));
        //在服务端做数据过滤，挑选出符合需求的列
        get.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("name"));
        Result result = table.get(get);
        Cell cell1 = result.getColumnLatestCell(Bytes.toBytes("cf"),Bytes.toBytes("name"));
        String name = Bytes.toString(CellUtil.cloneValue(cell1));
        System.out.println(name);
    }
    @Test
    public void getMoreColumn() throws IOException {
        Table table = connection.getTable(tableName);
        Get get = new Get(Bytes.toBytes("2"));
        //在服务端做数据过滤，挑选出符合需求的列
        get.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("name"));
        get.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("age"));
        get.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("sex"));

        Result result = table.get(get);
        Cell cell1 = result.getColumnLatestCell(Bytes.toBytes("cf"),Bytes.toBytes("name"));
        Cell cell2 = result.getColumnLatestCell(Bytes.toBytes("cf"),Bytes.toBytes("age"));
        Cell cell3 = result.getColumnLatestCell(Bytes.toBytes("cf"),Bytes.toBytes("sex"));

        String name = Bytes.toString(CellUtil.cloneValue(cell1));
        String age = Bytes.toString(CellUtil.cloneValue(cell2));
        String sex = Bytes.toString(CellUtil.cloneValue(cell3));

        System.out.println(name);
        System.out.println(age);
        System.out.println(sex);
    }



    /**
     * 获取表中所有的记录
     */
    @Test
    public void scan() throws IOException {
        Table table = connection.getTable(tableName);
        Scan scan = new Scan();
        //hbase 2.0版本及以上 废弃了 scan 扫表时候的setStartRow 和 setStopRow，更改为对应的withStartRow 和 withStopRow 两方法 。
        //第一个参数为scan扫表开始的rowkey，第二个参数，若为true，则包含rowkey为1打头的；若为false，则不包含rowkey为1打头的数据。
//        scan.withStartRow();
//        scan.withStopRow();
        ResultScanner rss = table.getScanner(scan);
        for (Result rs: rss) {
            byte[] row = rs.getRow();
            System.out.println("RowKey:"+Bytes.toString(row));
            Cell cell1 = rs.getColumnLatestCell(Bytes.toBytes("cf"),Bytes.toBytes("name"));
            Cell cell2 = rs.getColumnLatestCell(Bytes.toBytes("cf"),Bytes.toBytes("age"));
            Cell cell3 = rs.getColumnLatestCell(Bytes.toBytes("cf"),Bytes.toBytes("sex"));
            if (cell1!= null){
                String name = Bytes.toString(CellUtil.cloneValue(cell1));
                System.out.println(name);
            }
            if (cell2!= null){
                String age = Bytes.toString(CellUtil.cloneValue(cell2));
                System.out.println(age);
            }
            if (cell3!= null){
                String sex = Bytes.toString(CellUtil.cloneValue(cell3));
                System.out.println(sex);
            }
        }
    }


    /**
     * 假设有10个用户，每个用户一年产生10000条记录
     */
    @Test
    public void insertMangData() throws Exception {
        Table table = connection.getTable(tableName);
        List<Put> puts = new ArrayList<>();
        for(int i = 0;i<10;i++){
            String phoneNumber = getNumber("158");
            for(int j = 0 ;j<10000;j++){
                String dnum = getNumber("177");
                String length = String.valueOf(random.nextInt(100));
                String date = getDate("2022");
                String type = String.valueOf(random.nextInt(2));
                //rowkey
                String rowkey = phoneNumber+"_"+(Long.MAX_VALUE-sdf.parse(date).getTime());
                Put put = new Put(Bytes.toBytes(rowkey));
                put.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("dnum"),Bytes.toBytes(dnum));
                put.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("length"),Bytes.toBytes(length));
                put.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("date"),Bytes.toBytes(date));
                put.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("type"),Bytes.toBytes(type));
                puts.add(put);
            }
        }
        table.put(puts);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private String getDate(String s) {
        return s+String.format("%02d%02d%02d%02d%02d",random.nextInt(12)+1,random.nextInt(31),random.nextInt(24),random.nextInt(60),random.nextInt(60));
    }

    Random random = new Random();
    public String getNumber(String str){
        return str+String.format("%08d",random.nextInt(99999999));
    }

}
