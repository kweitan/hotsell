package com.sinjee.wechat.controller;

import com.sinjee.admin.dto.OrderMasterDTO;
import com.sinjee.admin.service.OrderMasterService;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.annotation.ApiIdempotency;
import com.sinjee.common.*;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.form.ShopCartForm;
import com.sinjee.wechat.form.ShopCartModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    private ProductInfoService productInfoService ;

    @Autowired
    private OrderMasterService orderMasterService ;

    /***
     * 创建订单
     * @param openid
     * @param shopCartForm
     * @return
     */
    @GetMapping("/createOrder")
    @AccessTokenIdempotency
    @ApiIdempotency
    public ResultVO create(String openid, @Valid ShopCartForm shopCartForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("创建订单不对");
            return ResultVOUtil.error(122,bindingResult.getFieldError().getDefaultMessage());
        }

        //校验hashNumber productNumber
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
            BuyerInfoDTO buyerInfoDTO = (BuyerInfoDTO)redisUtil.getString(openid);
            if (null == buyerInfoDTO || StringUtils.isBlank(buyerInfoDTO.getOpenId())){
                return ResultVOUtil.error(121,"用户尚未授权");
            }

            OrderMasterDTO orderMasterDTO = new OrderMasterDTO();
            CacheBeanCopier.copy(shopCartForm,orderMasterDTO);

            orderMasterDTO.setBuyerOpenid(openid);
            orderMasterDTO.setShopCartModelList(shopCartModelList);
            OrderMasterDTO orderMasterDTO1 = orderMasterService.save(orderMasterDTO) ;

            Map<String,Object> map = new HashMap<>() ;
            map.put("orderNumber",orderMasterDTO1.getOrderNumber());
            return ResultVOUtil.success(map) ;

        }else {
            return ResultVOUtil.error(121,"购物车无数据");
        }

    }
}
