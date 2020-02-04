package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.admin.dto.OrderDetailDTO;
import com.sinjee.admin.dto.OrderMasterDTO;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.admin.entity.OrderDetail;
import com.sinjee.admin.entity.OrderMaster;
import com.sinjee.admin.entity.ProductInfo;
import com.sinjee.admin.mapper.OrderDetailMapper;
import com.sinjee.admin.mapper.OrderMasterMapper;
import com.sinjee.admin.service.OrderDetailService;
import com.sinjee.admin.service.OrderMasterService;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.DateUtils;
import com.sinjee.common.KeyUtil;
import com.sinjee.enums.OrderStatusEnum;
import com.sinjee.enums.PayStatusEnum;
import com.sinjee.exceptions.MyException;
import com.sinjee.wechat.form.ShopCartModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */

@Service
@Slf4j
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private OrderMasterMapper orderMasterMapper ;

    @Autowired
    private ProductInfoService productInfoService ;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public OrderMasterDTO save(OrderMasterDTO orderMasterDTO) {

        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        //查询商品（数量, 价格）
        for (ShopCartModel shopCartModel:orderMasterDTO.getShopCartModelList()){
            ProductInfoDTO productInfoDTO = productInfoService.findByNumber(shopCartModel.getProductNumber()) ;
            if (productInfoDTO == null || StringUtils.isBlank(productInfoDTO.getProductNumber())) {
                throw new MyException(255,"商品不存在");
            }

            //计算订单总价
            orderAmount = productInfoDTO.getProductPrice().multiply(new BigDecimal(shopCartModel.getProductCount())).add(orderAmount) ;

            //订单详情入库
            OrderDetail orderDetailDTO = new OrderDetail() ;
            orderDetailDTO.setOrderNumber(orderId);
            orderDetailDTO.setProductIcon(productInfoDTO.getProductIcon());
            orderDetailDTO.setProductLabels(productInfoDTO.getProductLabels());
            orderDetailDTO.setProductName(productInfoDTO.getProductName());
            orderDetailDTO.setProductPrice(productInfoDTO.getProductPrice());
            orderDetailDTO.setProductQuantity(shopCartModel.getProductCount());
            orderDetailDTO.setProductUnit(productInfoDTO.getProductUnit());
            orderDetailDTO.setCreator(orderMasterDTO.getBuyerOpenid());
            orderDetailDTO.setUpdater(orderMasterDTO.getBuyerOpenid());
            orderDetailDTO.setCreateTime(DateUtils.getTimestamp());
            orderDetailDTO.setUpdateTime(DateUtils.getTimestamp());

            orderDetailMapper.insert(orderDetailDTO) ;

        }

        //主订单写入订单数据库
        orderMasterDTO.setOrderNumber(orderId);

        OrderMaster orderMaster = new OrderMaster() ;
        CacheBeanCopier.copy(orderMasterDTO,orderMaster);

        orderMaster.setOrderNumber(orderId);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());

        orderMasterMapper.insert(orderMaster) ;

        //减库存
        productInfoService.decreaseStock(orderMasterDTO.getShopCartModelList());

        //发送websocket消息
//        webSocket.sendMessage(orderDTO.getOrderId());

        return orderMasterDTO ;
    }

    @Override
    public OrderMasterDTO findByOrderNumber(String orderNumber) {
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber).eq("enable_flag",1);
        OrderMaster orderMaster = orderMasterMapper.selectOne(wrapper) ;
        if (null == orderMaster || StringUtils.isBlank(orderMaster.getOrderNumber())){
            throw new MyException(257,"订单不存在");
        }
        OrderMasterDTO orderMasterDTO = new OrderMasterDTO() ;
        CacheBeanCopier.copy(orderMaster,orderMasterDTO);

        QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper();
        detailWrapper.eq("order_number",orderNumber).eq("enable_flag",1);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(detailWrapper) ;
        orderMasterDTO.setOrderDetailList(orderDetailList);
        return orderMasterDTO;
    }

    @Override
    public Integer update(OrderMasterDTO orderMasterDTO) {
        OrderMaster orderMaster = new OrderMaster() ;
        CacheBeanCopier.copy(orderMasterDTO,orderMaster);

        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderMasterDTO.getOrderNumber()).eq("enable_flag",1);
        return orderMasterMapper.update(orderMaster,wrapper);
    }

    @Override
    public Integer delete(String orderNumber) {
        OrderMaster orderMaster = new OrderMaster() ;
        orderMaster.setEnableFlag(0);
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber).eq("enable_flag",1);
        return orderMasterMapper.update(orderMaster,wrapper);
    }

    @Override
    public IPage<OrderMasterDTO> findByOpenId(Integer currentPage, Integer pageSize,String openId) {
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("buyer_openid",openId).eq("enable_flag",1);

        return returnPageByMaster(currentPage,pageSize,wrapper);
    }

    @Override
    public IPage<OrderMasterDTO> findByOpenIdAndOrderStatus(Integer currentPage, Integer pageSize,String openId, Integer orderStatus) {
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("buyer_openid",openId).eq("enable_flag",1).eq("order_status",orderStatus);

        return returnPageByMaster(currentPage,pageSize,wrapper);
    }

    @Override
    public IPage<OrderMasterDTO> findAllByPage(Integer currentPage, Integer pageSize, Integer orderStatus, String orderNumber) {
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber).eq("enable_flag",1).eq("order_status",orderStatus);
        return returnPageByMaster(currentPage,pageSize,wrapper);
    }

    @Override
    public Integer deleteByOpenId(String orderNumber, String openId) {
        OrderMaster orderMaster = new OrderMaster() ;
        orderMaster.setEnableFlag(0);
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber).eq("buyer_openid",openId).eq("enable_flag",1);
        return orderMasterMapper.update(orderMaster,wrapper);
    }

    private IPage<OrderMasterDTO> returnPageByMaster(Integer currentPage, Integer pageSize,QueryWrapper<OrderMaster> wrapper){
        Page<OrderMaster> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<OrderMaster> mapPage = orderMasterMapper.selectPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<OrderMaster> orderMasterList = mapPage.getRecords() ;
        List<OrderMasterDTO> orderMasterDTOList = BeanConversionUtils.copyToAnotherList(OrderMasterDTO.class,orderMasterList);

        Page<OrderMasterDTO> orderMasterDTOPage = new Page<>(currentPage,pageSize) ;
        orderMasterDTOPage.setPages(mapPage.getPages());
        orderMasterDTOPage.setTotal(mapPage.getTotal());
        orderMasterDTOPage.setRecords(orderMasterDTOList) ;

        return orderMasterDTOPage;
    }
}
