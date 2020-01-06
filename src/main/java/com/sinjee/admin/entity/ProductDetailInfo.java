package com.sinjee.admin.entity;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -06
 * 产品明细表
 * @author kweitan
 */
@Data
public class ProductDetailInfo extends BaseEntity{

    private Integer productDetailId ;

    private String productNumber ;

    private String productDetailIcon;

    private String productDetailField ;

    private String productDetailDescription ;
}
