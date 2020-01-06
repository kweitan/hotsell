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

    /** 买家对手服务的评级 json格式**/
    private String buyerReviewLevel ;

    /** 买家对订单内所有商品的评价 <key,value> 如:<'商品编码','好评或者差评'>**/
    private String buyerReviewProduct;

    /** 订单编码 **/
    private String orderNumber ;
    private String openId ;

    /** 评价内容 **/
    private String buyerReviewContent;
}
