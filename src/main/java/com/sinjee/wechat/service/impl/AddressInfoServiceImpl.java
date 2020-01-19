package com.sinjee.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.wechat.dto.AddressInfoDTO;
import com.sinjee.wechat.entity.AddressInfo;
import com.sinjee.wechat.mapper.AddressInfoMapper;
import com.sinjee.wechat.service.AddressInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Service
@Slf4j
public class AddressInfoServiceImpl implements AddressInfoService {

    @Autowired
    AddressInfoMapper addressInfoMapper ;

    @Override
    public AddressInfoDTO getAddressById(Integer id) {
        QueryWrapper<AddressInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("address_id",id);
        AddressInfo addressInfo = addressInfoMapper.selectOne(wrapper) ;
        AddressInfoDTO addressInfoDTO = new AddressInfoDTO() ;
        CacheBeanCopier.copy(addressInfo,addressInfoDTO);
        return addressInfoDTO;
    }

    @Override
    public IPage<AddressInfoDTO> selectAddressByPage(Integer currentPage, Integer pageSize,String selectName) {

        QueryWrapper<AddressInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).like("buyer_name",selectName);
        Page<AddressInfo> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<AddressInfo> mapPage = addressInfoMapper.selectPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<AddressInfo> addressInfoList = mapPage.getRecords() ;
        List<AddressInfoDTO> addressInfoDTOList = BeanConversionUtils.CopyToAnotherList(AddressInfoDTO.class,addressInfoList);

        Page<AddressInfoDTO> addressInfoDTOPage = new Page<>(currentPage,pageSize) ;
        addressInfoDTOPage.setPages(mapPage.getPages());
        addressInfoDTOPage.setTotal(mapPage.getTotal());
        addressInfoDTOPage.setRecords(addressInfoDTOList) ;

        return addressInfoDTOPage;

    }

    @Override
    public Integer save(AddressInfoDTO addressInfoDTO) {
        AddressInfo addressInfo = new AddressInfo() ;
        CacheBeanCopier.copy(addressInfoDTO,addressInfo);
        return addressInfoMapper.insert(addressInfo);
    }

    @Override
    public Integer update(AddressInfoDTO addressInfoDTO) {
        AddressInfo addressInfo = new AddressInfo() ;
        CacheBeanCopier.copy(addressInfoDTO,addressInfo);
        QueryWrapper<AddressInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("address_id",addressInfoDTO.getAddressId());
        return addressInfoMapper.update(addressInfo,wrapper);
    }

    @Override
    public Integer delete(Integer id) {
        QueryWrapper<AddressInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("address_id",id);
        AddressInfo addressInfo = new AddressInfo() ;
        addressInfo.setEnableFlag(0);
        return addressInfoMapper.update(addressInfo,wrapper);
    }
}
