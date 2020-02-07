package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 小小极客
 * 时间 2020/2/7 21:43
 * @ClassName WechatOrderDetailVO
 * 描述 WechatOrderDetailVO
 **/
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class WechatOrderDetailVO implements Serializable {

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
