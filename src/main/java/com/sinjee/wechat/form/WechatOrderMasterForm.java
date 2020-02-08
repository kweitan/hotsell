package com.sinjee.wechat.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2020/2/8 17:07
 * @ClassName WechatOrderMasterForm
 * 描述 WechatOrderMasterForm
 **/
@Data
public class WechatOrderMasterForm implements Serializable {

    @NotEmpty(message="订单号必填")
    private String orderNumber ;

    @NotEmpty(message="哈希编码必填")
    private String hashNumber ;

    @NotEmpty(message="内容必填")
    private String productReviewLists ;
}
