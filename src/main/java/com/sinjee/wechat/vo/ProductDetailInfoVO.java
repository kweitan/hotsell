package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 创建时间 2020 - 01 -06
 * 商品详情
 * @author kweitan
 */
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ProductDetailInfoVO extends ProductInfoVO implements Serializable {
    /** 商品编码 */
    private String productNumber ;

    /** 商品明细图 **/
    private String productDetailIcon ;

    /** 商品属性JSON对 **/
    private String productDetailField ;

    /** 商品明细描述 **/
    private String productDetailDescription ;

    private Integer productReviewId ;

    private Integer productReviewLevel;

    private String productReviewContent ;

    private Integer buyerReviewId ;

    /**评论数量**/
    private Integer productReviewCount ;


    /**图像**/
    private String personIcon ;

    /**昵称**/
    private String personName ;

    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp createTime ;

}
