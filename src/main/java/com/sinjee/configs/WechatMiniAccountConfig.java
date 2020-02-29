package com.sinjee.configs;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author 小小极客
 * 时间 2020/2/4 22:33
 * @ClassName WechatMiniAccountConfig
 * 描述 WechatMiniAccountConfig
 **/
@Component
public class WechatMiniAccountConfig {

    @Value("${myWechat.miniAppId}")
    private String miniAppId ;

    @Value("${myWechat.mpAppId}")
    private String mpAppId ;

    @Value("${myWechat.miniAppSecret}")
    private String miniAppSecret ;

    /** 商户号 **/
    @Value("${myWechat.mchId}")
    private String mchId;

    /** 商户密钥 **/
    @Value("${myWechat.mchKey}")
    private String mchKey;

    /** 证书路劲 **/
    @Value("${myWechat.keyPath}")
    private String keyPath;

    /** 微信支付异步通知 **/
    @Value("${myWechat.notifyUrl}")
    private String notifyUrl;

    /** 交易类型 **/
    @Value("${myWechat.tradeType}")
    private String tradeType ;

    @Bean
    public WxPayService wxPayService(){
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(getWxPayConfig());
        return wxPayService ;
    }

    @Bean
    public WxPayConfig getWxPayConfig(){
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(mpAppId);
        payConfig.setMchId(mchId);
        payConfig.setMchKey(mchKey);
        payConfig.setKeyPath(keyPath);
        payConfig.setNotifyUrl(notifyUrl);
        payConfig.setTradeType(tradeType);
        return payConfig ;
    }
}
