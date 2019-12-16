package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.ProductInfoDTO;


public interface ProductInfoService {
    //根据status分页查找商品
    IPage<ProductInfoDTO> selectProductInfosByPage(
            Integer currentPage,Integer pageSize,Integer productStatus);

    IPage<ProductInfoDTO> selectProductInfosByProductStatus(
            Integer currentPage,Integer pageSize,Integer productStatus);

    /**保存**/
    int saveProductInfo(ProductInfoDTO productInfoDTO);

    /****/
}
