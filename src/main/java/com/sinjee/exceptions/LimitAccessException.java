package com.sinjee.exceptions;

/**
 * @author 小小极客
 * 时间 2020/2/22 10:58
 * @ClassName LimitAccessException
 * 描述 频繁访问异常
 **/
public class LimitAccessException extends RuntimeException{
    public LimitAccessException(String message){
        super(message);
    }
}
