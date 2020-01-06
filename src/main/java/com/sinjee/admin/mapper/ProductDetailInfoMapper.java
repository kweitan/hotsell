package com.sinjee.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sinjee.admin.entity.ProductDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 创建时间 2020 - 01 -06
 * 商品明细DAO
 * @author kweitan
 */
@Repository
@Mapper
public interface ProductDetailInfoMapper extends BaseMapper<ProductDetailInfo> {
}
