package com.sinjee.wechat.controller;

import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.annotation.ApiIdempotency;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.form.ShopCartForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return null ;
    }
}
