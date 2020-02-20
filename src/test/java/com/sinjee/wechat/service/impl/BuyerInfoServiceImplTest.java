package com.sinjee.wechat.service.impl;

import com.sinjee.admin.dto.SellerInfoDTO;
import com.sinjee.admin.entity.SellerInfo;
import com.sinjee.admin.mapper.SellerInfoMapper;
import com.sinjee.admin.service.SellerInfoService;
import com.sinjee.common.DateUtils;
import com.sinjee.common.IdUtil;
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

    @Autowired
    private SellerInfoMapper sellerInfoMapper;

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

//        SellerInfo sellerInfoDTO = new SellerInfo() ;
//        sellerInfoDTO.setSellerName("kweitan");
//        sellerInfoDTO.setSellerPassword("3oEfxVnkrbO4tlAjb7dHOw==");
//        sellerInfoDTO.setSellerNumber(IdUtil.genId());
//        sellerInfoDTO.setCreator("admin");
//        sellerInfoDTO.setUpdater("admin");
//
//        sellerInfoDTO.setCreateTime(DateUtils.getTimestamp());
//        sellerInfoDTO.setUpdateTime(DateUtils.getTimestamp());
//
//        sellerInfoMapper.insert(sellerInfoDTO) ;
    }
}