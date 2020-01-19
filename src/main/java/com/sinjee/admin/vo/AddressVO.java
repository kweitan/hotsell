package com.sinjee.admin.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建时间 2020 - 01 -19
 *
 * @author kweitan
 */
@Data
public class AddressVO extends BaseVO implements Serializable {

    @JsonProperty("addressId")
    private Integer addressId;

    @JsonProperty("buyerName")
    private String buyerName;

    @JsonProperty("buyerPhone")
    private String buyerPhone;

    @JsonProperty("buyerAddress")
    private String buyerAddress;

    /** 是否选中 **/
    @JsonProperty("selectStatus")
    private Integer selectStatus;

    /** 地址标签 **/
    @JsonProperty("addressLabels")
    private String addressLabels;

    /** 哈希编码 **/
    @JsonProperty("goodHashNumber")
    private String hashNumber ;
}
