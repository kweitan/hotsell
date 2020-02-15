package com.sinjee.admin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 小小极客
 * 时间 2019/12/15 12:10
 * @ClassName ProductInfoVO
 * 描述 商品信息
 **/
@Data
public class ProductInfoVO extends BaseVO implements Serializable {

    //商品名称
    @JsonProperty("goodName")
    private String productName ;

    //商品价格
    @JsonProperty("goodPrice")
    private BigDecimal productPrice ;

    //商品库存
    @JsonProperty("goodStock")
    private Integer productStock ;

    //商品描述
    @JsonProperty("goodDescription")
    private String productDescription;

    //商品图标
    @JsonProperty("goodIcon")
    private String productIcon ;

    //商品规格
    @JsonProperty("goodStandard")
    private String productStandard ;

    //商品提示 比如[热销 活动 爆销 优惠]
    @JsonProperty("goodTips")
    private String productTips ;

    //商品标签
    @JsonProperty("goodLabels")
    private String productLabels ;

    //商品单位
    @JsonProperty("goodUnit")
    private String productUnit ;

    //商品编码
    @JsonProperty("goodNumber")
    private String productNumber ;

    //类目编码
    @JsonProperty("goodCategoryNumber")
    private String categoryNumber ;

    /** 哈希编码 **/
    @JsonProperty("goodHashNumber")
    private String hashNumber ;

    @JsonProperty("selectName")
    private String selectName ;

    //商品状态
    @JsonProperty("goodStatus")
    private Integer productStatus ;

    private String productDetailIcon;

    private String productDetailField ;

    private String productDetailDescription ;
}
