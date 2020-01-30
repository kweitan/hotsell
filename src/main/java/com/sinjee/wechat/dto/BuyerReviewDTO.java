package com.sinjee.wechat.dto;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Data
public class BuyerReviewDTO extends WechatBaseDTO {

    private Integer buyerReviewId ;

    private String orderNumber ;

    private String openId ;
}
