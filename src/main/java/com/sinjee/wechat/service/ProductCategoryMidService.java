package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.ProductCategoryMidDTO;

public interface ProductCategoryMidService {

    Integer saveProductCategoryMidInfo(ProductCategoryMidDTO productCategoryMidDTO);

    /**更新商品类目中间表信息**/
    Integer updateProductCategoryMidInfo(ProductCategoryMidDTO productCategoryMidDTO);

    /**设置商品类目中间表信息无效**/
    Integer invalidProductCategoryMidInfo(Integer productCategoryMidId);

    /**分页查找商品类目中间表信息**/
    IPage<ProductCategoryMidDTO> selectProductCategoryMidInfoByPage(
            Integer currentPage, Integer pageSize);
}
