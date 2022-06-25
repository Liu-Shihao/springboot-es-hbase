//package com.lsh;
//
//
//import com.lsh.utils.HBaseClient;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//
///**
// * @author ：LiuShihao
// * @date ：Created in 2022/6/24 12:14 上午
// * @desc ：
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class HbaseDemoApplicationTests {
//
//    @Autowired
//    private HBaseClient hBaseClient;
//
//    /**
//     * 测试获得
//     */
//    @Test
//    public void testGetValue() {
//        String value = hBaseClient.getValue("tbl_user", "mengday", "info", "age");
//        System.out.println(value);
//    }
//
//    /**
//     * 创建表
//     * @throws IOException
//     */
//    @Test
//    public void testCreateTable() throws IOException {
//        String tableName = "tbl_abc";
//        hBaseClient.deleteTable(tableName);
//        hBaseClient.createTable(tableName, new String[] {"cf1", "cf2"});
//    }
//
//    /**
//     * 删除表
//     * @throws IOException
//     */
//    @Test
//    public void dropTable() throws IOException {
//        hBaseClient.deleteTable("tbl_abc");
//    }
//
//    /**
//     * 测试插入或更新
//     * @throws IOException
//     */
//    @Test
//    public void testInsertOrUpdate() throws IOException {
//        hBaseClient.insertOrUpdate("tbl_abc", "rowKey1", "cf1", new String[]{"c1", "c2"}, new String[]{"v1", "v22"});
//    }
//
//    /**
//     * 测试查询一行数据
//     * @throws IOException
//     */
//    @Test
//    public void testSelectOneRow() throws IOException {
//        hBaseClient.selectOneRow("tbl_abc", "rowKey1");
//    }
//
//    /**
//     * 测试扫描全表
//     * @throws IOException
//     */
//    @Test
//    public void testScanTable() throws IOException {
//        hBaseClient.scanTable("tbl_abc", "rowKey1");
//    }
//}
