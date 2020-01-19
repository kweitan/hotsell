package com.sinjee.admin.form;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 创建时间 2020 - 01 -19
 *
 * @author kweitan
 */
@Data
public class ProductCategoryForm implements Serializable {

    @NotBlank(message = "商品类名不能为空")
    @Max(value = 50,message = "太大了")
    private String categoryName;

    private String categoryNumber ;

    /** 哈希编码 **/
    private String hashNumber ;

    @NotBlank(message = "商品状态不能为空")
    @Max(value=1,message="商品状态不能超过1位")
    private Integer categoryStatus ;
}
