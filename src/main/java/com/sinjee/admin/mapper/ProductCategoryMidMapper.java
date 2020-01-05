package com.sinjee.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.sinjee.admin.entity.ProductCategoryMid;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 类目中间表DAO
 */
@Repository
@Mapper
public interface ProductCategoryMidMapper extends BaseMapper<ProductCategoryMid> {
    /** 保存类目中间表**/
    Integer saveProductCategoryMidInfo(ProductCategoryMid productCategoryMid);

    /**更新类目中间表**/
    Integer updateProductCategoryMidInfo(ProductCategoryMid productCategoryMid);

    /**设置类目中间表无效**/
    Integer invalidProductCategoryMidInfo(Integer productCategoryMidId);

    /**分页查找类目中间表**/
    IPage<ProductCategoryMid> selectProductCategoryMidInfoBypage(
            IPage<ProductCategoryMid> page, @Param(Constants.WRAPPER) Wrapper<ProductCategoryMid> queryWrapper);

    /** 根据商品编码*/
}
