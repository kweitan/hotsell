package com.sinjee.wechat.service.impl;

import com.sinjee.wechat.mapper.BuyerReviewMapper;
import com.sinjee.wechat.service.BuyerReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Service
@Slf4j
public class BuyerReviewServiceImpl implements BuyerReviewService {

    @Autowired
    private BuyerReviewMapper buyerReviewMapper ;
}
