package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.ProductReviewDTO;

public interface ProductReviewService {

    void save(ProductReviewDTO productReviewDTO) ;

    Integer update(ProductReviewDTO productReviewDTO) ;

    IPage<ProductReviewDTO> selectProductReviewByPage(Integer currentPage, Integer pageSize, String openid);

    Integer delete(Integer productReviewId) ;

    IPage<ProductReviewDTO> selectProductReviewByPageProductNumber(Integer currentPage, Integer pageSize, String productNumber);

    ProductReviewDTO selecOne(String productNumber) ;

    Integer productReviewCount(String productNumber) ;
}
