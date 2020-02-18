package com.sinjee.admin.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建时间 2020 - 02 -18
 *
 * @author kweitan
 */
@Data
public class RefundOrderDTO extends  BaseDTO {

    private Integer refundOrderId ;

    /**退款单号 自己生成**/
    private String refundNumber ;

    /**订单号 属于订单表**/
    private String orderNumber  ;

    /**订单金额**/
    private BigDecimal totalFee ;

    /**退款金额**/
    private BigDecimal refundFee ;

    /**退款币种类型 CNY默认**/
    private String refundFeeType ;

    /**退款原因**/
    private String refundDesc ;

    /**退款状态**/
    private String refundStatus ;
}
