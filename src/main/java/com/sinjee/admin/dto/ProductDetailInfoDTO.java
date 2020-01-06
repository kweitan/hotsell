package com.sinjee.admin.dto;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -06
 * 商品明细表
 * @author kweitan
 */
@Data
public class ProductDetailInfoDTO extends BaseDTO{

    private Integer productDetailId ;

    private String productNumber ;

    private String productDetailIcon;

    private String productDetailField ;

    private String productDetailDescription ;
}
