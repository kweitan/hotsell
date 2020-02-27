package com.sinjee.service.impl;

import com.sinjee.common.DateUtils;
import com.sinjee.service.ScheduledTaskService;

import java.util.Calendar;

/**
 * 创建时间 2020 - 02 -27
 *
 * @author kweitan
 */
public class ScheduledTaskServiceImpl implements ScheduledTaskService {
    public void test(){
        //判断时间是否过期
        Calendar calendar = Calendar.getInstance() ;
        calendar.add(Calendar.MINUTE,-10);
        //< createDate <![CDATA[ and create < #{createDate} ]]>
        String createDate = DateUtils.getFormatDateTime(calendar.getTime()) ;
    }
}
