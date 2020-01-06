package com.sinjee.admin.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Data
public class OrderDetailDTO extends BaseDTO{

    private Integer detailId ;

    /** 订单编码*/
    private String orderNumber ;

    /** 商品编码 **/
    private String productNumber ;

    /** 商品名称 **/
    private String productName ;

    /** 商品价格 **/
    private BigDecimal productPrice ;

    /** 商品数量 **/
    private Integer productQuantity ;

    /** 商品规格 **/
    private String productIcon ;

    /** 商品规格 **/
    private String productStandard ;

    /** 商品标签 **/
    private String productLabels ;

    /** 商品单位 **/
    private String productUnit ;
}
