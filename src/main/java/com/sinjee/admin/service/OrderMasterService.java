package com.sinjee.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.OrderMasterDTO;
import com.sinjee.admin.entity.ExpressDelivery;
import com.sinjee.admin.entity.OrderMaster;


public interface OrderMasterService {

    OrderMasterDTO save(OrderMasterDTO orderMasterDTO);

    OrderMasterDTO findByOrderNumberAndOpenid(String orderNumber,String openid);

    OrderMasterDTO findByOrderNumber(String orderNumber);

    Integer update(OrderMasterDTO orderMasterDTO) ;

    Integer delete(String orderNumber);

    IPage<OrderMasterDTO> findByOpenId(Integer currentPage, Integer pageSize,String openId);

    IPage<OrderMasterDTO> findByOpenIdAndOrderStatus(Integer currentPage, Integer pageSize,String openId,Integer orderStatus);

    IPage<OrderMasterDTO> findAllByPage(Integer currentPage,Integer pageSize,Integer orderStatus,String orderNumber);

    Integer deleteByOpenId(String orderNumber,String openId);

    OrderMasterDTO pay(OrderMasterDTO orderMasterDTO);

    Integer cancelOrder(String orderNumber,String openid);

    Integer applyOrder(String orderNumber,String openid,String refundType,String refundDesc);

    Integer updataOrderStatus(QueryWrapper<OrderMaster> wrapper,String orderStatus,String payStatus,String orderNumber,String openid);

    IPage<OrderMasterDTO> findByTpye(Integer currentPage, Integer pageSize,String openid,String type);

    IPage<OrderMasterDTO> selectOrderMasterInfo(Integer currentPage, Integer pageSize, String openid,String orderStatus,String payStatus);

    /** 中台搜索所有订单 **/
    IPage<OrderMasterDTO> findOrderAllList(Integer currentPage, Integer pageSize,String searchType,String orderNumber);

    /** 中台 填写运单号**/
    Integer enterTrackingNumber(String creator,String orderNumber, ExpressDelivery expressDelivery);

    /** 中台 订单想起专用**/
    OrderMasterDTO lookupDetailByOrderNumber(String orderNumber);

    /** 中台 修改订单支付金额**/
    Integer modifyActFee(String creator,String orderNumber,String actFee);

}
