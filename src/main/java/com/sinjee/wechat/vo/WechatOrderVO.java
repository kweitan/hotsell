package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sinjee.admin.entity.OrderDetail;
import com.sinjee.wechat.form.ShopCartModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/2/7 21:42
 * @ClassName WechatOrderVO
 * 描述 WechatOrderVO
 **/
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class WechatOrderVO implements Serializable {

    /** 订单编码 **/
    private String orderNumber ;

    /** 买家名字 **/
    private String buyerName ;

    /** 买家电话 **/
    private String buyerPhone ;

    /** 买家地址**/
    private String buyerAddress ;

    /*** 订单总金额 **/
    private BigDecimal orderAmount ;

    /**订单实际支付金额**/
    private BigDecimal actAmount ;

    /*** 订单状态，默认0-新下单（等待支付） 1-完结 2-取消 3-等待发货 4-等待收货 5-已收货 6-等待评价 **/
    private String orderStatus ;

    /** 支付状态，默认0-等待支付 1-支付成功 **/
    private String payStatus ;

    private String transactionId ;

    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime ;

    List<WechatOrderDetailVO> orderDetailList ;

    List<WechatProductReviewVO> productReviewLists ;

    private String buyerMessage ;

    private String hashNumber ;

    private boolean haveOne;

    private String orderRemark ;

    private String buyerId ;
}
