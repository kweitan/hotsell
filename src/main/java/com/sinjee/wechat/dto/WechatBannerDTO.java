package com.sinjee.wechat.dto;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -22
 *
 * @author kweitan
 */
@Data
public class WechatBannerDTO extends WechatBaseDTO{
    private Integer bannerId ;

    private String bannerUrl ;

    private String bannerName ;

    private String bannerIcon ;

    private String bannerWidth ;

    private String bannerHeight ;
}
