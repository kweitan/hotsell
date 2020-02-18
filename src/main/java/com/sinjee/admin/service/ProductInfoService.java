package com.sinjee.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductDetailInfoDTO;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.wechat.form.ShopCartModel;

import java.util.List;

public interface ProductInfoService {
    //根据status分页查找商品
    IPage<ProductInfoDTO> selectProductInfosByPage(
            Integer currentPage,Integer pageSize,String selectName);

    IPage<ProductInfoDTO> selectProductInfosByProductStatus(
            Integer currentPage,Integer pageSize,Integer productStatus,String productName);

    /**保存**/
    Integer saveProductInfo(ProductInfoDTO productInfoDTO);

    /****/
    Integer save(ProductInfoDTO productInfoDTO, ProductDetailInfoDTO productDetailInfoDTO);


    Integer update(ProductInfoDTO productInfoDTO,ProductDetailInfoDTO productDetailInfoDTO);

    /***上架**/
    Integer upProductInfo(String productNumber);

    /***下架**/
    Integer downProductInfo(String productNumber);

    /**删除*/
    Integer deleteProductInfo(String productNumber);

    ProductInfoDTO findByNumber(String productNumber) ;

    IPage<ProductInfoDTO> selectProductInfosByCategoryNumber(Integer currentPage,Integer pageSize,String categoryNumber);

    List<ProductInfoDTO> getList() ;

    void increaseStock(List<ShopCartModel> shopCartModelList) ;

    void decreaseStock(List<ShopCartModel> shopCartModelList) ;

    Integer moveProductInfo(String productNumber,Integer type,Integer sequenceId);

    Integer findSequenceId();

}
