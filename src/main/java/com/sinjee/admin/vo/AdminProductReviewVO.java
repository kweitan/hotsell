package com.sinjee.admin.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * 创建时间 2020 - 02 -24
 *
 * @author kweitan
 */
@Data
public class AdminProductReviewVO extends BaseVO implements Serializable {

    private Integer productReviewLevel;

    private String productReviewContent ;

    private String productNumber ;

    private String productName ;

    /**图像**/
    private String personIcon ;

    /**昵称**/
    private String personName ;

    private String openid ;

    private String orderNumber ;

    private String hashNumber ;
}
