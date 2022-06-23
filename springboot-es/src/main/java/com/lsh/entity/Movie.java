package com.lsh.entity;

import lombok.Data;
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
// indexName es的索引库名字
// type  es的类型[表]
@Data
@Document(indexName = "test1", type = "movie")
public class Movie {

    @Id
    @Field(type = FieldType.Text)
    private String movieId;

    //是否索引，就是看该域是否能被搜索。
    //是否分词，就表示搜索的时候是整体匹配还是单词匹配
    //是否存储，就是是否在页面上显示
    @Field(type = FieldType.Text)
    private String movieTitle;

    @Field(type = FieldType.Text)
    private String movieContent;

}
