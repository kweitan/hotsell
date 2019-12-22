package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.ProductCategoryDTO;

public interface ProductCategoryService {
    Integer saveProductCategoryInfo(ProductCategoryDTO productCategoryDTO);

    /**更新类目信息**/
    Integer updateProductCategoryInfo(ProductCategoryDTO productCategoryDTO);

    /**设置类目信息无效**/
    Integer invalidProductCategoryInfo(String categoryNumber);

    /**分页查找类目信息**/
    IPage<ProductCategoryDTO> selectProductCategoryInfoByPage(
            Integer currentPage, Integer pageSize);
}
