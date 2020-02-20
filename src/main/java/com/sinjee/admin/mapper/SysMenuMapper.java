package com.sinjee.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sinjee.admin.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 创建时间 2020 - 02 -20
 *
 * @author kweitan
 */
@Repository
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
}
