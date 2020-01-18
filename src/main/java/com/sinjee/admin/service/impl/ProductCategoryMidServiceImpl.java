package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.admin.dto.ProductCategoryMidDTO;
import com.sinjee.admin.entity.ProductCategoryMid;
import com.sinjee.admin.mapper.ProductCategoryMidMapper;
import com.sinjee.admin.service.ProductCategoryMidService;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建时间 2019 - 12 -22
 *
 * @author kweitan
 */
@Service
@Slf4j
public class ProductCategoryMidServiceImpl implements ProductCategoryMidService {

    @Autowired
    private ProductCategoryMidMapper productCategoryMidMapper;

    @Override
    public Integer saveProductCategoryMidInfo(ProductCategoryMidDTO productCategoryMidDTO) {
        ProductCategoryMid productCategoryMid = new ProductCategoryMid();
        CacheBeanCopier.copy(productCategoryMidDTO,productCategoryMid);
        return productCategoryMidMapper.saveProductCategoryMidInfo(productCategoryMid);
    }

    @Override
    public Integer updateProductCategoryMidInfo(ProductCategoryMidDTO productCategoryMidDTO) {
        ProductCategoryMid productCategoryMid = new ProductCategoryMid();
        CacheBeanCopier.copy(productCategoryMidDTO,productCategoryMid);

        return productCategoryMidMapper.updateProductCategoryMidInfo(productCategoryMid);
    }

    @Override
    public Integer invalidProductCategoryMidInfo(Integer productCategoryMidId) {
        return productCategoryMidMapper.invalidProductCategoryMidInfo(productCategoryMidId);
    }

    @Override
    public IPage<ProductCategoryMidDTO> selectProductCategoryMidInfoByPage(Integer currentPage, Integer pageSize) {
        QueryWrapper<ProductCategoryMid> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1);
        Page<ProductCategoryMid> page = new Page<ProductCategoryMid>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<ProductCategoryMid> mapPage = productCategoryMidMapper.selectProductCategoryMidInfoBypage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<ProductCategoryMid> productCategoryMidList = mapPage.getRecords() ;
        List<ProductCategoryMidDTO> productCategoryMidDTOList = BeanConversionUtils.CopyToAnotherList(ProductCategoryMidDTO.class,productCategoryMidList);

        Page<ProductCategoryMidDTO> productCategoryMidDTOPage = new Page<>(currentPage,pageSize) ;
        productCategoryMidDTOPage.setPages(mapPage.getPages()); //设置总页数
        productCategoryMidDTOPage.setTotal(mapPage.getTotal()); //设置总数
        productCategoryMidDTOPage.setRecords(productCategoryMidDTOList) ; //设置内容
        return productCategoryMidDTOPage;
    }
}
