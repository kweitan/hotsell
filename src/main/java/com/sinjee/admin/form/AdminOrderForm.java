package com.sinjee.admin.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 创建时间 2020 - 02 -21
 * AdminOrderForm
 * @author kweitan
 */
@Getter
@Setter
public class AdminOrderForm implements Serializable {

    /** 订单符号 **/
    @NotBlank(message = "订单不能为空")
    private String orderNumber ;

    @NotBlank(message = "哈希不能为空")
    private String hashNumber ;

    /** 加减符号 **/
    @NotBlank(message = "符号不能为空")
    private String symbol ;

    /** 实际支付金额 **/
    @NotBlank(message = "总付款金额不能为空")
    private String actPayFee ;

    /** 加减金额 **/
    @NotBlank(message = "加减金额不能为空")
    private String actFee ;
}
