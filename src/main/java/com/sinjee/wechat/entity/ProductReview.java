package com.sinjee.wechat.entity;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -29
 *
 * @author kweitan
 */
@Data
public class ProductReview extends WechatBaseEntity{

    private Integer productReviewId ;

    private Integer productReviewLevel;

    private String productReviewContent ;

    private Integer buyerReviewId ;

    private String productNumber;

    /**图像**/
    private String personIcon ;

    /**昵称**/
    private String personName ;
}
