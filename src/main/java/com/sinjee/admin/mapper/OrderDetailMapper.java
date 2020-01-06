package com.sinjee.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sinjee.admin.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Repository
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
