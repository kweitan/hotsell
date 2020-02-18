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

import com.sinjee.exceptions.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Transactional
    public Integer saveProductCategoryInfo(ProductCategoryDTO productCategoryDTO) {
        ProductCategory productCategory = new ProductCategory();
        CacheBeanCopier.copy(productCategoryDTO,productCategory);
        return productCategoryMapper.insert(productCategory);
    }

    @Override
    @Transactional
    public Integer updateProductCategoryInfo(ProductCategoryDTO productCategoryDTO) {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).like("category_number",productCategoryDTO.getCategoryNumber());

        ProductCategory productCategory = new ProductCategory();
        productCategory.setBelongIndex(productCategoryDTO.getBelongIndex());
        productCategory.setCategoryIcon(productCategoryDTO.getCategoryIcon());
        productCategory.setCategoryName(productCategoryDTO.getCategoryName());
        return productCategoryMapper.update(productCategory,wrapper) ;
    }

    @Override
    @Transactional
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
        IPage<ProductCategory> mapPage = productCategoryMapper.selectPage(page,wrapper);
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
        //1-表示类目上架 0-表示类目下架
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

    @Override
    public Integer save(ProductCategoryDTO productCategoryDTO) {
        ProductCategory productCategory = new ProductCategory() ;
        CacheBeanCopier.copy(productCategoryDTO,productCategory);
        return productCategoryMapper.insert(productCategory);
    }

    @Override
    public Integer update(ProductCategoryDTO productCategoryDTO) {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("category_number",productCategoryDTO.getCategoryNumber())
                .eq("category_status",1);
        ProductCategory productCategory = new ProductCategory() ;
        CacheBeanCopier.copy(productCategoryDTO,productCategory);
        return productCategoryMapper.update(productCategory,wrapper);
    }

    @Override
    public IPage<ProductCategoryDTO> selectProductCategoryBySearchName(Integer currentPage, Integer pageSize, String searchName) {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).like("category_name",searchName);
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

    @Override
    public Integer countIndexNumber() {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("category_status",1)
        .eq("belong_index",1);
        return productCategoryMapper.selectCount(wrapper) ;
    }

    @Override
    public boolean existCategoryInfo(String categoryNumber) {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("category_number",categoryNumber).eq("category_status",1)
                .eq("belong_index",1);
        Integer res = productCategoryMapper.selectCount(wrapper);
        if (res > 0){
            return true;
        }
        return false;
    }

    @Override
    public Integer moveCategoryInfo(String categoryNumber, Integer type, Integer sequenceId) {
        Map<String,Object> map = new HashMap<>() ;
        map.put("sequenceId",sequenceId) ;
        map.put("type",type);
        map.put("categoryNumber",categoryNumber) ;
        ProductCategory productInfo = productCategoryMapper.moveProductCategory(map) ;
        if (null == productInfo && type == 0){
            return -1 ; //-1表示 到顶了
        }else if(null == productInfo && type == 1){
            return -2 ; //-1表示 到底了
        }

        int tempId = sequenceId;
        QueryWrapper<ProductCategory> oldWrapper = new QueryWrapper();
        oldWrapper.eq("enable_flag",1).eq("product_number",categoryNumber);
        ProductCategory oldProductInfo = new ProductCategory();
        oldProductInfo.setSequenceId(productInfo.getSequenceId());
        Integer res1 = productCategoryMapper.update(oldProductInfo,oldWrapper);

        QueryWrapper<ProductCategory> newWrapper = new QueryWrapper();
        oldWrapper.eq("enable_flag",1).eq("product_number",productInfo.getCategoryNumber());
        ProductCategory newProductInfo = new ProductCategory();
        newProductInfo.setSequenceId(tempId);
        Integer res2 = productCategoryMapper.update(newProductInfo,newWrapper) ;
        if (res1 > 0 && res2 >0){
            return 1 ; //成功
        }else {
            //回滚事务
            throw new MyException(101,"上下移动更新失败") ;
        }
    }

    @Override
    public Integer findSequenceId() {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).orderByDesc("sequence_id");
        List<ProductCategory> productCategoryList = productCategoryMapper.selectList(wrapper) ;
        if (null == productCategoryList || productCategoryList.size() == 0){
            return 1 ;
        }

        return productCategoryList.get(0).getSequenceId() + 1;
    }
}
