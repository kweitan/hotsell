package com.sinjee.wechat.service.impl;

import com.sinjee.wechat.dto.WechatBannerDTO;
import com.sinjee.wechat.service.WechatBannerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class WechatBannerServiceImplTest {

    @Autowired
    private WechatBannerService wechatBannerService ;

    @Test
    void save() {
        WechatBannerDTO wechatBannerDTO = new WechatBannerDTO() ;
        wechatBannerDTO.setBannerHeight(280);
        wechatBannerDTO.setBannerWidth(700);
        wechatBannerDTO.setBannerName("导航栏");
        ///pages/images/top/01.jpg
        ///pages/images/top/02.jpg
        ///pages/images/top/03.jpg
        wechatBannerDTO.setBannerIcon("/pages/images/03.jpg");
        wechatBannerDTO.setBannerUrl("/page/index");
        wechatBannerDTO.setCreateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        wechatBannerDTO.setCreator("kweitan");
        wechatBannerDTO.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        wechatBannerDTO.setUpdater("kweitan");
        wechatBannerService.save(wechatBannerDTO) ;
    }

    @Test
    void update() {
    }

    @Test
    void getBannerIndexList() {
    }

    @Test
    void getBannerByPage() {
    }
}