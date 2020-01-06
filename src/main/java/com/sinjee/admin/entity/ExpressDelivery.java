package com.sinjee.admin.entity;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -06
 * 快递信息表
 * @author kweitan
 */
@Data
public class ExpressDelivery extends BaseEntity {

    private Integer expressId ;

    /** 快递编码*/
    private String expressNumber ;

    /*** 快递公司名称 **/
    private String expressCorName ;

    /*** 快递公司简称 **/
    private String expressCorAbbreviation ;

    /*** 运单编号**/
    private String trackingNumber ;

    /*** 订单编号**/
    private String orderNumber ;


}
