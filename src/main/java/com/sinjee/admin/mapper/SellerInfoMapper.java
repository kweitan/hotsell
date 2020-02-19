package com.sinjee.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sinjee.admin.entity.SellerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SellerInfoMapper extends BaseMapper<SellerInfo> {
}
