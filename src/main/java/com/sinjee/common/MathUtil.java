package com.sinjee.common;

/**
 * @author 小小极客
 * 时间 2020/2/5 1:40
 * @ClassName MathUtil
 * 描述 MathUtil
 **/
public class MathUtil {
    private static final Double MONEY_RANGE = 0.01;

    /**
     * 比较2个金额是否相等
     * @param d1
     * @param d2
     * @return
     */
    public static Boolean equals(Double d1, Double d2) {
        Double result = Math.abs(d1 - d2);
        if (result < MONEY_RANGE) {
            return true;
        }else {
            return false;
        }
    }
}
