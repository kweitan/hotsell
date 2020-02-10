package com.sinjee.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum{
    NEW("NEW", "新订单"),
    SHIPMENT("SHIPMENT","已发货"),
    FINISHED("FINISHED", "完结"),
    CANCEL("CANCEL", "已取消"),
    PARTIAL("PARTIAL", "部分付款"),
    REFUND("REFUND","申请退款"),
    ;

    private String code;

    private String message;

    OrderStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
