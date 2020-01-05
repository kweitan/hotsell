package com.sinjee.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 小小极客
 * 时间 2020/1/1 13:01
 * @ClassName WechatAccountConfig
 * 描述 WechatAccountConfig
 **/
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {

    /**
     * 设置微信小程序的appid
     */
    private String miniAppId ;

    /**
     * 设置微信小程序的Secret
     */
    private String miniAppSecret ;

    /**
     * 设置微信小程序消息服务器配置的token
     */
    private String token;

    /**
     * 设置微信小程序消息服务器配置的EncodingAESKey
     */
    private String aesKey;

    /**
     * 消息格式，XML或者JSON
     */
    private String msgDataFormat;

}
