package com.sinjee.admin.service;

import com.sinjee.admin.dto.ProductDetailInfoDTO;

public interface ProductDetailInfoService {

    Integer save(ProductDetailInfoDTO productDetailInfoDTO);

    Integer update(ProductDetailInfoDTO productDetailInfoDTO);

    ProductDetailInfoDTO findDetailByProductNumber(String productNumber);
}
