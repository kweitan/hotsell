package com.sinjee.admin.form;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 创建时间 2020 - 01 -19
 *
 * @author kweitan
 */
@Data
public class ProductInfoForm implements Serializable {
    //商品名称
    @NotBlank(message = "商品名称不能为空")
    @Max(value = 50,message = "太大了")
    private String productName ;

    //商品价格
    @NotBlank(message = "商品价格不能为空")
    private BigDecimal productPrice ;

    //商品库存
    @NotBlank(message = "商品库存不能为空")
    private Integer productStock ;

    //商品描述
    @NotBlank(message = "商品描述不能为空")
    private String productDescription;

    //商品图标
    @NotBlank(message = "商品图标不能为空")
    private String productIcon ;

    //商品规格
    @NotBlank(message = "商品规格不能为空")
    private String productStandard ;

    //商品提示 比如[热销 活动 爆销 优惠]
    @NotBlank(message = "商品提示不能为空")
    private String productTips ;

    //商品标签
    private String productLabels ;

    //商品单位
    @NotBlank(message = "商品单位不能为空")
    private String productUnit ;

    //商品编码
    private String productNumber ;

    //商品类目
    @NotBlank(message = "商品类目不能为空")
    private List<String> allCategoryLists ;

    /** 哈希编码 **/
    private String hashNumber ;
}
