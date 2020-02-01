package com.sinjee.common;

import java.util.UUID;

/**
 * @author 小小极客
 * 时间 2020/2/1 22:14
 * @ClassName UUIDUtil
 * 描述 UUID生成器
 **/
public class UUIDUtil {

    public static String genUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
