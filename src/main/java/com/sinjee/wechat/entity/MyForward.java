package com.sinjee.wechat.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建时间 2020 - 01 -20
 * 转发
 * @author kweitan
 */
@Data
public class MyForward extends WechatBaseEntity implements Serializable {

    private Integer myForwardId;

    /**商品编码**/
    private String productNumber ;

    /**商品名称**/
    private String productName ;

    /**商品单价**/
    private BigDecimal productPrice ;

    /**商品简单秒速**/
    private String productDescription ;

    /**商品小图**/
    private String productIcon ;

    /**用户openid**/
    private String openId ;
}
