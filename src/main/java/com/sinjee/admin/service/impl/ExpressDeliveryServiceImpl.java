package com.sinjee.admin.service.impl;

import com.sinjee.admin.mapper.ExpressDeliveryMapper;
import com.sinjee.admin.service.ExpressDeliveryService;
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
public class ExpressDeliveryServiceImpl implements ExpressDeliveryService {

    @Autowired
    private ExpressDeliveryMapper expressDeliveryMapper ;
}
