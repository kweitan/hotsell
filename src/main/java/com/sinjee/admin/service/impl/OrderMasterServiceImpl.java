package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.admin.dto.OrderMasterDTO;
import com.sinjee.admin.entity.OrderMaster;
import com.sinjee.admin.mapper.OrderMasterMapper;
import com.sinjee.admin.service.OrderMasterService;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Transactional
@Service
@Slf4j
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private OrderMasterMapper orderMasterMapper ;

    @Override
    public Integer save(OrderMasterDTO orderMasterDTO) {
        OrderMaster orderMaster = new OrderMaster() ;
        CacheBeanCopier.copy(orderMasterDTO,orderMaster);
        return orderMasterMapper.insert(orderMaster);
    }

    @Override
    public Integer update(OrderMasterDTO orderMasterDTO) {
        OrderMaster orderMaster = new OrderMaster() ;
        CacheBeanCopier.copy(orderMasterDTO,orderMaster);

        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderMasterDTO.getOrderNumber());
        return orderMasterMapper.update(orderMaster,wrapper);
    }

    @Override
    public Integer delete(String orderNumber) {
        OrderMaster orderMaster = new OrderMaster() ;
        orderMaster.setEnableFlag(0);
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber);
        return orderMasterMapper.update(orderMaster,wrapper);
    }

    @Override
    public IPage<OrderMasterDTO> findByOpenId(Integer currentPage, Integer pageSize,String openId) {
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("buyer_openid",openId).eq("enable_flag",1);

        Page<OrderMaster> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<OrderMaster> mapPage = orderMasterMapper.selectPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<OrderMaster> orderMasterList = mapPage.getRecords() ;
        List<OrderMasterDTO> orderMasterDTOList = BeanConversionUtils.CopyToAnotherList(OrderMasterDTO.class,orderMasterList);

        Page<OrderMasterDTO> orderMasterDTOPage = new Page<>(currentPage,pageSize) ;
        orderMasterDTOPage.setPages(mapPage.getPages());
        orderMasterDTOPage.setTotal(mapPage.getTotal());
        orderMasterDTOPage.setRecords(orderMasterDTOList) ;

        return orderMasterDTOPage;
    }

    @Override
    public IPage<OrderMasterDTO> findByOpenIdAndOrderStatus(Integer currentPage, Integer pageSize,String openId, Integer orderStatus) {
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("buyer_openid",openId).eq("enable_flag",1).eq("order_status",orderStatus);

        Page<OrderMaster> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<OrderMaster> mapPage = orderMasterMapper.selectPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<OrderMaster> orderMasterList = mapPage.getRecords() ;
        List<OrderMasterDTO> orderMasterDTOList = BeanConversionUtils.CopyToAnotherList(OrderMasterDTO.class,orderMasterList);

        Page<OrderMasterDTO> orderMasterDTOPage = new Page<>(currentPage,pageSize) ;
        orderMasterDTOPage.setPages(mapPage.getPages());
        orderMasterDTOPage.setTotal(mapPage.getTotal());
        orderMasterDTOPage.setRecords(orderMasterDTOList) ;

        return orderMasterDTOPage;
    }

    @Override
    public IPage<OrderMasterDTO> findAllByPage(Integer currentPage, Integer pageSize, Integer orderStatus, String orderNumber) {
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber).eq("enable_flag",1).eq("order_status",orderStatus);

        Page<OrderMaster> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<OrderMaster> mapPage = orderMasterMapper.selectPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<OrderMaster> orderMasterList = mapPage.getRecords() ;
        List<OrderMasterDTO> orderMasterDTOList = BeanConversionUtils.CopyToAnotherList(OrderMasterDTO.class,orderMasterList);

        Page<OrderMasterDTO> orderMasterDTOPage = new Page<>(currentPage,pageSize) ;
        orderMasterDTOPage.setPages(mapPage.getPages());
        orderMasterDTOPage.setTotal(mapPage.getTotal());
        orderMasterDTOPage.setRecords(orderMasterDTOList) ;

        return orderMasterDTOPage;
    }

    @Override
    public Integer deleteByOpenId(String orderNumber, String openId) {
        OrderMaster orderMaster = new OrderMaster() ;
        orderMaster.setEnableFlag(0);
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber).eq("buyer_openid",openId);
        return orderMasterMapper.update(orderMaster,wrapper);
    }
}
