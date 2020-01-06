package com.sinjee.admin.service.impl;

import com.sinjee.admin.mapper.ProductDetailInfoMapper;
import com.sinjee.admin.service.ProductDetailInfoService;
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
public class ProductDetailInfoServiceImpl implements ProductDetailInfoService {

    @Autowired
    private ProductDetailInfoMapper productDetailInfoMapper;
}
