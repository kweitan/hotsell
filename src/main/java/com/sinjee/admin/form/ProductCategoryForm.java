package com.sinjee.admin.form;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 创建时间 2020 - 01 -19
 *
 * @author kweitan
 */
@Data
public class ProductCategoryForm implements Serializable {

    @NotBlank(message = "商品类名不能为空")
    @Size(min = 1, max = 50, message = "商品名称长度应当在 6 ~ 50 个字符之间")
    private String categoryName;

    private String categoryNumber ;

    /** 哈希编码 **/
    private String hashNumber ;

    /**类目链接**/
    private String categoryUrl ;

    /**类目小图**/
    private String categoryIcon ;

    /**0-未上架 1-上架*/
    private Integer categoryStatus ;


    /*** 是否属于首页*/
    private String belongIndex ;

    //排序ID
    private Integer sequenceId ;
}
