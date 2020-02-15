package com.sinjee.admin.service;

import com.sinjee.admin.dto.ProductCategoryDTO;
import com.sinjee.admin.dto.ProductDetailInfoDTO;

import java.util.List;

public interface ProductDetailInfoService {

    Integer save(ProductDetailInfoDTO productDetailInfoDTO);

    Integer update(ProductDetailInfoDTO productDetailInfoDTO);

    ProductDetailInfoDTO findDetailByProductNumber(String productNumber);

    List<ProductCategoryDTO> findCategoryInfoByProductNumber(String productNumber);
}
