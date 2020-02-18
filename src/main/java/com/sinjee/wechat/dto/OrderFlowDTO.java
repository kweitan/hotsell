package com.sinjee.wechat.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 创建时间 2020 - 02 -18
 *--- NEW -> SUCCESS -> SHIPMENT -> FINISHED -> ---
 * --用户             -> REFUND -> CANCEL     -> REFUND -> CANCEL
 * @author kweitan
 */
@Data
public class OrderFlowDTO {

    private Integer orderFlowId ;

    //订单流水号
    private String orderFlowNumber ;

    //订单号
    private String orderNumber ;

    //上一个流水状态
    private String preFlowStatus ;

    //流水状态
    private String flowStatus ;

    //流水备注
    private String flowRemark ;

    //创建时间
    private Timestamp createTime ;

    //创建者
    private String creator ;

    //是否可用状态[0-不可用 1-可用 默认可用]
    private Integer enableFlag ;
}
