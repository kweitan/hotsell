package com.sinjee.common;

import java.security.SecureRandom;

/**
 * @author 小小极客
 * 时间 2020/2/5 2:03
 * @ClassName KeyUtil
 * 描述 KeyUtil
 **/
public class KeyUtil {

    /**
     * 生成唯一的主键
     * 格式: 时间+随机数
     * @return
     */
    public static synchronized String genUniqueKey() {
        SecureRandom random = new SecureRandom();
        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
