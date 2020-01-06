package com.sinjee.wechat.entity;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Data
public class AddressInfo extends WechatBaseEntity {

    private Integer addressId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;

    /** 是否选中 **/
    private Integer selectStatus;

    /** 地址标签 **/
    private String addressLabels;
}
