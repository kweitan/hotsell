package com.sinjee.wechat.entity;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -29
 *
 * @author kweitan
 */
@Data
public class WechatSearchKeyword extends WechatBaseEntity{

    private Integer searchKeywordId ;

    private String searchKeywordName ;

    private String searchKeywordNumber ;

    private Integer searchKeywordStatus ;
}
