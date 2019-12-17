package com.sinjee.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sinjee.wechat.entity.ProductCategoryMid;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 类目中间表DAO
 */
@Repository
@Mapper
public interface ProductCategoryMidMapper extends BaseMapper<ProductCategoryMid> {
}
