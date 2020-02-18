package com.sinjee.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.sinjee.admin.entity.ProductInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;


/**
 * 商品信息 DAO接口
 */
@Repository
@Mapper
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {

    //保存商品信息
    Integer saveProductInfo(ProductInfo productInfoEntity);

    //修改商品信息
    Integer updateProductInfo(ProductInfo productInfoEntity);

    //根据商品ID查找商品
    ProductInfo selectProductInfoEntityById(Integer productId);

    //根据商品编号查找商品
    ProductInfo selectProductByNumber(String productNumber) ;

    //删除商品
    Integer deleteProductInfo(Integer productId) ;

    //下架商品
    Integer offProductInfo(Integer productId);

    //批量下架商品
    Integer batchOffProductInfos() ;

    //上架商品
    Integer onProductInfo(Integer productId);

    //批量上架商品
    Integer batchOnProductInfos() ;

    //根据status分页查找商品
    IPage<ProductInfo> selectProductInfosByPage(
            IPage<ProductInfo> page, @Param(Constants.WRAPPER) Wrapper<ProductInfo> queryWrapper);


    //自定义分页
    IPage<ProductInfo> selectProductInfosByProductStatus(
            IPage<ProductInfo> page, @Param("status") Integer status);

    //根据status分页查找商品
    IPage<ProductInfo> selectProductInfosByCategoryNumber(
            IPage<ProductInfo> page, @Param("map") Map<String, Object> params);

    ProductInfo moveProductInfo(@Param("map") Map<String, Object> params);
}
