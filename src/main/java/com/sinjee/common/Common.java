package com.sinjee.common;

import com.sinjee.admin.dto.SellerInfoDTO;
import com.sinjee.exceptions.SellerAuthorizeException;
import com.sinjee.wechat.entity.OrderFlow;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * 创建时间 2020 - 02 -18
 * 提取公共部分
 * @author kweitan
 */
@Slf4j
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

    public static SellerInfoDTO getSellerInfo(HttpServletRequest request,RedisUtil redisUtil){
        String sellerNumber = (String)request.getAttribute("sellerNumber") ;
        Object object = redisUtil.getString(sellerNumber) ;
        if (object == null){
            log.info("已经过期重新登录");
            throw new SellerAuthorizeException() ;
        }
        SellerInfoDTO sellerInfoDTO = (SellerInfoDTO)object ;
        return sellerInfoDTO ;
    }
}
