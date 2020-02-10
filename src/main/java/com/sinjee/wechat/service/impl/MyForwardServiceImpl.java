package com.sinjee.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.GsonUtil;
import com.sinjee.exceptions.MyException;
import com.sinjee.wechat.dto.MyForwardDTO;
import com.sinjee.wechat.entity.MyForward;
import com.sinjee.wechat.mapper.MyForwardMapper;
import com.sinjee.wechat.service.MyForwardService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 创建时间 2020 - 01 -20
 *
 * @author kweitan
 */
@Service
@Slf4j
public class MyForwardServiceImpl implements MyForwardService {

    @Autowired
    private MyForwardMapper myForwardMapper ;

    @Override
    public Integer save(MyForwardDTO myForwardDTO) {
        MyForward myForward = new MyForward() ;
        CacheBeanCopier.copy(myForwardDTO,myForward);
        return myForwardMapper.insert(myForward);
    }

    @Override
    public Integer update(MyForwardDTO myForwardDTO) {
        MyForward myForward = new MyForward() ;
        CacheBeanCopier.copy(myForwardDTO,myForward);

        QueryWrapper<MyForward> wrapper = new QueryWrapper();
        wrapper.eq("openid",myForwardDTO.getOpenid()).eq("enable_flag",1);
        return myForwardMapper.update(myForward,wrapper);
    }

    @Override
    public Integer delete(Integer myForwardId) {
        MyForward myForward = new MyForward() ;
        myForward.setEnableFlag(0);

        QueryWrapper<MyForward> wrapper = new QueryWrapper();
        wrapper.eq("my_forward_id",myForwardId).eq("enable_flag",1);
        return myForwardMapper.update(myForward,wrapper);
    }

    @Override
    public IPage<MyForwardDTO> selectMyForwardByPage(Integer currentPage, Integer pageSize, String openId) {
        QueryWrapper<MyForward> wrapper = new QueryWrapper();
        wrapper.eq("openid",openId).eq("enable_flag",1);
        return returnPageByWrapper(currentPage,pageSize,wrapper);
    }

    @Override
    public IPage<MyForwardDTO> selectMyForwardByPages(Integer currentPage, Integer pageSize) {
        QueryWrapper<MyForward> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1);
        return returnPageByWrapper(currentPage,pageSize,wrapper);
    }

    @Override
    public MyForwardDTO selectOneMyForward(String productNumber, String openid) {
        QueryWrapper<MyForward> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_number",productNumber)
        .eq("openid",openid);
        MyForward myForward = myForwardMapper.selectOne(wrapper) ;
        MyForwardDTO myForwardDTO = new MyForwardDTO() ;
        log.info("myForwardDTO={}", GsonUtil.getInstance().toStr(myForward));
        CacheBeanCopier.copy(myForward,myForwardDTO);
        return myForwardDTO;
    }

    private IPage<MyForwardDTO> returnPageByWrapper(Integer currentPage, Integer pageSize,QueryWrapper<MyForward> wrapper){
        Page<MyForward> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<MyForward> mapPage = myForwardMapper.selectPage(page,wrapper);

        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<MyForward> myForwardList = mapPage.getRecords() ;
        List<MyForwardDTO> myForwardDTOList = BeanConversionUtils.copyToAnotherList(MyForwardDTO.class,myForwardList);

        Page<MyForwardDTO> myForwardDTOPage = new Page<>(currentPage,pageSize) ;
        myForwardDTOPage.setPages(mapPage.getPages());
        myForwardDTOPage.setTotal(mapPage.getTotal());
        myForwardDTOPage.setRecords(myForwardDTOList) ;
        return myForwardDTOPage ;
    }
}
