package com.sinjee.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.OrderMasterDTO;
import com.sinjee.admin.entity.OrderDetail;
import com.sinjee.admin.entity.OrderMaster;
import com.sinjee.admin.service.OrderMasterService;
import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.annotation.ApiIdempotency;
import com.sinjee.common.*;
import com.sinjee.enums.OrderStatusEnum;
import com.sinjee.enums.PayStatusEnum;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.form.ShopCartForm;
import com.sinjee.wechat.form.ShopCartModel;
import com.sinjee.wechat.vo.WechatOrderDetailVO;
import com.sinjee.wechat.vo.WechatOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 小小极客
 * 时间 2020/2/4 9:56
 * @ClassName WechatOrderController
 * 描述 WechatOrderController
 **/
@RestController
@Slf4j
@RequestMapping("wechat/order")
public class WechatOrderController {

    @Value("${myWechat.salt}")
    private String salt ;

    @Autowired
    private RedisUtil redisUtil ;

    @Autowired
    private OrderMasterService orderMasterService ;

    /***
     * 创建订单
     * @param request
     * @param shopCartForm
     * @return
     */
    @PostMapping("/createOrder")
    @AccessTokenIdempotency
    @ApiIdempotency
    public ResultVO create(HttpServletRequest request, @Valid ShopCartForm shopCartForm, BindingResult bindingResult){
        String openid = (String)request.getAttribute("openid") ;
        if (bindingResult.hasErrors()){
            log.error("创建订单不对");
            return ResultVOUtil.error(122,bindingResult.getFieldError().getDefaultMessage());
        }

        log.info("openid={}",openid);
        if (StringUtils.isBlank(openid)){
            return ResultVOUtil.error(121,"用户尚未授权");
        }

        log.info("shopCartForm={}", GsonUtil.getInstance().toStr(shopCartForm));

        List<ShopCartModel> shopCartModelList = null ;
        try {
            shopCartModelList = GsonUtil.getInstance().
                    parseString2List(shopCartForm.getShopCartList(),ShopCartModel.class);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultVOUtil.error(123,"购物车数据转换出错");
        }

        if (null != shopCartModelList && shopCartModelList.size()>0){
            for (ShopCartModel shopCartModel:shopCartModelList){
                if (!HashUtil.verify(shopCartModel.getProductNumber(),salt,shopCartModel.getHashNumber())){
                    return ResultVOUtil.error(121,"数据不一致");
                }
            }

            //取出用户信息
            Object object = redisUtil.getString(openid) ;
            if (null == object){
                return ResultVOUtil.error(121,"缓存已经过期") ;
            }
            BuyerInfoDTO buyerInfoDTO = (BuyerInfoDTO)object ;

            OrderMasterDTO orderMasterDTO = new OrderMasterDTO();
            CacheBeanCopier.copy(shopCartForm,orderMasterDTO);

            orderMasterDTO.setCreator(buyerInfoDTO.getBuyerName());
            orderMasterDTO.setCreateTime(DateUtils.getTimestamp());
            orderMasterDTO.setUpdater(buyerInfoDTO.getBuyerName());
            orderMasterDTO.setUpdateTime(DateUtils.getTimestamp());

            orderMasterDTO.setBuyerOpenid(openid);
            orderMasterDTO.setShopCartModelList(shopCartModelList);
            OrderMasterDTO orderMasterDTO1 = orderMasterService.save(orderMasterDTO) ;

            Map<String,Object> map = new HashMap<>() ;
            map.put("orderNumber",orderMasterDTO1.getOrderNumber());
            map.put("hashNumber",HashUtil.sign(orderMasterDTO1.getOrderNumber(),salt)) ;
            return ResultVOUtil.success(map) ;

        }else {
            return ResultVOUtil.error(121,"购物车无数据");
        }

    }

    @GetMapping("/getOrderByNumber")
    @AccessTokenIdempotency
    public ResultVO getOrderByNumber(HttpServletRequest request, String orderNumber, String hashNumber){
        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);

        if (!HashUtil.verify(orderNumber,salt,hashNumber)){
            return ResultVOUtil.error(121,"数据不一致");
        }

        OrderMasterDTO orderMasterDTO = orderMasterService.findByOrderNumberAndOpenid(orderNumber,openid) ;
        WechatOrderVO wechatOrderVO = new WechatOrderVO() ;
        CacheBeanCopier.copy(orderMasterDTO,wechatOrderVO);

        List<WechatOrderDetailVO> wechatOrderDetailVOList = BeanConversionUtils.
                copyToAnotherList(WechatOrderDetailVO.class,orderMasterDTO.getOrderDetailList()) ;

        wechatOrderVO.setOrderDetailList(wechatOrderDetailVOList);

