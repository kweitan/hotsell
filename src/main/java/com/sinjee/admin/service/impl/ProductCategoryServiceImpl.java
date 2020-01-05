package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.admin.dto.ProductCategoryDTO;
import com.sinjee.admin.entity.ProductCategory;
import com.sinjee.admin.mapper.ProductCategoryMapper;
import com.sinjee.admin.service.ProductCategoryService;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2019/12/17 0:06
 * @ClassName ProductCategoryImpl
 * 描述 商品类目服务实现类
 **/
@Service
@Slf4j
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public Integer saveProductCategoryInfo(ProductCategoryDTO productCategoryDTO) {
        ProductCategory productCategory = new ProductCategory();
        CacheBeanCopier.copy(productCategoryDTO,productCategory);
        return productCategoryMapper.saveProductCategoryInfo(productCategory);
    }

    @Override
    public Integer updateProductCategoryInfo(ProductCategoryDTO productCategoryDTO) {
        ProductCategory productCategory = new ProductCategory();
        CacheBeanCopier.copy(productCategoryDTO,productCategory);
        return productCategoryMapper.updateProductCategoryInfo(productCategory);
    }

    @Override
    public Integer invalidProductCategoryInfo(String categoryNumber) {
        return productCategoryMapper.invalidProductCategoryInfo(categoryNumber);
    }

    @Override
    public IPage<ProductCategoryDTO> selectProductCategoryInfoByPage(Integer currentPage, Integer pageSize) {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1);
        Page<ProductCategory> page = new Page<ProductCategory>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<ProductCategory> mapPage = productCategoryMapper.selectProductCategoryInfoByPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<ProductCategory> productCategoryList = mapPage.getRecords() ;
        List<ProductCategoryDTO> productCategoryDTOList = BeanConversionUtils.CopyToAnotherList(ProductCategoryDTO.class,productCategoryList);

        Page<ProductCategoryDTO> productCategoryDTOPage = new Page<>(currentPage,pageSize) ;
        productCategoryDTOPage.setPages(mapPage.getPages()); //设置总页数
        productCategoryDTOPage.setTotal(mapPage.getTotal()); //设置总数
        productCategoryDTOPage.setRecords(productCategoryDTOList) ; //设置内容

        return productCategoryDTOPage;
    }

    public List<ProductCategoryDTO> getAllProductCategoryDTOList(){
        List<ProductCategory> productCategoryList = productCategoryMapper.selectList(null) ;
        List<ProductCategoryDTO> productCategoryDTOList = BeanConversionUtils.CopyToAnotherList(ProductCategoryDTO.class,productCategoryList);
        return productCategoryDTOList ;
    }
}
