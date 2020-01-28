package com.sinjee.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建时间 2020 - 01 -26
 *
 * @author kweitan
 */
@Data
public class WechatBannerVO implements Serializable {

    private Integer bannerId ;

    private String bannerUrl ;

    private String bannerName ;

    private String bannerIcon ;

    private Integer bannerWidth ;

    private Integer bannerHeight ;

    private String hashNumber ;
}