        return ResultVOUtil.success(wechatOrderVO) ;
    }

    /**
     * 根据操作类型 订单处理 类型
     * @param request
     * @param type
     * @return
     */
    @GetMapping("/selectOrder")
    @AccessTokenIdempotency
    public ResultVO selectOrder(HttpServletRequest request, @RequestParam(value = "currentPage", defaultValue = "1")
            Integer currentPage,
                                @RequestParam(value = "pageSize", defaultValue = "6")
                                            Integer pageSize,
                                @RequestParam(value = "type", defaultValue = "")
                                            String type){

        //1.加载页数不超过20页
        if (currentPage > 20){
            currentPage = 20 ;
        }

        if(pageSize > 10){
            pageSize = 10 ;
        }

        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);

        IPage<OrderMasterDTO> orderMasterDTOIPage = orderMasterService.findByTpye(currentPage,pageSize,openid,type);

        //从分页中获取List
        List<OrderMasterDTO> orderMasterDTOList = orderMasterDTOIPage.getRecords() ;
        List<WechatOrderVO> wechatOrderVOList = new ArrayList<>() ;
        //遍历放到productInfoVOList中
        if(null != orderMasterDTOList && orderMasterDTOList.size()>0){
            orderMasterDTOList.stream().forEach(orderMasterDTO -> {
                //根据商品编码生成唯一HASH编码
                String hashNumber = HashUtil.sign(orderMasterDTO.getOrderNumber(),salt);
                WechatOrderVO wechatOrderVO = new WechatOrderVO() ;
                CacheBeanCopier.copy(orderMasterDTO,wechatOrderVO);
                wechatOrderVO.setHashNumber(hashNumber);
                List<OrderDetail> orderDetailList = orderMasterDTO.getOrderDetailList();
                if (null != orderDetailList && orderDetailList.size()>1){
                    wechatOrderVO.setHaveOne(false);
                }else {
                    wechatOrderVO.setHaveOne(true);
                }

                wechatOrderVO.setOrderDetailList(BeanConversionUtils
                        .copyToAnotherList(WechatOrderDetailVO.class,orderDetailList));
                wechatOrderVOList.add(wechatOrderVO) ;
            });
        }

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(wechatOrderVOList);
        resultVO.setCurrentPage(currentPage);
        resultVO.setTotalSize(orderMasterDTOIPage.getTotal());
        resultVO.setPageTotal(orderMasterDTOIPage.getPages());
        resultVO.setCode(0);
        resultVO.setMessage("成功");

        return resultVO ;
    }

    /**
     * 申请退款
     *  已经发货的申请退款 需要中台审核
     */
    @ResponseBody
    @PostMapping(value = "applyRefund")
    @AccessTokenIdempotency
    public ResultVO applyRefund(HttpServletRequest request, String orderNumber, String hashNumber){
        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);

        if (!HashUtil.verify(orderNumber,salt,hashNumber)){
            return ResultVOUtil.error(121,"数据不一致");
        }

        Integer res = orderMasterService.applyOrder(orderNumber,openid) ;

        if (res > 0){
            return ResultVOUtil.success();
        }

        return ResultVOUtil.error(121,"申请退款失败");
    }


    /**
     * 取消订单
     */
    //取消
    @ResponseBody
    @PostMapping(value = "cancel")
    @AccessTokenIdempotency
    public ResultVO cancel(HttpServletRequest request, String orderNumber, String hashNumber){
        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);

        if (!HashUtil.verify(orderNumber,salt,hashNumber)){
            return ResultVOUtil.error(121,"数据不一致");
        }

        Integer res = orderMasterService.cancelOrder(orderNumber,openid) ;

        if (res > 0){
            return ResultVOUtil.success();
        }

        return ResultVOUtil.error(121,"取消订单失败");
    }

    /**
     * 用户点击确认收货
     */
    @ResponseBody
    @PostMapping(value = "confirmReceipt")
    @AccessTokenIdempotency
    public ResultVO confirmReceipt(HttpServletRequest request, String orderNumber, String hashNumber){
        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);

        if (!HashUtil.verify(orderNumber,salt,hashNumber)){
            return ResultVOUtil.error(121,"数据不一致");
        }

        QueryWrapper<OrderMaster> wrapper = new QueryWrapper<>() ;
        wrapper.eq("buyer_openid",openid)
                .eq("enable_flag",1)
                .eq("order_status",OrderStatusEnum.SHIPMENT.getCode())
                .eq("pay_status", PayStatusEnum.SUCCESS.getCode())
        .eq("order_number",orderNumber);

        String orderStatus = OrderStatusEnum.FINISHED.getCode();
        String payStatus = PayStatusEnum.CLOSED.getCode() ;

        Integer res = orderMasterService.updataOrderStatus(wrapper,orderStatus,payStatus) ;

        if (res > 0){
            return ResultVOUtil.success();
        }

        return ResultVOUtil.error(121,"确认收货失败");
    }
}
