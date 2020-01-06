package com.sinjee.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建时间 2020 - 01 -06
 * 商品详情
 * @author kweitan
 */
@Data
public class ProductDetailInfoVO implements Serializable {
    /** 商品编码 */
    private String productNumber ;

    /** 商品明细图 **/
    private String productDetailIcon ;

    /** 商品属性JSON对 **/
    private String productDetailField ;

    /** 商品明细描述 **/
    private String productDetailDescription ;
}
