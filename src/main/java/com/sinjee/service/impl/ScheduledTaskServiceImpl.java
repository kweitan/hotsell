package com.sinjee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sinjee.admin.entity.OrderMaster;
import com.sinjee.admin.mapper.OrderMasterMapper;
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
    private OrderMasterMapper orderMasterMapper ;

    public void test(){
        //判断时间是否过期
        Calendar calendar = Calendar.getInstance() ;
        calendar.add(Calendar.MINUTE,-10);
        //< createDate <![CDATA[ and create < #{createDate} ]]>
        String createDate = DateUtils.getFormatDateTime(calendar.getTime()) ;
    }

    /**
     * 5分钟执行一次 处理未支付订单
     */
    @Scheduled(fixedRate=1000 * 300)
    @Override
    public void doWaitOrderStatus() {
        //判断时间是否过期
        Calendar calendar = Calendar.getInstance() ;
        //分钟内订单未支付 取消
        calendar.add(Calendar.MINUTE,-10);
        //< createDate <![CDATA[ and create < #{createDate} ]]>
        String createDate = DateUtils.getFormatDateTime(calendar.getTime()) ;

        QueryWrapper<OrderMaster> wrapper = new QueryWrapper();
        wrapper.eq("order_status", OrderStatusEnum.NEW.getCode())
                .eq("pay_status", PayStatusEnum.WAIT.getCode())
                .lt("create_time",createDate)
                .eq("enable_flag",1);
        List<OrderMaster> orderMasterList = orderMasterMapper.selectList(wrapper) ;
        for (OrderMaster orderMaster : orderMasterList){
            QueryWrapper<OrderMaster> wrap = new QueryWrapper();
            wrap.eq("order_id",orderMaster.getOrderId());
            OrderMaster entity = new OrderMaster() ;
            entity.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
            entity.setPayStatus(PayStatusEnum.CLOSED.getCode());
            orderMasterMapper.update(entity,wrap) ;
        }
    }
}
