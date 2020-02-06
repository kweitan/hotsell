package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2020/2/4 11:21
 * @ClassName WechatAddressVO
 * 描述 WechatAddressVO
 **/
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class WechatAddressVO implements Serializable {

    @JsonProperty("name")
    private String buyerName;

    @JsonProperty("phone")
    private String buyerPhone;

    @JsonProperty("addressInfo")
    private String buyerAddress;

    /** 是否选中 **/
    private Integer selectStatus;

    /** 地址标签 **/
    @JsonProperty("label")
    private String addressLabels;
}
