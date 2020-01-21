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

    private String avatarUrl;

    private String openId ;

    private String buyerCountry;

    private String buyerProvince;

    private String buyerCity;

    private Integer buyerGender;

    private String unionId ;

    private String sessionKey ;
}
