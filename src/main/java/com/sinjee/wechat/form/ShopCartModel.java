package com.sinjee.wechat.form;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2020/2/4 11:38
 * @ClassName ShopCartModel
 * 描述 ShopCartModel
 **/
@Data
public class ShopCartModel implements Serializable {

    /** 商品编码 **/
    private String productNumber ;

    /** 商品哈希编码 **/
    private String hashNumber ;

    /** 商品数量 **/
    private String productCount ;

}
