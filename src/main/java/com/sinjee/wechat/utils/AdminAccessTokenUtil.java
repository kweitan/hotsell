package com.sinjee.wechat.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sinjee.common.ConfigInfoUtil;
import com.sinjee.exceptions.MyException;
import com.sinjee.exceptions.SellerAuthorizeException;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 小小极客
 * 时间 2020/2/2 22:04
 * @ClassName WechatAccessTokenUtil
 * 描述 中台签名工具类
 **/
@Slf4j
public class AdminAccessTokenUtil {


    /**
     * 签名生成
     * @param sellerNumber
     * @return
     */
    public static String sign(String sellerNumber){

        String adminToken = null;
        log.info("adminExpireTime={}",ConfigInfoUtil.adminExpireTime);
        log.info("adminTokenSecret={}",ConfigInfoUtil.adminTokenSecret);
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + Long.valueOf(ConfigInfoUtil.adminExpireTime));
            adminToken = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("sellerNumber", sellerNumber)
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(ConfigInfoUtil.adminTokenSecret));
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return adminToken;
    }

    public static Map<String,Object> getMap(String adminToken){
        Map<String,Object> map = new HashMap<>() ;
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(ConfigInfoUtil.adminTokenSecret)).withIssuer("auth0").build();
            DecodedJWT jwt = jwtVerifier.verify(adminToken) ;
            map.put("issuer",jwt.getIssuer()) ;
            map.put("sellerNumber",jwt.getClaim("sellerNumber").asString());
            map.put("adminExpireTime",jwt.getExpiresAt()) ;

        }catch (Exception e){
            log.error(e.getMessage());
            log.info("token解析不正确!");
            throw new SellerAuthorizeException();
        }

        return map;
    }

}
