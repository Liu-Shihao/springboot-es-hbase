package com.lsh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/22 10:20 下午
 * @desc ：
 * warn_id	预警ID
 * police_code	公安编码
 * warning_phone	预警手机号
 * warning_type	预警类型
 * warning_level	预警风险等级
 * province_code	省份编码
 * city_code	地市编码
 * area_code	区域编码
 * data_source	数据来源
 * receive_time	预警接收数据时间（预处理环节）
 * receipt_state	短闪信回执状态
 * ai_aegis_clue_result_out_flag	AI外呼线索回调状态（1表示已成功回调）
 * human_callout_final_out_flag	人工外呼回调状态（1表示已成功回调）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "fraud_index")
public class WarnData {

    @Id
    @Field(type = FieldType.Text)
    private String warn_id;

    @Field(type = FieldType.Text)
    private String police_code;

    @Field(type = FieldType.Text)
    private String warning_phone;

    @Field(type = FieldType.Text)
    private String warning_type;

    @Field(type = FieldType.Text)
    private String warning_level;

    @Field(type = FieldType.Text)
    private String province_code;

    @Field(type = FieldType.Text)
    private String city_code;

    @Field(type = FieldType.Text)
    private String area_code;

    @Field(type = FieldType.Text)
    private String data_source;

    //ES默认是不支持yyyy-MM-dd HH:mm:ss格式的
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
//    @Field(type = FieldType.Date)
    @Field(type = FieldType.Date, format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss")

    private String receive_time;

    @Field(type = FieldType.Text)
    private String receipt_state;

    @Field(type = FieldType.Text)
    private String ai_aegis_clue_result_out_flag;

    @Field(type = FieldType.Text)
    private String human_callout_final_out_flag;

}
