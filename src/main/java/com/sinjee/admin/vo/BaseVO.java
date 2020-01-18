package com.sinjee.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 小小极客
 * 时间 2020/1/18 15:36
 * @ClassName BaseVO
 * 描述 vo基础类
 **/
@Data
public class BaseVO {
    //创建时间
    @JsonProperty("createTime")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime ;

    //创建者
    @JsonProperty("creator")
    private String creator ;

    //更新时间
    @JsonProperty("updateTime")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime ;

    //更新者
    @JsonProperty("updater")
    private String updater ;
}
