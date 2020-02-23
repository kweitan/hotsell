package com.sinjee.admin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建时间 2020 - 01 -26
 *
 * @author kweitan
 */
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class AdminBannerVO extends BaseVO implements Serializable {

    private Integer bannerId ;

    private String bannerUrl ;

    private String bannerName ;

    private String bannerIcon ;

    private Integer bannerWidth ;

    private Integer bannerHeight ;

    private String hashNumber ;

}
