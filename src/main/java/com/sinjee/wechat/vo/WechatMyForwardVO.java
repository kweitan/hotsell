package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author 小小极客
 * 时间 2020/2/8 22:29
 * @ClassName WechatMyForwardVO
 * 描述 WechatMyForwardVO
 **/
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class WechatMyForwardVO implements Serializable {
    /**商品编码**/
    private String productNumber ;

    /**商品名称**/
    private String productName ;

    /**商品单价**/
    private BigDecimal productPrice ;

    /**商品简单秒速**/
    private String productDescription ;

    /**商品小图**/
    private String productIcon ;

    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp createTime ;

}
