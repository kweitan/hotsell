package com.sinjee.admin.service.impl;

import com.sinjee.admin.mapper.OrderMasterMapper;
import com.sinjee.admin.service.OrderMasterService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private OrderMasterMapper orderMasterMapper ;
}
