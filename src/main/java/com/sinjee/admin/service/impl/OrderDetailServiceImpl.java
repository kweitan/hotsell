package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sinjee.admin.dto.OrderDetailDTO;
import com.sinjee.admin.entity.OrderDetail;
import com.sinjee.admin.mapper.OrderDetailMapper;
import com.sinjee.admin.service.OrderDetailService;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/1/19 23:29
 * @ClassName OrderDetailServiceImpl
 * 描述 订单明细类
 **/

@Service
@Slf4j
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper ;

    @Override
    @Transactional
    public Integer save(OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = new OrderDetail();
        CacheBeanCopier.copy(orderDetailDTO,orderDetail);
        return orderDetailMapper.insert(orderDetail);
    }

    @Override
    @Transactional
    public Integer update(OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = new OrderDetail();
        CacheBeanCopier.copy(orderDetailDTO,orderDetail);
        QueryWrapper<OrderDetail> wrapper = new QueryWrapper();
        wrapper.eq("detail_id",orderDetailDTO.getDetailId()).eq("enable_flag",1);
        return orderDetailMapper.update(orderDetail,wrapper);
    }

    @Override
    @Transactional
    public Integer delete(OrderDetailDTO orderDetailDTO) {
        QueryWrapper<OrderDetail> wrapper = new QueryWrapper();
        wrapper.eq("detail_id",orderDetailDTO.getDetailId()).eq("enable_flag",1);
        return orderDetailMapper.delete(wrapper);
    }

    @Override
    public List<OrderDetailDTO> findByOrderNumber(String orderNumber) {
        QueryWrapper<OrderDetail> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber).eq("enable_flag",1);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(wrapper);
        List<OrderDetailDTO> orderDetailDTOList = BeanConversionUtils.copyToAnotherList(OrderDetailDTO.class,orderDetailList);
        return orderDetailDTOList ;
    }
}
