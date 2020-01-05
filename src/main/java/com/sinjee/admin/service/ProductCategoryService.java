package com.sinjee.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductCategoryDTO;

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
            Integer currentPage, Integer pageSize);

    List<ProductCategoryDTO> getAllProductCategoryDTOList();
}
