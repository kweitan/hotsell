package com.sinjee.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2019/12/15 12:08
 * @ClassName ResultVO
 * 描述 请求返回的最外层对象
 **/
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ResultVO<T> implements Serializable {
    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String message;

    /** 当前页 **/
    private Integer currentPage ;

    /** 总记录 **/
    private Long totalSize ;

    /** 总页数*/
    private Long pageTotal ;

    /** 具体内容. */
    private T data;
}
