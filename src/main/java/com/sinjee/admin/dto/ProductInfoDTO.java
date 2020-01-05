package com.sinjee.admin.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 小小极客
 * 时间 2019/12/15 16:32
 * @ClassName ProductInfoDTO
 * 描述 商品信息 DTO
 **/
@Data
public class ProductInfoDTO extends BaseDTO{

    //主键
    private Integer productId ;

    //商品名称
    private String productName ;

    //商品价格
    private BigDecimal productPrice ;

    //商品库存
    private Integer productStock ;

    //商品描述
    private String productDescription;

    //商品图标
    private String productIcon ;

    //商品规格
    private String productStandard ;

    //商品提示 比如[热销 活动 爆销 优惠]
    private String productTips ;

    //商品标签
    private String productLabels ;

    //商品单位
    private String productUnit ;

    //商品编码
    private String productNumber ;

    //商品状态
    private String productStatus ;

    //类目编码
    private String categoryNumber ;

    private Integer hashNumber ;
}
