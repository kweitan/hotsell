package com.sinjee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sinjee.admin.entity.OrderMaster;
import com.sinjee.admin.mapper.OrderMasterMapper;
import com.sinjee.admin.service.OrderMasterService;
import com.sinjee.common.DateUtils;
import com.sinjee.enums.OrderStatusEnum;
import com.sinjee.enums.PayStatusEnum;
import com.sinjee.service.ScheduledTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

/**
 * 创建时间 2020 - 02 -27
 *
 * @author kweitan
 */
@Component
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    @Autowired
    private OrderMasterService orderMasterService ;

    /**
     * 5分钟执行一次 处理未支付订单
     */
    @Scheduled(fixedRate=1000 * 300)
    @Override
    public void doWaitOrderStatus() {
        //更新订单状态和回滚库存
        orderMasterService.updateOrderStatus();;
    }
}
