package com.sinjee.enums;

import lombok.Getter;

@Getter
public enum PayStatusEnum implements CodeEnum {

    WAIT("WAIT", "等待支付"),
    SUCCESS("SUCCESS", "支付成功"),

    ;

    private String code;

    private String message;

    PayStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
