package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.admin.dto.ProductCategoryDTO;
import com.sinjee.admin.entity.ProductCategory;
import com.sinjee.admin.mapper.ProductCategoryMapper;
import com.sinjee.admin.service.ProductCategoryMidService;
import com.sinjee.admin.service.ProductCategoryService;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2019/12/17 0:06
 * @ClassName ProductCategoryImpl
 * 描述 商品类目服务实现类
 **/
@Service
@Slf4j
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;


    @Autowired
    private ProductCategoryMidService productCategoryMidService ;

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
        productCategoryMidService.deleteByCategoryNumber(categoryNumber) ;
        return productCategoryMapper.invalidProductCategoryInfo(categoryNumber);
    }

    @Override
    public IPage<ProductCategoryDTO> selectProductCategoryInfoByPage(Integer currentPage, Integer pageSize,String selectName) {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).like("category_name",selectName);
        Page<ProductCategory> page = new Page<ProductCategory>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<ProductCategory> mapPage = productCategoryMapper.selectProductCategoryInfoByPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<ProductCategory> productCategoryList = mapPage.getRecords() ;
        List<ProductCategoryDTO> productCategoryDTOList = BeanConversionUtils.copyToAnotherList(ProductCategoryDTO.class,productCategoryList);

        Page<ProductCategoryDTO> productCategoryDTOPage = new Page<>(currentPage,pageSize) ;
        productCategoryDTOPage.setPages(mapPage.getPages()); //设置总页数
        productCategoryDTOPage.setTotal(mapPage.getTotal()); //设置总数
        productCategoryDTOPage.setRecords(productCategoryDTOList) ; //设置内容

        return productCategoryDTOPage;
    }

    public List<ProductCategoryDTO> getAllProductCategoryDTOList(){
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("category_status",1);

        List<ProductCategory> productCategoryList = productCategoryMapper.selectList(wrapper) ;

        List<ProductCategoryDTO> productCategoryDTOList = BeanConversionUtils.copyToAnotherList(ProductCategoryDTO.class,productCategoryList);
        return productCategoryDTOList ;
    }

    @Override
    public ProductCategoryDTO getProductCategoryDTOByNumber(String categoryNumber) {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("category_number",categoryNumber);
        ProductCategory productCategory = productCategoryMapper.selectOne(wrapper) ;
        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO() ;
        CacheBeanCopier.copy(productCategory,productCategoryDTO);

        return productCategoryDTO;
    }

    @Override
    public Integer upCategoryInfo(String categoryNumber) {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("category_number",categoryNumber)
        .eq("category_status",0);
        ProductCategory productCategory = new ProductCategory() ;
        productCategory.setCategoryStatus(1);
        return productCategoryMapper.update(productCategory,wrapper);
    }

    @Override
    public Integer downCategoryInfo(String categoryNumber) {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("category_number",categoryNumber)
                .eq("category_status",1);
        ProductCategory productCategory = new ProductCategory() ;
        productCategory.setCategoryStatus(0);
        return productCategoryMapper.update(productCategory,wrapper);
    }
}
