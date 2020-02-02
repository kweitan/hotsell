package com.sinjee.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * 单向散列算法(指纹算法，以防止篡改)
 * 不推荐使用(MD5 SHA1)
 * 推荐使用SHA256
 */
@Slf4j
public class HashUtil {

    private final static String CHARSET = "UTF-8" ;


    public static String signByMD5(String text, String key) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, CHARSET));
    }

    public static boolean verifyByMD5(String text, String key,String sign) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, CHARSET));
        if(mysign.equals(sign)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 签名字符
     * @param text 签名的字符串
     * @param key 密钥
     * @return 签名结果
     */
    public static String sign(String text, String key) {
        text = text + key;
        return DigestUtils.sha256Hex(getContentBytes(text, CHARSET));
    }



    /**
     * 签名的字符串
     * @param text
     * @param charset
     * @return
     * @throws Exception
     */
    public static String signKey(String text,String charset){
        String result = null ;
        try {
            result = Base64.encodeBase64String(DigestUtils.sha256(text.getBytes(charset)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result ;
    }

    /**
     * 签名字符
     * @param text 签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @return 签名结果
     */
    public static boolean verify(String text, String key,String sign) {
        text = text + key;
        String mysign = DigestUtils.sha256Hex(getContentBytes(text, CHARSET));
        if(mysign.equals(sign)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("SHA256签名过程中出现错 指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    public static void main(String[] args) {
        String text = "Hello SHA256!" ;
        String key = "are you ok" ;
        //签名
    }
}
