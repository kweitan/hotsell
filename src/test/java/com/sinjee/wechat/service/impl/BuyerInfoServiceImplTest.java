package com.sinjee.wechat.service.impl;

import com.sinjee.common.DateUtils;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.service.BuyerInfoService;
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
class BuyerInfoServiceImplTest {

    @Autowired
    private BuyerInfoService buyerInfoService ;

    @Test
    void save() {

//        BuyerInfoDTO buyerInfoDTO = new BuyerInfoDTO() ;
//        buyerInfoDTO.setOpenId("admin");
//        buyerInfoDTO.setBuyerName("kweitan-gx");
//        buyerInfoDTO.setSessionKey("admin");
//        buyerInfoDTO.setAvatarUrl("15914253470");
//        buyerInfoDTO.setBuyerPassword("admin");
//        buyerInfoDTO.setCreator("admin");
//        buyerInfoDTO.setUpdater("admin");
//
//        buyerInfoDTO.setCreateTime(DateUtils.getTimestamp());
//        buyerInfoDTO.setUpdateTime(DateUtils.getTimestamp());
//
//        buyerInfoService.save(buyerInfoDTO) ;
    }
}