package com.sinjee.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 创建时间 2020 - 02 -03
 *
 * @author kweitan
 */
@Component
public class ConfigInfoUtil {

    public static String expireTime ;

    public static String tokenSecret ;

    @Value("${wechat.accessTokenExpTime}")
    public void setExpireTime(String expireTime){
        ConfigInfoUtil.expireTime = expireTime ;
    }

    @Value("${wechat.accessToekenScret}")
    public void setTokenSecret(String tokenSecret){
        ConfigInfoUtil.tokenSecret = tokenSecret ;
    }

}
