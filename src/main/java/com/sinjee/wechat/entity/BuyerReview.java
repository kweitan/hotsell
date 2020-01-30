package com.sinjee.wechat.entity;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -06
 * 买家评论实体
 * @author kweitan
 */
@Data
public class BuyerReview extends WechatBaseEntity{

    private Integer buyerReviewId ;

    /** 订单编码 **/
    private String orderNumber ;

    private String openId ;

}
