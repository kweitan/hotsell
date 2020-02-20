package com.sinjee.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES CBC 加密模式
 * @author kweitan
 */
@Slf4j
public class AESCBCUtil {

    private static final String KEY_ALGORITHM = "AES";

    /**
     * 算法/模组/补码
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"; // 默认的加密算法

    private static final String IV = "OSIDJF5934853940" ;

    private static final String CHARSET_NAME = "UTF-8" ;

    /**
     * AES 加密操作
     *
     * @param content     待加密内容
     * @param encryptPass 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String encryptPass) throws Exception{
        byte[] contentByte = content.getBytes(CHARSET_NAME) ;
        byte[] encryptPassByte = encryptPass.getBytes(CHARSET_NAME) ;

        SecretKeySpec secretKeySpec = new SecretKeySpec(encryptPassByte,KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

        IvParameterSpec ips = new IvParameterSpec(IV.getBytes()) ;
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ips);

        return Base64.encodeBase64String(cipher.doFinal(contentByte));
    }

    /**
     * AES 解密操作
     *
     * @param base64Content
     * @param encryptPass
     * @return
     */
    public static String decrypt(String base64Content, String encryptPass) throws Exception{
        byte[] contentByte = Base64.decodeBase64(base64Content);
        byte[] encryptPassByte = encryptPass.getBytes(CHARSET_NAME) ;

        SecretKeySpec secretKeySpec = new SecretKeySpec(encryptPassByte,KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

        IvParameterSpec ips = new IvParameterSpec(IV.getBytes()) ;
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ips);

        return new String(cipher.doFinal(contentByte)) ;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String getSecretKey(String encryptPass) throws NoSuchAlgorithmException {
        // 初始化密钥生成器，AES要求密钥长度为128位、192位、256位
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        kg.init(128, new SecureRandom(encryptPass.getBytes()));
        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM).toString();// 转换为AES专用密钥
    }

    public static void main(String[] args) {
        try {
            String content = "guixin";
            String salt = "4aW9Pnzx=Q80Xqib";
            String encoded = encrypt(content, salt);
            log.info("加密之前：{}", content);
            log.info("加密结果：{}", encoded);
            log.info("解密结果：{}", decrypt(encoded, salt));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
