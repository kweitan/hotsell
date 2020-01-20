package com.sinjee.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sinjee.wechat.entity.MyForward;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/***
 * 我的转发
 */
@Repository
@Mapper
public interface MyForwardMapper extends BaseMapper<MyForward> {
}
