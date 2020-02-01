package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2020/2/1 22:42
 * @ClassName WechatTokenVO
 * 描述 WechatTokenVO
 **/
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class WechatTokenVO implements Serializable {

    private String token ;
}
