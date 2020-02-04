package com.sinjee.wechat.form;

import lombok.Data;

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

    private List<ShopCartModel> shopCartList ;
}
