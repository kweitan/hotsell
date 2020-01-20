package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.MyForwardDTO;

public interface MyForwardService {

    Integer save(MyForwardDTO myForwardDTO) ;

    Integer update(MyForwardDTO myForwardDTO) ;

    Integer delete(Integer myForwardId);

    IPage<MyForwardDTO> selectMyForwardByPage(Integer currentPage, Integer pageSize, String openId);

    IPage<MyForwardDTO> selectMyForwardByPages(Integer currentPage, Integer pageSize);

}
