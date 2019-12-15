package com.sinjee.wechat.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.sinjee.wechat.entity.ProductInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

/**
 * 商品信息 DAO接口
 */
@Repository
public interface ProductInfoMapper {

    //保存商品信息
    int saveProductInfo(ProductInfoEntity productInfoEntity);

    //修改商品信息
    int updateProductInfo(ProductInfoEntity productInfoEntity);

    //根据商品ID查找商品
    ProductInfoEntity getProductInfoEntityById(Integer productId);

    //根据商品编号查找商品
    ProductInfoEntity getProductInfoEntityByProductNumber(String productNumber) ;

    //删除商品
    int deleteProductInfo(Integer productId) ;

    //下架商品
    int offProductInfo(Integer productId);

    //批量下架商品
    int batchOffProductInfos() ;

    //上架商品
    int onProductInfo(Integer productId);

    //批量上架商品
    int batchOnProductInfos() ;

    //根据status分页查找商品
    Page<ProductInfoEntity> selectProductInfosByPage(Pagination page, Integer status);
}
