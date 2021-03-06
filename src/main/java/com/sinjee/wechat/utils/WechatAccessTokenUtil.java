package com.sinjee.wechat.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sinjee.common.ConfigInfoUtil;
import com.sinjee.exceptions.MyException;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 小小极客
 * 时间 2020/2/2 22:04
 * @ClassName WechatAccessTokenUtil
 * 描述 WechatAccessTokenUtil
 **/
@Slf4j
public class WechatAccessTokenUtil {


    /**
     * 签名生成
     * @param openid
     * @return
     */
    public static String sign(String openid){

        String accessToken = null;
        log.info("expireTime={}",ConfigInfoUtil.expireTime);
        log.info("tokenSecret={}",ConfigInfoUtil.tokenSecret);
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + Long.valueOf(ConfigInfoUtil.expireTime));
            accessToken = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("openid", openid)
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(ConfigInfoUtil.tokenSecret));
        } catch (Exception e){
            e.printStackTrace();
        }
        return accessToken;
    }

    public static Map<String,Object> getMap(String accessToken){
        Map<String,Object> map = new HashMap<>() ;
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(ConfigInfoUtil.tokenSecret)).withIssuer("auth0").build();
            DecodedJWT jwt = jwtVerifier.verify(accessToken) ;
            map.put("issuer",jwt.getIssuer()) ;
            map.put("openid",jwt.getClaim("openid").asString());
            map.put("expiresTime",jwt.getExpiresAt()) ;

        }catch (Exception e){
            throw new MyException(202,"token解析不正确!");
        }

        return map;
    }

    public static boolean verify(String accessToken){
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(ConfigInfoUtil.tokenSecret)).withIssuer("auth0").build();
            DecodedJWT jwt = jwtVerifier.verify(accessToken) ;
            //1.取出issuer
            jwt.getIssuer();
            //2.取出openid
            jwt.getClaim("openid");
            //3.取出过期时间
            jwt.getExpiresAt();

            /***
             *
             * SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             * Date date = new Date();
             * date = s.parse("2007-06-06 15:15:00");
             * Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
             * calendar.setTime(date);
             * long time = calendar.getTimeInMillis() / 1000;
             * **/

            //获取当前时间毫秒
            System.currentTimeMillis();
        }catch (Exception e){
            log.error(e.getMessage());
            return false ;
        }

        return true;
    }
}
