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
import com.sinjee.common.*;
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
import java.util.ArrayList;
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
            orderDetailDTO.setProductQuantity(Integer.valueOf(shopCartModel.getProductCount()));
            orderDetailDTO.setProductUnit(productInfoDTO.getProductUnit());
            orderDetailDTO.setProductNumber(productInfoDTO.getProductNumber());
            orderDetailDTO.setCreator(orderMasterDTO.getBuyerName());
            orderDetailDTO.setUpdater(orderMasterDTO.getBuyerName());
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
    public OrderMasterDTO findByOrderNumberAndOpenid(String orderNumber,String openid) {
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber).eq("enable_flag",1).eq("buyer_openid",openid);
        OrderMaster orderMaster = orderMasterMapper.selectOne(wrapper) ;
        if (null == orderMaster || StringUtils.isBlank(orderMaster.getOrderNumber())){
            throw new MyException(257,"订单不存在");
        }
        OrderMasterDTO orderMasterDTO = new OrderMasterDTO() ;
        CacheBeanCopier.copy(orderMaster,orderMasterDTO);

        QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper();
        detailWrapper.eq("order_number",orderMaster.getOrderNumber()).eq("enable_flag",1);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(detailWrapper) ;
        orderMasterDTO.setOrderDetailList(orderDetailList);
        return orderMasterDTO;
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

        return orderMasterDTO;
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public OrderMasterDTO pay(OrderMasterDTO orderMasterDTO) {

        //判断订单状态
        if (!orderMasterDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("订单状态不正确, orderId={}, orderStatus={}",
                    orderMasterDTO.getOrderNumber(), orderMasterDTO.getOrderStatus());
            throw new MyException(257,"订单状态不正确");
        }

        //判断支付状态
        if (!orderMasterDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("订单支付状态不正确, orderDTO={}", GsonUtil.getInstance().toStr(orderMasterDTO));
            throw new MyException(257,"订单支付状态不正确");
        }


        //修改支付状态
        orderMasterDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster() ;
        CacheBeanCopier.copy(orderMasterDTO,orderMaster);

        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderMasterDTO.getOrderNumber()).
                eq("buyer_openid",orderMasterDTO.getBuyerOpenid()).eq("enable_flag",1);

        Integer res = orderMasterMapper.update(orderMaster,wrapper) ;
        if (null == res || !(res > 0)){
            log.error("修改支付状态失败");
            throw new MyException(257,"修改支付状态失败");
        }

        return orderMasterDTO ;
    }

    @Override
    @Transactional
    public Integer cancelOrder(String orderNumber, String openid) {
        QueryWrapper<OrderMaster> uWrapper = new QueryWrapper();
        uWrapper.eq("order_number",orderNumber).
                eq("buyer_openid",openid).eq("enable_flag",1)
        .eq("order_status","NEW");
        uWrapper.and(wrapper -> wrapper.eq("pay_status","WAIT").or().eq("pay_status","SUCCESS"));

        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setPayStatus(PayStatusEnum.CLOSED.getCode());
        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        orderMaster.setUpdateTime(DateUtils.getTimestamp());

        return orderMasterMapper.update(orderMaster,uWrapper);
    }

    @Override
    public Integer applyOrder(String orderNumber, String openid) {
        QueryWrapper<OrderMaster> uWrapper = new QueryWrapper();
        uWrapper.eq("order_number",orderNumber).
                eq("buyer_openid",openid).eq("enable_flag",1)
                .eq("order_status","NEW").eq("pay_status","SUCCESS");

        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setPayStatus(PayStatusEnum.REFUND.getCode());
        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        orderMaster.setUpdateTime(DateUtils.getTimestamp());

        return orderMasterMapper.update(orderMaster,uWrapper);
    }

    @Override
    public Integer updataOrderStatus(String orderNumber, String payStatus) {
    // 订单状态查询 待开发
        //todo
        if (payStatus.equals("SUCCESS")){

        }
        return null;
    }

    @Override
    public IPage<OrderMasterDTO> findByTpye(Integer currentPage, Integer pageSize, String openid, String type) {
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();

        if ("WAIT".equals(type)){
            //待支付(继续支付 取消订单)【继续支付】 【取消订单】
            wrapper.eq("buyer_openid",openid)
                    .eq("enable_flag",1)
                    .eq("order_status","NEW")
            .eq("pay_status","WAIT");
        }else if ("WAITSEND".equals(type)){
            //待发货【申请退款】【取消订单】
            wrapper.eq("buyer_openid",openid)
                    .eq("enable_flag",1)
                    .eq("order_status","NEW")
                    .eq("pay_status","SUCCESS");
        }else if ("SHIPMENT".equals(type)){
            //待收货(不能退款)【查看物流】 【催单】
            wrapper.eq("buyer_openid",openid)
                    .eq("enable_flag",1)
                    .eq("order_status","SHIPMENT")
                    .eq("pay_status","SUCCESS");
        }else if ("REVIEW".equals(type)){
            //已经完成(申请退款 继续评价 也就是待评价)【继续评价】 (orderStatus:NEW AND payStatus:SUCCESS || orderStatus:NEW AND payStatus:WAIT) 都可以取消订单
            wrapper.eq("buyer_openid",openid)
                    .eq("enable_flag",1)
                    .eq("order_status","FINISHED")
                    .eq("pay_status","CLOSED");
        }else if ("CANCEL".equals(type)){
            //已经取消(再来一单)(包括退款和取消)【再来一单)】
            wrapper.eq("buyer_openid",openid)
                    .eq("enable_flag",1)
                    .eq("order_status","CANCEL")
                    .eq("pay_status","CLOSED");
        }else{
            wrapper.eq("buyer_openid",openid)
                    .eq("enable_flag",1) ;
        }

        wrapper.orderByDesc("create_time") ; //降序

        Page<OrderMaster> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<OrderMaster> mapPage = orderMasterMapper.selectPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<OrderMaster> orderMasterList = mapPage.getRecords() ;

        List<OrderMasterDTO> orderMasterDTOList = new ArrayList<>() ;

        if (orderMasterList != null){
            orderMasterList.stream().forEach(orderMaster -> {
                OrderMasterDTO orderMasterDTO = new OrderMasterDTO();
                CacheBeanCopier.copy(orderMaster,orderMasterDTO);
                QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper();
                detailWrapper.eq("enable_flag",1)
                        .eq("order_number",orderMaster.getOrderNumber()) ;
                List<OrderDetail> orderDetailList = orderDetailMapper.selectList(detailWrapper) ;
                orderMasterDTO.setOrderDetailList(orderDetailList);
                orderMasterDTOList.add(orderMasterDTO) ;
            });
        }

        Page<OrderMasterDTO> orderMasterDTOPage = new Page<>(currentPage,pageSize) ;
        orderMasterDTOPage.setPages(mapPage.getPages());
        orderMasterDTOPage.setTotal(mapPage.getTotal());
        orderMasterDTOPage.setRecords(orderMasterDTOList) ;

        return orderMasterDTOPage;
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
