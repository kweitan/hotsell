package com.sinjee.wechat.form;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2020/2/8 16:35
 * @ClassName WechatProductReviewForm
 * 描述 WechatProductReviewForm
 **/
@Data
@Slf4j
public class WechatProductReviewForm implements Serializable {

    @NotEmpty(message="评价级别必填")
    private Integer productReviewLevel;

    @NotEmpty(message="评价内容必填")
    private String productReviewContent ;

    @NotEmpty(message="产品编码必填")
    private String productNumber;

    @NotEmpty(message="产品名称必填")
    private String productName;
}
