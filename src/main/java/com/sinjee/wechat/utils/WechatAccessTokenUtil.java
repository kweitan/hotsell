package com.sinjee.wechat.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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

    /**
     * 签名生成
     * @param user
     * @return
     */
    public static String sign(BuyerInfoDTO user){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + expireTime);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("openid", user.getOpenId())
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(tokenSecret));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    public static boolean verify(String token){
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(tokenSecret)).withIssuer("auth0").build();
            DecodedJWT jwt = jwtVerifier.verify(token) ;
            //1.取出issuer
            jwt.getIssuer();
            //2.取出openid
            jwt.getClaim("openid");
            //3.取出过期时间
            jwt.getExpiresAt();
        }catch (Exception e){

        }

        return true;
    }
}
