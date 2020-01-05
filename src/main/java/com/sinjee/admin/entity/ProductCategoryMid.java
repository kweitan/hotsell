package com.sinjee.admin.entity;

import lombok.Data;

/**
 * @author 小小极客
 * 时间 2019/12/17 23:25
 * @ClassName ProductCategoryMid
 * 描述 类目中间表
 **/
@Data
public class ProductCategoryMid extends BaseEntity{
    /** 类目中间表ID*/
    private Integer productCategoryMidId ;

    /** 商品编码字段*/
    private String productNumber ;

    /** 商品类目编码字段*/
    private  String categoryNumber ;
}
