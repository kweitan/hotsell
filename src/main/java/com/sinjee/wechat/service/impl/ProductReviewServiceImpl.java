package com.sinjee.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.admin.entity.OrderMaster;
import com.sinjee.admin.mapper.OrderMasterMapper;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.Common;
import com.sinjee.common.Constant;
import com.sinjee.enums.OrderStatusEnum;
import com.sinjee.enums.PayStatusEnum;
import com.sinjee.exceptions.MyException;
import com.sinjee.wechat.dto.ProductReviewDTO;
import com.sinjee.wechat.entity.OrderFlow;
import com.sinjee.wechat.entity.ProductReview;
import com.sinjee.wechat.form.WechatProductReviewForm;
import com.sinjee.wechat.mapper.OrderFlowMapper;
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

    @Autowired
    private OrderMasterMapper orderMasterMapper ;

    @Autowired
    private OrderFlowMapper orderFlowMapper ;

    @Override
    @Transactional
    public void save(String buyerName,ProductReviewDTO productReviewDTO) {

        //保存订单流水
        OrderFlow orderFlow = Common.getOrderFlow(productReviewDTO.getOrderNumber(),buyerName,
                "用户评价商品",Constant.OrderFlowStatus.FINISHED,Constant.OrderFlowStatus.REVIEW) ;
        orderFlowMapper.insert(orderFlow) ;

        /**
         * 填写运单的时候 设置订单状态为SHIPMENT 待收货
         * 客户签收的时候 设置订单状态为REVIEW 待评价
         * 客户评价后 设置订单状态为FINISHED
         */
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",productReviewDTO.getOrderNumber()).eq("enable_flag",1)
        .eq("order_status", OrderStatusEnum.REVIEW.getCode())
        .eq("pay_status", PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster() ;
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderMaster.setPayStatus(PayStatusEnum.CLOSED.getCode());
        orderMasterMapper.update(orderMaster,wrapper) ;

        List<WechatProductReviewForm> wechatProductReviewFormList = productReviewDTO.getWechatProductReviewFormList() ;

        try {
            wechatProductReviewFormList.stream().forEach(wechatProductReviewForm -> {
                ProductReview productReview = new ProductReview() ;
                CacheBeanCopier.copy(productReviewDTO,productReview);
                productReview.setProductReviewContent(wechatProductReviewForm.getProductReviewContent());
                productReview.setProductNumber(wechatProductReviewForm.getProductNumber());
                productReview.setProductName(wechatProductReviewForm.getProductName());

                /** 这里的99 是微信小程序默认情况下 不选中差评 好评的默认值**/
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

    @Override
    public IPage<ProductReviewDTO> findListByPage(Integer currentPage, Integer pageSize, String productName, String personName) {
        Page<ProductReview> page = new Page<>(currentPage,pageSize) ;

        QueryWrapper<ProductReview> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).like("product_name",productName).like("person_name",personName);
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
    public Integer deleteProductReview(String productNumber) {
        QueryWrapper<ProductReview> wrapper = new QueryWrapper();
        wrapper.eq("product_number",productNumber)
                .eq("enable_flag",1);
        ProductReview productReview = new ProductReview() ;
        productReview.setEnableFlag(0);
        return productReviewMapper.update(productReview,wrapper);
    }

    @Override
    public Integer modifyProductReview(String productNumber,String reviewContent,Integer reviewLevel) {
        QueryWrapper<ProductReview> wrapper = new QueryWrapper();
        wrapper.eq("product_number",productNumber)
                .eq("enable_flag",1);
        ProductReview productReview = new ProductReview() ;
        productReview.setProductReviewContent(reviewContent);
        productReview.setProductReviewLevel(reviewLevel);
        return productReviewMapper.update(productReview,wrapper);
    }
}
