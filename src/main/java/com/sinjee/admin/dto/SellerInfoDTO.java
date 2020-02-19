package com.sinjee.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 创建时间 2020 - 02 -19
 *
 * @author kweitan
 */
@Getter
@Setter
public class SellerInfoDTO extends BaseDTO implements Serializable {

    /****/
    private Integer sellerId ;

    /**卖家名称**/
    private String sellerName ;

    /****/
    private String sellerPassword ;

    /**卖家头像**/
    private String avatarUrl ;

    /**卖家国家**/
    private String sellerCountry ;

    /**卖家省份**/
    private String sellerProvince ;

    /**卖家城市**/
    private String sellerCity ;

    /**卖家性别 1-男 0-女**/
    private Integer sellerGender ;

    /**卖家编号**/
    private String sellerNumber ;

    /**卖家电话**/
    private String sellerPhone ;

    /**卖家qq**/
    private String sellerQq ;

    /**卖家邮箱**/
    private String sellerEmail ;

    /**卖家微信号**/
    private String sellerWechatId ;

    /**卖家支付宝**/
    private String sellerPayId ;


}
