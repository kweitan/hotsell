package com.sinjee.wechat.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.ProductCategoryDTO;
import com.sinjee.wechat.mapper.ProductCategoryMapper;
import com.sinjee.wechat.service.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 小小极客
 * 时间 2019/12/17 0:06
 * @ClassName ProductCategoryImpl
 * 描述 商品类目服务实现类
 **/
@Service
public class ProductCategoryImpl implements ProductCategory {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public Integer saveProductCategoryInfo(ProductCategoryDTO productCategory) {
        return null;
    }

    @Override
    public Integer updateProductCategoryInfo(ProductCategoryDTO productCategory) {
        return null;
    }

    @Override
    public Integer invalidProductCategoryInfo(String categoryNumber) {
        return null;
    }

    @Override
    public IPage<ProductCategoryDTO> selectProductCategoryInfoBypage(IPage<ProductCategoryDTO> page) {
        return null;
    }
}
