package com.sinjee.wechat.vo;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -26
 *
 * @author kweitan
 */
@Data
public class WechatBannerVO {

    private Integer bannerId ;

    private String bannerUrl ;

    private String bannerName ;

    private String bannerIcon ;

    private Integer bannerWidth ;

    private Integer bannerHeight ;

    private String hashNumber ;
}
