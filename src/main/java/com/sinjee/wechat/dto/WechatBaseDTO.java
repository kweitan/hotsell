package com.sinjee.wechat.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Data
public class WechatBaseDTO {
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
