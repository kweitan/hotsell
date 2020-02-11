package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sinjee.admin.dto.ProductDetailInfoDTO;
import com.sinjee.admin.entity.ProductDetailInfo;
import com.sinjee.admin.mapper.ProductDetailInfoMapper;
import com.sinjee.admin.service.ProductDetailInfoService;
import com.sinjee.common.CacheBeanCopier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Service
@Slf4j
public class ProductDetailInfoServiceImpl implements ProductDetailInfoService {

    @Autowired
    private ProductDetailInfoMapper productDetailInfoMapper;

    @Override
    public Integer save(ProductDetailInfoDTO productDetailInfoDTO) {
        ProductDetailInfo productDetailInfo = new ProductDetailInfo() ;
        CacheBeanCopier.copy(productDetailInfoDTO,productDetailInfo);
        return productDetailInfoMapper.insert(productDetailInfo);
    }

    @Override
    public Integer update(ProductDetailInfoDTO productDetailInfoDTO) {
        ProductDetailInfo productDetailInfo = new ProductDetailInfo() ;
        CacheBeanCopier.copy(productDetailInfoDTO,productDetailInfo);

        //ID 不更新
        productDetailInfo.setProductDetailId(null);
        //enableflag 不更新
        productDetailInfo.setEnableFlag(null);

        QueryWrapper<ProductDetailInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_number",productDetailInfo.getProductNumber());

        return productDetailInfoMapper.update(productDetailInfo,wrapper);
    }

    @Override
    public ProductDetailInfoDTO findDetailByProductNumber(String productNumber) {
        QueryWrapper<ProductDetailInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_number",productNumber);
        ProductDetailInfo productDetailInfo = productDetailInfoMapper.selectOne(wrapper) ;
        ProductDetailInfoDTO productDetailInfoDTO = new ProductDetailInfoDTO() ;
        CacheBeanCopier.copy(productDetailInfo,productDetailInfoDTO);
        return productDetailInfoDTO;
    }
}
