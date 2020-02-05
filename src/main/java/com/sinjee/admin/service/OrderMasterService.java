package com.sinjee.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.OrderMasterDTO;

import java.util.List;

public interface OrderMasterService {

    OrderMasterDTO save(OrderMasterDTO orderMasterDTO);

    OrderMasterDTO findByOrderNumber(String orderNumber);

    Integer update(OrderMasterDTO orderMasterDTO) ;

    Integer delete(String orderNumber);

    IPage<OrderMasterDTO> findByOpenId(Integer currentPage, Integer pageSize,String openId);

    IPage<OrderMasterDTO> findByOpenIdAndOrderStatus(Integer currentPage, Integer pageSize,String openId,Integer orderStatus);

    IPage<OrderMasterDTO> findAllByPage(Integer currentPage,Integer pageSize,Integer orderStatus,String orderNumber);

    Integer deleteByOpenId(String orderNumber,String openId);

    OrderMasterDTO pay(OrderMasterDTO orderMasterDTO);

}
