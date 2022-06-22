package com.lsh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/22 10:20 下午
 * @desc ：
 */
// @Document声明es的文档类型
// indexName es的索引库名字  必须小写
// type  es的类型[表]
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "test2", type = "book")
public class Book {

    @Id
    @Field(type = FieldType.Text)
    private String bookId;

    //是否索引，就是看该域是否能被搜索。
    //是否分词，就表示搜索的时候是整体匹配还是单词匹配
    //是否存储，就是是否在页面上显示
    @Field(type = FieldType.Text)
    private String bookTitle;

//    @Field(type = FieldType.Text,index = true, analyzer="ik_max_word", searchAnalyzer="ik_max_word")
    @Field(type = FieldType.Text)
    private String bookContent;

}
