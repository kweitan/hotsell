package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建时间 2020 - 01 -25
 *
 * @author kweitan
 */
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class CategoryIndexVO implements Serializable {

    private String categoryNumber ;

    private String categoryName ;

    private String categoryIcon ;

    private String categoryUrl ;

    private String hashNumber ;
}
