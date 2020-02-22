package com.sinjee.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.OrderMasterDTO;
import com.sinjee.admin.dto.SellerInfoDTO;
import com.sinjee.admin.entity.ExpressDelivery;
import com.sinjee.admin.entity.OrderDetail;
import com.sinjee.admin.entity.OrderMaster;
import com.sinjee.admin.form.AdminOrderForm;
import com.sinjee.admin.form.ExpressDeliveryForm;
import com.sinjee.admin.service.OrderMasterService;
import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.*;
import com.sinjee.enums.OrderStatusEnum;
import com.sinjee.enums.PayStatusEnum;
import com.sinjee.exceptions.SellerAuthorizeException;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.vo.WechatOrderDetailVO;
import com.sinjee.wechat.vo.WechatOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/2/16 20:56
 * @ClassName AdminOrderController
 * 描述 中台订单管理
 **/
@RestController
@RequestMapping("/admin/order")
@Slf4j
public class AdminOrderController {

    @Value("${myWechat.salt}")
    private String salt ;

    @Autowired
    private OrderMasterService orderMasterService ;

    @Autowired
    private RedisUtil redisUtil ;

    /**
     * 根据操作类型 订单处理 类型
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/listAll")
    public ResultVO listAll(@RequestParam(value = "currentPage", defaultValue = "1")
            Integer currentPage,
                                @RequestParam(value = "pageSize", defaultValue = "10")
                                        Integer pageSize,
                                @RequestParam(value = "searchType", defaultValue = "ALL")
                                        String searchType,
                            @RequestParam(value = "orderNumber", defaultValue = "")
                                        String orderNumber){
        IPage<OrderMasterDTO> orderMasterDTOIPage = orderMasterService.findOrderAllList(currentPage,pageSize,searchType,orderNumber);

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
                wechatOrderVO.setBuyerId(orderMasterDTO.getBuyerOpenid());
                List<OrderDetail> orderDetailList = orderMasterDTO.getOrderDetailList();
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
     * 填写运单号
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/enterTrackingNumber")
    public ResultVO enterTrackingNumber(HttpServletRequest request,@RequestBody @Valid ExpressDeliveryForm expressDeliveryForm, BindingResult bindingResult){

        //获取卖家信息
        SellerInfoDTO sellerInfoDTO = Common.getSellerInfo(request,redisUtil) ;

        if (bindingResult.hasErrors()){
            return ResultVOUtil.error(101,bindingResult.getFieldError().getDefaultMessage()) ;
        }

        if (!HashUtil.verify(expressDeliveryForm.getOrderNumber(),salt,expressDeliveryForm.getHashNumber())){
            return ResultVOUtil.error(121,"数据不一致");
        }

        //生成运单号
//        String trackingNumber = KeyUtil.genUniqueKey() ;

        ExpressDelivery expressDelivery = new ExpressDelivery() ;
        expressDelivery.setExpressCorAbbreviation(expressDeliveryForm.getExpressCorAbbreviation());
        expressDelivery.setExpressCorName(expressDeliveryForm.getExpressCorName());
        expressDelivery.setTrackingNumber(expressDeliveryForm.getTrackingNumber());
        expressDelivery.setExpressNumber(KeyUtil.genUniqueKey());
        expressDelivery.setOrderNumber(expressDeliveryForm.getOrderNumber());

        Integer res = orderMasterService.enterTrackingNumber(sellerInfoDTO.getSellerName(),expressDeliveryForm.getOrderNumber(),expressDelivery) ;
        if (res > 0){
            ResultVOUtil.success() ;
        }

        return ResultVOUtil.error(101,"填写运单号失败") ;
    }

    /**
     * 查看订单详情
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/lookupOrderDetail")
    public ResultVO lookupOrderDetail(@RequestParam String orderNumber,
                                      @RequestParam String hashNumber){
        //校验
        if(!HashUtil.verify(orderNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"数据不一致!") ;
        }

        OrderMasterDTO orderMasterDTO = orderMasterService.lookupDetailByOrderNumber(orderNumber) ;
        WechatOrderVO wechatOrderVO = new WechatOrderVO() ;
        CacheBeanCopier.copy(orderMasterDTO,wechatOrderVO);

        List<WechatOrderDetailVO> wechatOrderDetailVOList = BeanConversionUtils.
                copyToAnotherList(WechatOrderDetailVO.class,orderMasterDTO.getOrderDetailList()) ;

        wechatOrderVO.setOrderDetailList(wechatOrderDetailVOList);

        return ResultVOUtil.success(wechatOrderVO) ;
    }

    /**
     * 修改订单支付金额
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/modifyActPayFee")
    public ResultVO modifyActPayFee(HttpServletRequest request, @RequestBody @Valid AdminOrderForm adminOrderForm, BindingResult bindingResult){
        //获取卖家信息
        SellerInfoDTO sellerInfoDTO = Common.getSellerInfo(request,redisUtil) ;

        if (bindingResult.hasErrors()){
            return ResultVOUtil.error(101,bindingResult.getFieldError().getDefaultMessage()) ;
        }

        if (!HashUtil.verify(adminOrderForm.getOrderNumber(),salt,adminOrderForm.getHashNumber())){
            return ResultVOUtil.error(121,"数据不一致");
        }

        //检验金额格式是否正确
        if (!(MathUtil.isFee(adminOrderForm.getActFee()) && MathUtil.isFee(adminOrderForm.getActPayFee()))){
            return ResultVOUtil.error(121,"金额格式不对");
        }

        OrderMasterDTO orderMasterDTO = orderMasterService.findByOrderNumber(adminOrderForm.getOrderNumber()) ;

        if (!("+".equals(adminOrderForm.getSymbol()) || "-".equals(adminOrderForm.getSymbol()))){
            return ResultVOUtil.error(121,"加减金额符号不对");
        }

        //金额对比
        BigDecimal totalFee = orderMasterDTO.getOrderAmount() ;
        if ("+".equals(adminOrderForm.getSymbol())){
            BigDecimal addFee = new BigDecimal(adminOrderForm.getActFee()).add(totalFee) ;
            BigDecimal fee = new BigDecimal(adminOrderForm.getActPayFee()) ;
            if(!MathUtil.equals(addFee.doubleValue(),fee.doubleValue())){
                return ResultVOUtil.error(121,"实际支付金额不对");
            }
        }

        if ("-".equals(adminOrderForm.getSymbol())){
            BigDecimal subFee = totalFee.subtract(new BigDecimal(adminOrderForm.getActFee())) ;
            BigDecimal fee = new BigDecimal(adminOrderForm.getActPayFee()) ;
            if(!MathUtil.equals(subFee.doubleValue(),fee.doubleValue())){
                return ResultVOUtil.error(121,"实际支付金额不对");
            }
        }

        Integer res = orderMasterService.modifyActFee(sellerInfoDTO.getSellerName(),adminOrderForm.getOrderNumber(),adminOrderForm.getActPayFee()) ;
        if (res > 0){
            ResultVOUtil.success() ;
        }

        return ResultVOUtil.error(101,"修改支付金额出错") ;

    }

}
