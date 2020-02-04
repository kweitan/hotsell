package com.sinjee.wechat.controller;

import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.annotation.ApiIdempotency;
import com.sinjee.common.GsonUtil;
import com.sinjee.common.HashUtil;
import com.sinjee.common.RedisUtil;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.form.ShopCartForm;
import com.sinjee.wechat.form.ShopCartModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /***
     * 提交订单
     * @param openid
     * @param shopCartForm
     * @return
     */
    @GetMapping("/submitOrder")
    @AccessTokenIdempotency
    @ApiIdempotency
    public ResultVO submitOrder(String openid,@RequestBody ShopCartForm shopCartForm){

        //校验hashNumber productNumber
        log.info("openid={}",openid);
        log.info("shopCartForm={}", GsonUtil.getInstance().toStr(shopCartForm));
        List<ShopCartModel>shopCartModelList = shopCartForm.getShopCartList();
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

            //
            return null ;

        }else {
            return ResultVOUtil.error(121,"购物车无数据");
        }

    }
}
