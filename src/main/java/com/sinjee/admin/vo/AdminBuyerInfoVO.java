package com.sinjee.admin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2020/2/22 10:25
 * @ClassName AdminBuyerInfoVO
 * 描述 AdminBuyerInfoVO
 **/
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class AdminBuyerInfoVO extends BaseVO implements Serializable {

    private String buyerName ;

    private String buyerPassword;

    private String avatarUrl;

    private String openId ;

    private String buyerCountry;

    private String buyerProvince;

    private String buyerCity;

    private Integer buyerGender;

    private String unionId ;

    private String sessionKey ;

    private String selectName ;

    private String hashNumber ;

}
