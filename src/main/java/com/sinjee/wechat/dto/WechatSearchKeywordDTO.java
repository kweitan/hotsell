package com.sinjee.wechat.dto;

import lombok.Data;

/**
 * 创建时间 2020 - 01 -29
 *
 * @author kweitan
 */
@Data
public class WechatSearchKeywordDTO extends WechatBaseDTO{

    private Integer searchKeywordId ;

    private String searchKeywordName ;

    private String searchKeywordNumber ;

    private Integer searchKeywordStatus ;
}
