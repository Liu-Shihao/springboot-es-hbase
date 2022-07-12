package com.lsh.test;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.regionserver.NoSuchColumnFamilyException;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class HbaseClient {


    private static Connection connection;
    private static Configuration hbaseConfig;
    private static Properties properties;

    public HbaseClient() throws IOException {
        this.connection = getConn();
    }

    /**
     * 创建连接池并初始化环境配置
     */
    public void init() throws IOException {
        properties = System.getProperties();
//        Configuration configuration = kerberos();
        Configuration configuration = new Configuration();

        //实例化HBase配置类
        if (hbaseConfig == null){
            hbaseConfig = HBaseConfiguration.create(configuration);
        }

        Config load = ConfigFactory.load();
        hbaseConfig.setInt("hbase.rpc.timeout",20000);
        hbaseConfig.setInt("hbase.client.operation.timeout",30000);
        hbaseConfig.setInt("hbase.client.scanner.timeout.period",20000);
//        hbaseConfig.addResource(new Path(load.getString("hbase.conf.path")));  //hbase-site.xml  文件地址

    }

    public static Configuration kerberos(){
        Config load = ConfigFactory.load();
        Configuration configuration = new Configuration();
        String krb5Path = load.getString("krd5.conf.path");  //krb5文件地址
        String principal = load.getString("user.principal");  //用户票据
        String keytabPath =  load.getString("user.keytab.path");  //用户keytab文件地址
        configuration.set("hbase.security.authentication", "kerberos");
        configuration.set("hadoop.security.authentication", "kerberos");
        configuration.set("hbase.client.waitforauth.enable", "true");
        configuration.set("hbase.master.kerberos.principal", load.getString("hbase.master.principal"));
        configuration.set("hbase.regionserver.kerberos.principal", load.getString("hbase.regionserver.principal"));
        System.setProperty("java.security.krb5.conf", krb5Path);
        UserGroupInformation.setConfiguration(configuration);
        try {
            UserGroupInformation.loginUserFromKeytab(principal, keytabPath);  //用keytab文件登陆  已有票据缓存则不需要
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("--------- HBase成功通过Kerberos认证! -------- ");
        return configuration;
    }

    public boolean createTable(String tableName, List<String> families){
        boolean result = false;
//        Configuration config = kerberos();

        System.out.println("准备建表, 切换Admin模式");

        try {
            HBaseAdmin hBaseAdmin = new HBaseAdmin(hbaseConfig);
            if (hBaseAdmin.tableExists(tableName)) {// 如果存在要创建的表，那么先删除，再创建
                hBaseAdmin.disableTable(tableName);
                hBaseAdmin.deleteTable(tableName);
                System.out.println(tableName + "已经存在, 已经删除 ");
            }
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            for (String family : families){
                tableDescriptor.addFamily(new HColumnDescriptor(family));
            }

            hBaseAdmin.createTable(tableDescriptor);
            result = true;
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("表 " + tableName + " 已经建立完毕");

        return result;
    }

    public List<String> getFamilyName(String tableName) throws IOException {
        Connection connection = this.connection;
        if(connection.isClosed() || connection == null){
            connection = this.getConn();
        }
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        List<String> list = new ArrayList<>();
        HTableDescriptor hTableDescriptor = table.getTableDescriptor();
        for(HColumnDescriptor fdescriptor : hTableDescriptor.getColumnFamilies()){
            list.add(fdescriptor.getNameAsString());
        }
        table.close();
        return list;
    }

    public List<String> getColumnName(String rowkey,String tableName, String family) throws IOException {
        Connection connection = this.connection;
        if(connection.isClosed() || connection == null){
            connection = this.getConn();
        }
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));

        List<String> list = new ArrayList<>();

        try{
            Get get = new Get(Bytes.toBytes(rowkey));
            Result result = table.get(get);
            Map<byte[], byte[]> familyMap = result.getFamilyMap(Bytes.toBytes(family));
            for(Map.Entry<byte[], byte[]> entry:familyMap.entrySet()){
                list.add(Bytes.toString(entry.getKey()));
            }
        } finally {
            table.close();
        }
        return list;
    }

    public List<String> getRowKey(String tableName) throws IOException {
        Connection connection = this.connection;
        if(connection.isClosed() || connection == null){
            connection = this.getConn();
        }
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));

        List<String> list = new ArrayList<>();
        try{
            Scan scan = new Scan();
            System.out.println("正在获取表" + tableName + "的rowkey!");
            ResultScanner scanner = table.getScanner(scan);
            for (Result r:scanner){
                list.add(new String(r.getRow()));
            }
            scanner.close();
        } finally {
            table.close();
        }
        System.out.println("获取表" + tableName + "的rowkey完毕!");
        return list;
    }


    public List<Result> getOneColumnData(String tableName, String family, String column) throws IOException {
        List<Result> result = new ArrayList<>();

        Connection connection = this.connection;
        if(connection.isClosed() || connection == null){
            connection = this.getConn();
        }
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        try{
            Scan scan = new Scan();
            ResultScanner scanner = table.getScanner(scan);
            scan.addColumn(family.getBytes(),column.getBytes());
            for (Result r:scanner){
                result.add(r);
            }
            scanner.close();
        } finally {
            table.close();
        }
        return result;
    }

    public List<Result> getAllData(String tableName) throws IOException {
        List<Result> result = new ArrayList<>();

        Connection connection = this.connection;
        if(connection.isClosed() || connection == null){
            connection = this.getConn();
        }
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        try{
            Scan scan = new Scan();
            ResultScanner scanner = table.getScanner(scan);
            for (Result r:scanner){
                result.add(r);
            }
            scanner.close();
        } finally {
            table.close();
        }
        return result;
    }


    public Result getOneRow(String tableName,String rowkey) throws IOException {
        Result r = new Result();

        Connection connection = this.connection;
        if(connection.isClosed() || connection == null){
            connection = this.getConn();
        }
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        try{
            Get get = new Get(rowkey.getBytes());
            r = table.get(get);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            table.close();
        }
        return r;
    }


    public void insertOneCell(String tablename, String rowKey, String family, String column, String value)
            throws IOException {
        Connection connection = this.connection;
        if(connection.isClosed() || connection == null){
            connection = this.getConn();
        }
        HTable table = (HTable) connection.getTable(TableName.valueOf(tablename));

        Put put = new Put(rowKey.getBytes());
        put.addColumn(family.getBytes(),column.getBytes(),value.getBytes());
        try {
            table.put(put);
        } catch (NoSuchColumnFamilyException x){
            System.out.println("\n 目标表没有" + family  + "字段， 无法写入数据 \n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        table.close();
    }


    public List<KeyValue> selectRowKeyFamilyColumn(String tablename, String rowKey, String family, String column)
            throws IOException {
        List<KeyValue> result = new ArrayList<>();

        Connection connection = this.connection;
        if(connection.isClosed() || connection == null){
            connection = this.getConn();
        }
        HTable table = (HTable) connection.getTable(TableName.valueOf(tablename));

        Get g = new Get(rowKey.getBytes());
        g.addColumn(family.getBytes(), column.getBytes());

        Result rs = table.get(g);

        for (KeyValue kv : rs.raw())
        {
            result.add(kv);
        }
        return result;
    }



    public Connection getConn() throws IOException {
        if (connection == null ){
            init();
            connection = ConnectionFactory.createConnection(hbaseConfig);
            System.out.println("获取 Hbase 连接成功");
        }
        return connection;
    }

    public void close() throws IOException {
        if (!connection.isClosed()){
            connection.close();
            System.out.println("Hbase 连接关闭成功");
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("启动");
        HbaseClient client = new HbaseClient();
        String tableName = "user";
        //创建表
//        client.createTable("user",Arrays.asList("cf1","cf2","cf3"));

        //获得列族信息
//        List<String> test = client.getFamilyName("user");
//        test.forEach(System.out::println);
        Table table = client.getConn().getTable(TableName.valueOf(tableName));

        //插入数据
//        Put put = new Put(Bytes.toBytes("1"));
//        put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("name"),Bytes.toBytes("zhangsan"));
//        put.addColumn(Bytes.toBytes("cf2"),Bytes.toBytes("age"),Bytes.toBytes("22"));
//        put.addColumn(Bytes.toBytes("cf3"),Bytes.toBytes("gender"),Bytes.toBytes("man"));
//        table.put(put);
//        System.out.println("data put done");

        //查询数据
        Get get = new Get(Bytes.toBytes("1"));
        Result result = table.get(get);
        byte[] row = result.getRow();
        System.out.println("row:"+new String(row));

        List<String> familyName = client.getFamilyName(tableName);


        for (String cf : familyName) {
            List<String> columnName = client.getColumnName("1", tableName, cf);
            if (columnName == null || columnName.isEmpty()){
                continue;
            }else {
                for (String filedName : columnName) {
                    Cell cell = result.getColumnLatestCell(Bytes.toBytes(cf),Bytes.toBytes(filedName));
                    System.out.println(filedName+":"+Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
        }






    }

}
