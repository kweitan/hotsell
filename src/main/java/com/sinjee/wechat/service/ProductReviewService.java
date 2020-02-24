package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.ProductReviewDTO;

import java.util.List;

public interface ProductReviewService {

    void save(String buyerName,ProductReviewDTO productReviewDTO) ;

    Integer update(ProductReviewDTO productReviewDTO) ;

    IPage<ProductReviewDTO> selectProductReviewByPage(Integer currentPage, Integer pageSize, String openid);

    Integer delete(Integer productReviewId) ;

    IPage<ProductReviewDTO> selectProductReviewByPageProductNumber(Integer currentPage, Integer pageSize, String productNumber);

    ProductReviewDTO selecOne(String productNumber) ;

    Integer productReviewCount(String productNumber) ;

    List<ProductReviewDTO> productReviewDTOListByOrderNumber(String orderNumber);

    /** 中台 返回产品评论**/
    IPage<ProductReviewDTO> findListByPage(Integer currentPage, Integer pageSize, String productName,String personName);

    /** 中台删除评论**/
    Integer deleteProductReview(String productNumber) ;

    /** 中台修改评论**/
    Integer modifyProductReview(String productNumber,String reviewContent,Integer reviewLevel) ;
}
