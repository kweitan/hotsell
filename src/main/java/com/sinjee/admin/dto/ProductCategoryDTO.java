package com.sinjee.admin.dto;

import lombok.Data;

/**
 * @author 小小极客
 * 时间 2019/12/17 0:05
 * @ClassName ProductCategoryDTO
 * 描述 商品类目DTO
 **/
@Data
public class ProductCategoryDTO extends BaseDTO{
    /**类目ID**/
    private Integer categoryId ;

    /**类目名字**/
    private String categoryName ;

    /**类目编号**/
    private String categoryNumber ;

    /**0-未上架 1-上架*/
    private Integer categoryStatus ;
}
