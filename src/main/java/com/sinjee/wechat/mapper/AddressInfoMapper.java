package com.sinjee.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sinjee.wechat.entity.AddressInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AddressInfoMapper extends BaseMapper<AddressInfo> {
}
