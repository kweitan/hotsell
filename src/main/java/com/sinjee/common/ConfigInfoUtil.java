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

    //小程序 过期时间
    public static String expireTime ;

    //小程序 密钥
    public static String tokenSecret ;

    //中台 过期时间
    public static String adminExpireTime ;

    //中台 密钥
    public static String adminTokenSecret ;

    @Value("${wechat.accessTokenExpTime}")
    public void setExpireTime(String expireTime){
        ConfigInfoUtil.expireTime = expireTime ;
    }

    @Value("${wechat.accessToekenScret}")
    public void setTokenSecret(String tokenSecret){
        ConfigInfoUtil.tokenSecret = tokenSecret ;
    }


    @Value("${admin.adminTokenExpTime}")
    public void setAdminExpireTime(String adminExpireTime){
        ConfigInfoUtil.adminExpireTime = adminExpireTime ;
    }

    @Value("${admin.adminTokenScret}")
    public void setAdminTokenSecret(String adminTokenSecret){
        ConfigInfoUtil.adminTokenSecret = adminTokenSecret ;
    }

}
