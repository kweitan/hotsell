package com.sinjee.wechat.service.impl;

import com.sinjee.wechat.mapper.BuyerInfoMapper;
import com.sinjee.wechat.service.BuyerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建时间 2020 - 01 -06
 * 买家信息
 * @author kweitan
 */
@Service
@Slf4j
public class BuyerInfoServiceImpl implements BuyerInfoService {

    @Autowired
    private BuyerInfoMapper buyerInfoMapper;
}
