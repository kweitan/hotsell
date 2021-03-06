package com.sinjee.admin.entity;

import lombok.Data;

/**
 * @author 小小极客
 * 时间 2019/12/16 23:36
 * @ClassName ProductCategoryService
 * 描述 商品类目表
 **/
@Data
public class ProductCategory extends BaseEntity{
    /**类目ID**/
    private Integer categoryId ;

    /**类目名字**/
    private String categoryName ;

    /**类目编号**/
    private String categoryNumber ;

    /**类目链接**/
    private String categoryUrl ;

    /**类目小图**/
    private String categoryIcon ;

    /**0-未上架 1-上架*/
    private Integer categoryStatus ;

    /*** 是否属于首页*/
    private Integer belongIndex ;

    //排序ID
    private Integer sequenceId ;

}
