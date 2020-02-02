package com.sinjee.wechat.utils;

import com.sinjee.wechat.dto.BuyerInfoDTO;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author 小小极客
 * 时间 2020/2/2 22:04
 * @ClassName WechatAccessTokenUtil
 * 描述 WechatAccessTokenUtil
 **/
public class WechatAccessTokenUtil {

    @Value("${wechat.accessTokenExpTime}")
    private static String expireTime ;

    @Value("${wechat.accessToekenScret}")
    private static String tokenSecret ;

    public static String sign(BuyerInfoDTO user){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + expireTime);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("username", user.getUsername())
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(tokenSecret));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }
}
