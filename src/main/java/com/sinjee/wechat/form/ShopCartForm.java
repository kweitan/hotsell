package com.sinjee.wechat.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/2/4 11:37
 * @ClassName ShopCartForm
 * 描述 ShopCartForm
 **/
@Data
public class ShopCartForm implements Serializable {

    /** 买家名字 **/
    @NotEmpty(message="姓名必填")
    private String buyerName ;

    /** 买家电话 **/
    @NotEmpty(message="电话必填")
    private String buyerPhone ;

    /** 买家地址**/
    @NotEmpty(message="地址必填")
    private String buyerAddress ;

    @NotEmpty(message="购物车不能为空")
    private String shopCartList ;
}
