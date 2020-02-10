package com.sinjee.wechat.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/2/10 12:21
 * @ClassName WechatOrderReviewForm
 * 描述 WechatOrderReviewForm
 **/
@Data
public class WechatOrderReviewForm implements Serializable {
    @NotEmpty(message="订单号必填")
    private String orderNumber ;

    @NotEmpty(message="哈希编码必填")
    private String hashNumber ;

    @NotEmpty(message="内容必填")
    private List<WechatProductReviewForm> productReviewLists ;
}
