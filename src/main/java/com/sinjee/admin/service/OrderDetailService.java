package com.sinjee.admin.service;

import com.sinjee.admin.dto.OrderDetailDTO;

import java.util.List;

public interface OrderDetailService {

    Integer save(OrderDetailDTO orderDetailDTO);

    Integer update(OrderDetailDTO orderDetailDTO);

    Integer delete(OrderDetailDTO orderDetailDTO);

    List<OrderDetailDTO> findByOrderNumber(String orderNumber);
}
