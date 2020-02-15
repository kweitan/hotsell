package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sinjee.admin.dto.ProductCategoryDTO;
import com.sinjee.admin.dto.ProductDetailInfoDTO;
import com.sinjee.admin.entity.ProductCategory;
import com.sinjee.admin.entity.ProductCategoryMid;
import com.sinjee.admin.entity.ProductDetailInfo;
import com.sinjee.admin.mapper.ProductCategoryMapper;
import com.sinjee.admin.mapper.ProductCategoryMidMapper;
import com.sinjee.admin.mapper.ProductDetailInfoMapper;
import com.sinjee.admin.service.ProductDetailInfoService;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.exceptions.MyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Service
@Slf4j
public class ProductDetailInfoServiceImpl implements ProductDetailInfoService {


    @Value("${myWechat.salt}")
    private String salt ;

    @Autowired
    private ProductDetailInfoMapper productDetailInfoMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper ;

    @Autowired
    private ProductCategoryMidMapper midMapper ;

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

    @Override
    public List<ProductCategoryDTO> findCategoryInfoByProductNumber(String productNumber) {
        QueryWrapper<ProductCategoryMid> categoryMidQueryWrapper = new QueryWrapper();
        categoryMidQueryWrapper.eq("enable_flag",1).eq("product_number",productNumber);

        List<ProductCategoryMid> productCategoryMidList = midMapper.selectList(categoryMidQueryWrapper) ;
        List<ProductCategoryDTO> productCategoryDTOList = new ArrayList<>() ;
        if (null != productCategoryMidList && productCategoryMidList.size() > 0){
         for (ProductCategoryMid mid : productCategoryMidList)  {
             QueryWrapper<ProductCategory> categoryQueryWrapper = new QueryWrapper();
             categoryQueryWrapper.eq("enable_flag",1).eq("category_number",mid.getCategoryNumber());
             ProductCategory productCategory = productCategoryMapper.selectOne(categoryQueryWrapper);
             if (productCategory == null || StringUtils.isBlank(productCategory.getCategoryNumber())){
                 throw new MyException(101,"该类目已经不存在");
             }
             ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
             CacheBeanCopier.copy(productCategory,productCategoryDTO);
             productCategoryDTO.setHashNumber(HashUtil.sign(productCategory.getCategoryNumber(),salt));
             productCategoryDTOList.add(productCategoryDTO) ;
         }
        }

        return productCategoryDTOList;
    }
}
