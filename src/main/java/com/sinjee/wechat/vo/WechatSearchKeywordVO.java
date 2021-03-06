package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建时间 2020 - 01 -29
 *
 * @author kweitan
 */
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class WechatSearchKeywordVO implements Serializable {
    private String searchKeywordName ;

    private String searchKeywordNumber ;

}
