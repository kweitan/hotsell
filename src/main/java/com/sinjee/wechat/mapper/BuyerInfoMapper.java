package com.sinjee.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sinjee.wechat.entity.BuyerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 买家信息
 */
@Repository
@Mapper
public interface BuyerInfoMapper extends BaseMapper<BuyerInfo> {

}
