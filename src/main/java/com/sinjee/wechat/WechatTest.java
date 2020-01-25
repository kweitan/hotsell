package com.sinjee.wechat;

import java.security.SecureRandom;

/**
 * @author 小小极客
 * 时间 2020/1/5 12:29
 * @ClassName WechatTest
 * 描述 WechatTest
 **/
public class WechatTest {

    public static void main(String[] args){
        for (int i = 0;i < 99; i ++){
            int random = new SecureRandom().nextInt(5) ;
            System.out.println(random);
        }

    }
}
