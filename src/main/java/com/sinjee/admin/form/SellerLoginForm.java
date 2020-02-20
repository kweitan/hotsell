package com.sinjee.admin.form;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2020/2/20 22:15
 * @ClassName SellerLoginForm
 * 描述 SellerLoginForm
 **/
@Getter
@Setter
public class SellerLoginForm implements Serializable {

    private String username;

    private String password;

    private String verifyCode;
}
