package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.admin.dto.ExpressDeliveryDTO;
import com.sinjee.admin.entity.ExpressDelivery;
import com.sinjee.admin.mapper.ExpressDeliveryMapper;
import com.sinjee.admin.service.ExpressDeliveryService;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Service
@Slf4j
public class ExpressDeliveryServiceImpl implements ExpressDeliveryService {

    @Autowired
    private ExpressDeliveryMapper expressDeliveryMapper ;

    @Override
    public Integer save(ExpressDeliveryDTO expressDeliveryDTO) {
        ExpressDelivery expressDelivery = new ExpressDelivery() ;
        CacheBeanCopier.copy(expressDeliveryDTO,expressDelivery);
        return expressDeliveryMapper.insert(expressDelivery);
    }

    @Override
    public Integer update(ExpressDeliveryDTO expressDeliveryDTO) {
        ExpressDelivery expressDelivery = new ExpressDelivery() ;
        CacheBeanCopier.copy(expressDeliveryDTO,expressDelivery);

        QueryWrapper<ExpressDelivery> wrapper = new QueryWrapper();
        wrapper.eq("express_number",expressDeliveryDTO.getExpressNumber());

        return expressDeliveryMapper.update(expressDelivery,wrapper);
    }

    @Override
    public ExpressDeliveryDTO findByOrderNumber(String orderNumber) {

        QueryWrapper<ExpressDelivery> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber);
        ExpressDelivery expressDelivery = expressDeliveryMapper.selectOne(wrapper) ;
        ExpressDeliveryDTO expressDeliveryDTO = new ExpressDeliveryDTO() ;
        CacheBeanCopier.copy(expressDelivery,expressDeliveryDTO);

        return expressDeliveryDTO;
    }

    @Override
    public ExpressDeliveryDTO findByStrackingNumber(String trackingNumber) {
        QueryWrapper<ExpressDelivery> wrapper = new QueryWrapper();
        wrapper.eq("tracking_number",trackingNumber);
        ExpressDelivery expressDelivery = expressDeliveryMapper.selectOne(wrapper) ;
        ExpressDeliveryDTO expressDeliveryDTO = new ExpressDeliveryDTO() ;
        CacheBeanCopier.copy(expressDelivery,expressDeliveryDTO);

        return expressDeliveryDTO;
    }

    @Override
    public IPage<ExpressDeliveryDTO> selectExpressDeliveryByPage(Integer currentPage, Integer pageSize, String selectName) {

        QueryWrapper<ExpressDelivery> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("tracking_number",selectName);
        Page<ExpressDelivery> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<ExpressDelivery> mapPage = expressDeliveryMapper.selectPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<ExpressDelivery> expressDeliveryList = mapPage.getRecords() ;
        List<ExpressDeliveryDTO> expressDeliveryDTOList = BeanConversionUtils.CopyToAnotherList(ExpressDeliveryDTO.class,expressDeliveryList);

        Page<ExpressDeliveryDTO> expressDeliveryDTOPage = new Page<>(currentPage,pageSize) ;
        expressDeliveryDTOPage.setPages(mapPage.getPages());
        expressDeliveryDTOPage.setTotal(mapPage.getTotal());
        expressDeliveryDTOPage.setRecords(expressDeliveryDTOList) ;

        return expressDeliveryDTOPage;
    }
}
