package com.sinjee.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sinjee.wechat.entity.OrderFlow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 创建时间 2020 - 02 -18
 *
 * @author kweitan
 */
@Repository
@Mapper
public interface OrderFlowMapper extends BaseMapper<OrderFlow> {
}
