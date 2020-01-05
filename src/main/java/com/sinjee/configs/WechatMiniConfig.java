package com.sinjee.configs;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author 小小极客
 * 时间 2020/1/1 13:07
 * @ClassName WechatMiniConfig
 * 描述 WechatMiniConfig
 **/
@Component
public class WechatMiniConfig {

    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Bean
    public WxMaService wxMaService(){
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(wxMaDefaultConfig());
        return wxMaService;
    }

    @Bean
    public WxMaDefaultConfigImpl wxMaDefaultConfig(){
        WxMaDefaultConfigImpl wxMaDefaultConfig = new WxMaDefaultConfigImpl() ;
        wxMaDefaultConfig.setAppid(wechatAccountConfig.getMiniAppId());
        wxMaDefaultConfig.setSecret(wechatAccountConfig.getMiniAppSecret());
        return wxMaDefaultConfig ;
    }
}
