package com.sinjee.wechat.form;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp ="^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    @Max(value = 11,message = "手机号只能为11位")
    @Min(value = 11,message = "手机号只能为11位")
    private String phone ;

    @NotEmpty(message="地址必填")
    private String addressInfo ;

    private String label ;

    @NotEmpty(message="类型必填")
    private String type ;

    private String number ;

    private String hashNumber ;

}
