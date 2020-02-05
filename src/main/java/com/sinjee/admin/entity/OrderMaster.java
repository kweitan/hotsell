package com.sinjee.admin.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Data
public class OrderMaster extends BaseEntity{

    private Integer orderId ;

    /** 订单编码 **/
    private String orderNumber ;

    /** 买家名字 **/
    private String buyerName ;

    /** 买家电话 **/
    private String buyerPhone ;

    /** 买家地址**/
    private String buyerAddress ;

    private String buyerOpenid ;

    /*** 订单总金额 **/
    private BigDecimal orderAmount ;

    /**订单实际支付金额**/
    private BigDecimal actAmount ;

    /*** 订单状态，默认0-新下单（等待支付） 1-完结 2-取消 3-等待发货 4-等待收货 5-已收货 6-等待评价 **/
    private String orderStatus ;

    /** 支付状态，默认0-等待支付 1-支付成功 **/
    private String payStatus ;

    private String transactionId ;
}
