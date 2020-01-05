package com.sinjee.admin.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 小小极客
 * 时间 2019/12/15 12:10
 * @ClassName ProductInfoVO
 * 描述 商品信息
 **/
@Data
public class ProductInfoVO {

    /** 商品ID **/
    @JsonProperty("id")
    private String productId;

    /** 商品名称 **/
    @JsonProperty("name")
    private String productName;

    /** 商品价格 **/
    @JsonProperty("price")
    private BigDecimal productPrice;

    /** 商品描述 **/
    @JsonProperty("description")
    private String productDescription;

    /** 商品图标 **/
    @JsonProperty("icon")
    private String productIcon;

    /** 商品单元 **/
    @JsonProperty("unit")
    private String productUnit;

    /** 商品标签 **/
    @JsonProperty("labels")
    private String productLabels;

    /** 商品规格 **/
    @JsonProperty("standard")
    private String productStandard;

    /** 商品提示 **/
    @JsonProperty("tips")
    private String productTips;
}
