package com.sinjee.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.entity.ProductCategory;
import com.sinjee.wechat.entity.ProductInfo;
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
    IPage<ProductCategory> selectProductCategoryInfoBypage(
            IPage<ProductCategory> page);

}
