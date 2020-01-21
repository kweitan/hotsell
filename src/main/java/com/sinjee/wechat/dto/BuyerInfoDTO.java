package com.sinjee.wechat.dto;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Data
public class BuyerInfoDTO extends WechatBaseDTO {
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
