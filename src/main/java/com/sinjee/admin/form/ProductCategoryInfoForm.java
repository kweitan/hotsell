package com.sinjee.admin.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2020/2/14 18:37
 * @ClassName ProductCategoryInfoForm
 * 描述 ProductCategoryInfoForm
 **/
@Data
public class ProductCategoryInfoForm implements Serializable {

    @NotBlank(message = "类目编码不能为空")
    private String categoryNumber;

    @NotBlank(message = "哈希编码不能为空")
    private String goodHashNumber ;
}
