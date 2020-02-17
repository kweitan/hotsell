package com.sinjee.admin.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 创建时间 2020 - 02 -17
 * 商户信息
 * @author kweitan
 */
@Setter
@Getter
public class MerchantInfo extends BaseEntity implements Serializable {

    private Integer merchantId;

    private Integer userId;

    private String merchantNumber;

    //商户电话
    private String merchantPhone;

    //商户名称
    private String merchantName;

    //商户描述
    private String merchantDesc;

    //商户logo
    private String merchantLogo;

    //商户微信号
    private String merchantWechatNumber;

    //商户支付宝
    private String merchantPayNumber;

    //商户QQ
    private String merchantQQ;

    //商户资质描述
    private String merchantQualificationDesc;

    //商户资质证书
    private String merchantQualificationIcon;
}

