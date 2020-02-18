package com.sinjee.common;

import com.sinjee.wechat.entity.OrderFlow;

/**
 * 创建时间 2020 - 02 -18
 * 提取公共部分
 * @author kweitan
 */
public class Common {

    public static OrderFlow getOrderFlow(String orderNumber,String creator,String remark,
                                         String flowStatus,String preFlowStatus){
        OrderFlow orderFlow = new OrderFlow() ;

        //生成流水号
        String flowNumber = IdUtil.genId() ;

        orderFlow.setOrderFlowNumber(flowNumber);
        orderFlow.setOrderNumber(orderNumber);
        orderFlow.setFlowRemark(remark);
        orderFlow.setFlowStatus(flowStatus);
        orderFlow.setPreFlowStatus(preFlowStatus);
        orderFlow.setEnableFlag(1);

        orderFlow.setCreator(creator);
        orderFlow.setCreateTime(DateUtils.getTimestamp());
        return orderFlow ;
    }
}
