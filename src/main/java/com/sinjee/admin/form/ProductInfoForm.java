package com.sinjee.admin.form;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    @Size(min = 6, max = 50, message = "商品名称长度应当在 6 ~ 50 个字符之间")
    private String productName ;

    //商品价格
    @NotBlank(message = "商品价格不能为空")
    private String productPrice ;

    //商品库存
    @NotBlank(message = "商品库存不能为空")
    private String productStock ;

    //商品描述
    @NotBlank(message = "商品描述不能为空")
    private String productDesc;

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
    private List<ProductCategoryInfoForm> categoryNewArrs ;

    /** 哈希编码 **/
    private String hashNumber ;

    //商品明细图
    @NotBlank(message = "商品明细图不能为空")
    private String productDetailIcon ;

    //商品详细描述内容
    private String productDetailDesc ;

    //商品属性
    @NotBlank(message = "商品属性不能为空")
    private String productDetailField;

    //排序ID
    private Integer sequenceId ;

    private Integer productStatus ;
}
