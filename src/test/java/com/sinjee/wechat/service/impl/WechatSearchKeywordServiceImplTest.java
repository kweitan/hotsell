package com.sinjee.wechat.service.impl;

import com.sinjee.common.IdUtil;
import com.sinjee.wechat.dto.WechatSearchKeywordDTO;
import com.sinjee.wechat.service.WechatSearchKeywordService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class WechatSearchKeywordServiceImplTest {

    @Autowired
    private WechatSearchKeywordService wechatSearchKeywordService ;

    @Test
    void save() {
        WechatSearchKeywordDTO wechatSearchKeywordDTO = new WechatSearchKeywordDTO() ;
        wechatSearchKeywordDTO.setSearchKeywordName("无花果");
        wechatSearchKeywordDTO.setSearchKeywordNumber(IdUtil.genId());
        wechatSearchKeywordDTO.setSearchKeywordStatus(1);
        wechatSearchKeywordDTO.setCreator("kweitan");
        wechatSearchKeywordDTO.setUpdater("kweitan");
        wechatSearchKeywordDTO.setCreateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        wechatSearchKeywordDTO.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        wechatSearchKeywordService.save(wechatSearchKeywordDTO) ;
    }

    @Test
    void update() {
    }

    @Test
    void enableStatus() {
    }

    @Test
    void disableStatus() {
    }

    @Test
    void countKeyword() {
    }
}