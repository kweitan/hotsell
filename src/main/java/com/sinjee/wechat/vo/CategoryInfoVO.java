package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建时间 2020 - 01 -06
 * 类目信息表
 * @author kweitan
 */
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class CategoryInfoVO implements Serializable {

    private Integer categoryId ;

    /** 类目名称 **/
    private String categoryName ;

    /** 类目编码 **/
    private String categoryNumber ;

}
