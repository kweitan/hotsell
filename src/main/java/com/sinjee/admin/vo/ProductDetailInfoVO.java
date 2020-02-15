package com.sinjee.admin.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2020/2/15 0:44
 * @ClassName ProductDetailInfoVO
 * 描述 ProductDetailInfoVO
 **/
@Data
public class ProductDetailInfoVO extends BaseVO implements Serializable {
    private String productNumber ;

    private String productDetailIcon;

    private String productDetailField ;

    private String productDetailDescription ;
}
