package com.sinjee.wechat;

import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

import java.security.SecureRandom;

/**
 * @author 小小极客
 * 时间 2020/1/5 12:29
 * @ClassName WechatTest
 * 描述 WechatTest
 **/
public class WechatTest {

    public static void main(String[] args){

        WxPayService wxPayService = new WxPayServiceImpl();
//        wxPayService.createOrder()

        for (int i = 0;i < 99; i ++){
            int random = new SecureRandom().nextInt(5) ;
            System.out.println(random);
        }

    }
}
