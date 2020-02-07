package com.sinjee.enums;

import lombok.Getter;

@Getter
public enum PayStatusEnum implements CodeEnum {

    WAIT("WAIT", "等待支付"),
    SUCCESS("SUCCESS", "支付成功"),
    REFUND("REFUND","转入退款"),
    NOTPAY("NOTPAY","未支付"),
    CLOSED("CLOSED","已关闭"),
    REVOKED("REVOKED","已撤销（刷卡支付）"),
    USERPAYING("USERPAYING","用户支付中"),
    PAYERROR("PAYERROR","支付失败(其他原因，如银行返回失败)")
    ;

    private String code;

    private String message;

    PayStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
