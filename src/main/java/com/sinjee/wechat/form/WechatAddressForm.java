package com.sinjee.wechat.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2020/2/5 15:43
 * @ClassName WechatAddressForm
 * 描述 WechatAddressForm
 **/
@Data
public class WechatAddressForm implements Serializable {

    @NotEmpty(message="姓名必填")
    private String name ;

    @NotEmpty(message="电话必填")
    private String phone ;

    @NotEmpty(message="地址必填")
    private String addressInfo ;

    @NotEmpty(message="标签必选")
    private String label ;

    @NotEmpty(message="类型必填")
    private String type ;

    @NotEmpty(message="编码不能为空")
    private String number ;

    @NotEmpty(message="哈希不能为空")
    private String hashNumber ;

}
