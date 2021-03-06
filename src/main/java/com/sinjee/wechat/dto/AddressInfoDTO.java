package com.sinjee.wechat.dto;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Data
public class AddressInfoDTO extends WechatBaseDTO{

    private Integer addressId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;

    /** 是否选中 **/
    private Integer selectStatus;

    /** 地址标签 **/
    private String addressLabels;

    private String openid ;

    private String hashNumber ;
}
