package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.ProductCategoryDTO;

public interface ProductCategory {
    Integer saveProductCategoryInfo(ProductCategoryDTO productCategory);

    /**更新类目信息**/
    Integer updateProductCategoryInfo(ProductCategoryDTO productCategory);

    /**设置类目信息无效**/
    Integer invalidProductCategoryInfo(String categoryNumber);

    /**分页查找类目信息**/
    IPage<ProductCategoryDTO> selectProductCategoryInfoBypage(
            IPage<ProductCategoryDTO> page);
}
