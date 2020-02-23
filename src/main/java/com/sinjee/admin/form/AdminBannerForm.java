package com.sinjee.admin.form;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author 小小极客
 * 时间 2020/2/23 9:03
 * @ClassName AdminBannerForm
 * 描述 AdminBannerForm
 **/
@Slf4j
@Getter
@Setter
public class AdminBannerForm implements Serializable {

    private Integer bannerId ;

    @NotBlank(message = "广告栏连接地址不能为空")
    private String bannerUrl ;

    @NotBlank(message = "广告栏名称不能为空")
    private String bannerName ;

    @NotBlank(message = "广告栏导航图不能为空")
    private String bannerIcon ;


    private String hashNumber ;
}
