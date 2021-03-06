package com.sinjee.common;

public interface Constant {

    public interface Redis {
        String OK = "OK";
        Integer EXPIRE_TIME_MINUTE = 60;// 过期时间, 60s, 一分钟
        Integer EXPIRE_TIME_2MINUTE = 60 * 2;// 过期时间, , 2分钟
        Integer EXPIRE_TIME_HOUR = 60 * 60;// 过期时间, 一小时
        Integer EXPIRE_TIME_DAY = 60 * 60 * 24;// 过期时间, 一天
        Integer EXPIRE_TIME_7DAY = 60 * 60 * 24 * 7;// 过期时间, 7天
        Integer EXPIRE_TIME_28DAY = 60 * 60 * 24 * 28;// 过期时间, 7天
        String TOKEN_PREFIX = "token:";
        String MSG_CONSUMER_PREFIX = "consumer:";
        String ACCESS_LIMIT_PREFIX = "accessLimit:";
    }

    public interface LogType {
        Integer LOGIN = 1;// 登录
        Integer LOGOUT = 2;// 登出
    }

    /**
     * 订单流水常量
     */
    public interface OrderFlowStatus{
        String EMPTY = "EMPTY" ;
        String NEW = "NEW" ;
        String SUCCESS = "SUCCESS" ;
        String SHIPMENT = "SHIPMENT" ;
        String REVIEW = "REVIEW" ;
        String REFUND = "REFUND" ;
        String FINISHED = "FINISHED" ;
        String CANCEL = "CANCEL" ;
        String CLOSE = "CLOSE" ;

    }

}
