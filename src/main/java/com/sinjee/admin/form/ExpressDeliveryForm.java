package com.sinjee.admin.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 创建时间 2020 - 02 -18
 *
 * @author kweitan
 */
@Data
public class ExpressDeliveryForm implements Serializable {

    @NotBlank(message = "订单不能为空")
    private String orderNumber ;

    @NotBlank(message = "哈希编码不能为空")
    private String hashNumber ;

    /*** 快递公司名称 **/
    @NotBlank(message = "快递公司名称不能为空")
    private String expressCorName ;

    /*** 快递公司简称 **/
    @NotBlank(message = "快递公司简称不能为空")
    private String expressCorAbbreviation ;

    /*** 运单编号**/
    @NotBlank(message = "运单号不能为空")
    private String trackingNumber ;

}
