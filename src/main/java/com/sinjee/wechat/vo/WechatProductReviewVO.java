package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 创建时间 2020 - 01 -30
 *
 * @author kweitan
 */
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class WechatProductReviewVO {

    private Integer productReviewId ;

    private Integer productReviewLevel;

    private String productReviewContent ;

    private Integer buyerReviewId ;

    private String productNumber;

    /**图像**/
    private String personIcon ;

    /**昵称**/
    private String personName ;

    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp updateTime ;
}
