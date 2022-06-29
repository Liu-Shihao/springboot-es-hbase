package com.lsh.dto;

import lombok.Data;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/28 6:16 下午
 * @desc ：
 */
@Data
public class SearchDataDto extends BaseDto {
    /**
     * 预警ID
     */
    private String warnId;

    /**
     * 预警号码
     */
    private String earlyWarningPhone;

    /**
     * 预警类型
     */
    private String earlyWarningType;

    /**
     * 预警等级
     */
    private String earlyWarningLevel;

    /**
     * 公安code
     */
    private String policeCode;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 地市编码
     */
    private String cityCode;

    /**
     * 区县编码
     */
    private String areaCode;

    /**
     * 数据接受时间 开始时间
     */
    private String receivingStartTime;

    /**
     * 数据接受时间 结束时间
     */
    private String receivingEndTime;

    /**
     * 数据来源
     */
    private String dataSources;

    @Override
    public String toString() {
        return "SearchDataDto{" +
                "warnId='" + warnId + '\'' +
                ", earlyWarningPhone='" + earlyWarningPhone + '\'' +
                ", earlyWarningType='" + earlyWarningType + '\'' +
                ", earlyWarningLevel='" + earlyWarningLevel + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", receivingStartTime='" + receivingStartTime + '\'' +
                ", receivingEndTime='" + receivingEndTime + '\'' +
                ", dataSources='" + dataSources + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
