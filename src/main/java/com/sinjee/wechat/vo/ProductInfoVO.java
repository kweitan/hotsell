package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建时间 2019 - 12 -22
 * 商品信息表
 * @author kweitan
 */
@Data
public class ProductInfoVO implements Serializable {

    //商品名称
    @JsonProperty("productName")
    private String productName ;

    //商品价格
    @JsonProperty("productPrice")
    private BigDecimal productPrice ;

    //商品库存
    @JsonProperty("productStock")
    private Integer productStock ;

    //商品描述
    @JsonProperty("productDesc")
    private String productDescription;

    //商品图标
    @JsonProperty("productImage")
    private String productIcon ;

    //商品规格
    @JsonProperty("goodStandard")
    private String productStandard ;

    //商品提示 比如[热销 活动 爆销 优惠]
    @JsonProperty("productTips")
    private String productTips ;

    //商品标签
    @JsonProperty("productLabes")
    private String productLabels ;

    //商品单位
    @JsonProperty("productUnit")
    private String productUnit ;

    //商品编码
    @JsonProperty("productId")
    private String productNumber ;

    //类目编码
    @JsonProperty("goodCategoryNumber")
    private String categoryNumber ;

    /** 哈希编码 **/
    @JsonProperty("hashId")
    private String hashNumber ;

}
