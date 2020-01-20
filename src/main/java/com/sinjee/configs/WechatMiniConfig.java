package com.sinjee.configs;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${myWechat.miniAppId}")
    private String miniAppId ;

    @Value("${myWechat.miniAppSecret}")
    private String miniAppSecret ;

    @Bean
    public WxMaService wxMaService(){
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(wxMaDefaultConfig());
        return wxMaService;
    }

    @Bean
    public WxMaDefaultConfigImpl wxMaDefaultConfig(){
        WxMaDefaultConfigImpl wxMaDefaultConfig = new WxMaDefaultConfigImpl() ;
        wxMaDefaultConfig.setAppid(miniAppId);
        wxMaDefaultConfig.setSecret(miniAppSecret);
        return wxMaDefaultConfig ;
    }
}
