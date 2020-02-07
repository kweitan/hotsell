package com.sinjee.common;

import com.sinjee.enums.CodeEnum;

/**
 * @author 小小极客
 * 时间 2020/2/7 22:05
 * @ClassName EnumUtil
 * 描述 EnumUtil
 **/
public class EnumUtil {
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
