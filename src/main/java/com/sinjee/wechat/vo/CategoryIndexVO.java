package com.sinjee.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建时间 2020 - 01 -25
 *
 * @author kweitan
 */
@Data
public class CategoryIndexVO implements Serializable {

    private String categoryNumber ;

    private String categoryName ;

    private String categoryIcon ;

    private String categoryUrl ;

    private String hashNumber ;
}
