package com.sinjee.wechat.entity;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -06
 * 买家信息
 * @author kweitan
 */
@Data
public class BuyerInfo extends WechatBaseEntity{

    private Integer buyerId ;

    private String buyerName ;

    private String buyerPassword;

    private String buyerIcon;

    private String openId ;
}
