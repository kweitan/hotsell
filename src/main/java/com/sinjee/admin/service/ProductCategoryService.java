package com.sinjee.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductCategoryDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface ProductCategoryService {
    /** 保存类目信息 **/
    Integer saveProductCategoryInfo(ProductCategoryDTO productCategoryDTO);

    /**更新类目信息**/
    Integer updateProductCategoryInfo(ProductCategoryDTO productCategoryDTO);

    /**设置类目信息无效**/
    Integer invalidProductCategoryInfo(String categoryNumber);

    /**分页查找类目信息**/
    IPage<ProductCategoryDTO> selectProductCategoryInfoByPage(
            Integer currentPage, Integer pageSize,String selectName);

    List<ProductCategoryDTO> getAllProductCategoryDTOList();

    ProductCategoryDTO getProductCategoryDTOByNumber(String categoryNumber);

    /**上架**/
    Integer upCategoryInfo(String categoryNumber);

    /**下架**/
    Integer downCategoryInfo(String categoryNumber);


    Integer save(ProductCategoryDTO productCategoryDTO) ;

    Integer update(ProductCategoryDTO productCategoryDTO) ;

    IPage<ProductCategoryDTO> selectProductCategoryBySearchName(
            Integer currentPage, Integer pageSize,String searchName);

    Integer countIndexNumber();

    boolean existCategoryInfo(String categoryNumber);

    Integer moveCategoryInfo(String categoryNumber,Integer type,Integer sequenceId);

    Integer findSequenceId();
}
