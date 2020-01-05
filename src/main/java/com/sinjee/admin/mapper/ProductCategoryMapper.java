package com.sinjee.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.sinjee.admin.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/***
 * 商品类目DAO接口
 */
@Repository
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
    /** 保存类目信息**/
    Integer saveProductCategoryInfo(ProductCategory productCategory);

    /**更新类目信息**/
    Integer updateProductCategoryInfo(ProductCategory productCategory);

    /**设置类目信息无效**/
    Integer invalidProductCategoryInfo(String categoryNumber);

    /**分页查找类目信息**/
    IPage<ProductCategory> selectProductCategoryInfoByPage(IPage<ProductCategory> page, @Param(Constants.WRAPPER) Wrapper<ProductCategory> queryWrapper);

}
