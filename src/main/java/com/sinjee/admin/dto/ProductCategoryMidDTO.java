package com.sinjee.admin.dto;

import lombok.Data;

/**
 * @author 小小极客
 * 时间 2019/12/17 23:29
 * @ClassName ProductCategoryMidDTO
 * 描述 类目中间表DTO
 **/
@Data
public class ProductCategoryMidDTO extends BaseDTO{
    /** 类目中间表ID*/
    private Integer productCategoryMidId ;

    /** 商品编码字段*/
    private String productNumber ;

    /** 商品类目编码字段*/
    private  String categoryNumber ;

}
