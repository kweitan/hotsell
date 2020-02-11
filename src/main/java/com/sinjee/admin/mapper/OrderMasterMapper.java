package com.sinjee.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.entity.OrderMaster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface OrderMasterMapper extends BaseMapper<OrderMaster> {

    IPage<OrderMaster> selectOrderMasterInfo(
            IPage<OrderMaster> page, @Param("map") Map<String, Object> params);
}
