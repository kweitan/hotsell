package com.sinjee.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductCategoryMidDTO;

import java.util.List;

public interface ProductCategoryMidService {

    Integer saveProductCategoryMidInfo(ProductCategoryMidDTO productCategoryMidDTO);

    /**更新商品类目中间表信息**/
    Integer updateProductCategoryMidInfo(ProductCategoryMidDTO productCategoryMidDTO);

    /**设置商品类目中间表信息无效**/
    Integer invalidProductCategoryMidInfo(Integer productCategoryMidId);

    /**分页查找商品类目中间表信息**/
    IPage<ProductCategoryMidDTO> selectProductCategoryMidInfoByPage(
            Integer currentPage, Integer pageSize);

    Integer update(ProductCategoryMidDTO productCategoryMidDTO) ;

    Integer delete(ProductCategoryMidDTO productCategoryMidDTO) ;

    List<ProductCategoryMidDTO> getListByProductNumber(String productNumber);

    List<ProductCategoryMidDTO> getListByProductCategoryNumber(String categoryNumber);

    Integer deleteByProductNumber(String productNumber);

    Integer deleteByCategoryNumber(String categoryNumber);

    Integer save(ProductCategoryMidDTO productCategoryMidDTO) ;
}
