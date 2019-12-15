package com.sinjee.wechat.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 小小极客
 * 时间 2019/12/15 15:18
 * @ClassName BaseEntity
 * 描述 BaseEntity 基本实体类
 **/
@Data
public class BaseEntity {
    //创建时间
    private Timestamp createTime ;

    //创建者
    private String creator ;

    //更新时间
    private Timestamp updateTime ;

    //更新者
    private String updater ;

    //是否可用状态[0-不可用 1-可用 默认可用]
    private Integer enableFlag ;
}
