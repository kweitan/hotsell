package com.sinjee.wechat.entity;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -22
 *
 * @author kweitan
 */
@Data
public class WechatBanner extends WechatBaseEntity{

    private Integer bannerId ;

    private String bannerUrl ;

    private String bannerName ;

    private String bannerIcon ;

    private String bannerWidth ;

    private String bannerHeight ;

}
