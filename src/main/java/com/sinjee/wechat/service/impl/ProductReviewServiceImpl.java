package com.sinjee.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.exceptions.MyException;
import com.sinjee.wechat.dto.ProductReviewDTO;
import com.sinjee.wechat.entity.ProductReview;
import com.sinjee.wechat.form.WechatProductReviewForm;
import com.sinjee.wechat.mapper.ProductReviewMapper;
import com.sinjee.wechat.service.ProductReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 创建时间 2020 - 01 -29
 *
 * @author kweitan
 */
@Service
@Slf4j
public class ProductReviewServiceImpl implements ProductReviewService {

    @Autowired
    private ProductReviewMapper productReviewMapper ;

    @Override
    @Transactional
    public void save(ProductReviewDTO productReviewDTO) {

        List<WechatProductReviewForm> wechatProductReviewFormList = productReviewDTO.getWechatProductReviewFormList() ;

        try {
            wechatProductReviewFormList.stream().forEach(wechatProductReviewForm -> {
                ProductReview productReview = new ProductReview() ;
                CacheBeanCopier.copy(productReviewDTO,productReview);
                productReview.setProductReviewContent(wechatProductReviewForm.getProductReviewContent());
                productReview.setProductNumber(wechatProductReviewForm.getProductNumber());
                if (wechatProductReviewForm.getProductReviewLevel() == 999){
                    productReview.setProductReviewLevel(1);
                }else {
                    productReview.setProductReviewLevel(wechatProductReviewForm.getProductReviewLevel());
                }

                productReviewMapper.insert(productReview);
            });
        }catch (Exception e){
            log.error(e.getMessage());
            throw new MyException(121,"无商品数据") ;
        }

    }

    @Override
    @Transactional
    public Integer update(ProductReviewDTO productReviewDTO) {
        ProductReview productReview = new ProductReview() ;
        CacheBeanCopier.copy(productReviewDTO,productReview);

        QueryWrapper<ProductReview> wrapper = new QueryWrapper();
        wrapper.eq("product_review_id",productReviewDTO.getProductReviewId()).eq("enable_flag",1);
        return productReviewMapper.update(productReview,wrapper);
    }

    @Override
    public IPage<ProductReviewDTO> selectProductReviewByPage(Integer currentPage, Integer pageSize, String openid) {
        Page<ProductReview> page = new Page<>(currentPage,pageSize) ;

        QueryWrapper<ProductReview> wrapper = new QueryWrapper();
        wrapper.eq("openid",openid).eq("enable_flag",1);
        //从数据库分页获取数据
        IPage<ProductReview> mapPage = productReviewMapper.selectPage(page,wrapper);

        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<ProductReview> productReviewList = mapPage.getRecords() ;
        List<ProductReviewDTO> productReviewDTOList = BeanConversionUtils.copyToAnotherList(ProductReviewDTO.class,productReviewList);

        Page<ProductReviewDTO> productReviewDTOPage = new Page<>(currentPage,pageSize) ;
        productReviewDTOPage.setPages(mapPage.getPages());
        productReviewDTOPage.setTotal(mapPage.getTotal());
        productReviewDTOPage.setRecords(productReviewDTOList) ;
        return productReviewDTOPage ;
    }

    @Override
    public Integer delete(Integer productReviewId) {
        ProductReview productReview = new ProductReview() ;
        productReview.setEnableFlag(0);

        QueryWrapper<ProductReview> wrapper = new QueryWrapper();
        wrapper.eq("product_review_id",productReviewId).eq("enable_flag",1);
        return productReviewMapper.update(productReview,wrapper);
    }

    @Override
    public IPage<ProductReviewDTO> selectProductReviewByPageProductNumber(Integer currentPage, Integer pageSize, String productNumber) {
        Page<ProductReview> page = new Page<>(currentPage,pageSize) ;

        QueryWrapper<ProductReview> wrapper = new QueryWrapper();
        wrapper.eq("product_number",productNumber).eq("enable_flag",1);
        //从数据库分页获取数据
        IPage<ProductReview> mapPage = productReviewMapper.selectPage(page,wrapper);

        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<ProductReview> productReviewList = mapPage.getRecords() ;
        List<ProductReviewDTO> productReviewDTOList = BeanConversionUtils.copyToAnotherList(ProductReviewDTO.class,productReviewList);

        Page<ProductReviewDTO> productReviewDTOPage = new Page<>(currentPage,pageSize) ;
        productReviewDTOPage.setPages(mapPage.getPages());
        productReviewDTOPage.setTotal(mapPage.getTotal());
        productReviewDTOPage.setRecords(productReviewDTOList) ;
        return productReviewDTOPage ;
    }

    @Override
    public ProductReviewDTO selecOne(String productNumber) {
        QueryWrapper<ProductReview> wrapper = new QueryWrapper();
        wrapper.eq("product_number",productNumber)
                .eq("enable_flag",1)
                .eq("product_review_level",1);

        List<ProductReview> productReviewList = productReviewMapper.selectList(wrapper) ;
        if(null == productReviewList || productReviewList.size() < 1){
            return new ProductReviewDTO();
        }
        ProductReviewDTO productReviewDTO = new ProductReviewDTO() ;
        CacheBeanCopier.copy(productReviewList.get(0),productReviewDTO);

        return productReviewDTO;
    }

    @Override
    public Integer productReviewCount(String productNumber) {
        QueryWrapper<ProductReview> wrapper = new QueryWrapper();
        wrapper.eq("product_number",productNumber)
                .eq("enable_flag",1);
        return productReviewMapper.selectCount(wrapper);
    }

    @Override
    public List<ProductReviewDTO> productReviewDTOListByOrderNumber(String orderNumber) {
        QueryWrapper<ProductReview> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber)
                .eq("enable_flag",1);

        List<ProductReview> productReviewList = productReviewMapper.selectList(wrapper) ;
        if(null == productReviewList || productReviewList.size() < 1){
            return null;
        }

        return BeanConversionUtils.copyToAnotherList(ProductReviewDTO.class,productReviewList);
    }
}
