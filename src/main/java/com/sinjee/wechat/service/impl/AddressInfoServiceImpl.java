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
import org.springframework.transaction.annotation.Transactional;

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
    public AddressInfoDTO getAddressByOpenid(String openid) {
        QueryWrapper<AddressInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("openid",openid).eq("select_status",1);
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
        List<AddressInfoDTO> addressInfoDTOList = BeanConversionUtils.copyToAnotherList(AddressInfoDTO.class,addressInfoList);

        Page<AddressInfoDTO> addressInfoDTOPage = new Page<>(currentPage,pageSize) ;
        addressInfoDTOPage.setPages(mapPage.getPages());
        addressInfoDTOPage.setTotal(mapPage.getTotal());
        addressInfoDTOPage.setRecords(addressInfoDTOList) ;

        return addressInfoDTOPage;

    }

    @Override
    @Transactional
    public Integer save(AddressInfoDTO addressInfoDTO) {
        AddressInfo addressInfo = new AddressInfo() ;
        CacheBeanCopier.copy(addressInfoDTO,addressInfo);

        QueryWrapper<AddressInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("openid",addressInfoDTO.getOpenid());
        List<AddressInfo> addressInfoList = addressInfoMapper.selectList(wrapper);
        if (addressInfoList != null && addressInfoList.size() < 2){
            addressInfo.setSelectStatus(1);
        }

        return addressInfoMapper.insert(addressInfo);
    }

    @Override
    @Transactional
    public Integer update(AddressInfoDTO addressInfoDTO) {
        AddressInfo addressInfo = new AddressInfo() ;
        CacheBeanCopier.copy(addressInfoDTO,addressInfo);
        QueryWrapper<AddressInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("address_id",addressInfoDTO.getAddressId());
        return addressInfoMapper.update(addressInfo,wrapper);
    }

    @Override
    @Transactional
    public Integer delete(Integer id) {
        QueryWrapper<AddressInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("address_id",id);
        AddressInfo addressInfo = new AddressInfo() ;
        addressInfo.setEnableFlag(0);
        return addressInfoMapper.update(addressInfo,wrapper);
    }

    @Override
    public List<AddressInfoDTO> getAllAddressByOpenid(String openid) {
        QueryWrapper<AddressInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("openid",openid) ;

        List<AddressInfo> addressInfoList = addressInfoMapper.selectList(wrapper) ;
        return BeanConversionUtils.copyToAnotherList(AddressInfoDTO.class,addressInfoList);
    }

    @Override
    @Transactional
    public Integer updateSelectStatus(String openid, Integer addressId) {
        AddressInfo addressInfo = new AddressInfo() ;
        QueryWrapper<AddressInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("openid",openid).eq("select_status",1);
        addressInfo.setSelectStatus(0);
        addressInfoMapper.update(addressInfo,wrapper);

        addressInfo.setSelectStatus(1);
        QueryWrapper<AddressInfo> wrapper1 = new QueryWrapper();
        wrapper1.eq("enable_flag",1).eq("openid",openid).eq("select_status",0)
        .eq("address_id",addressId);

        return addressInfoMapper.update(addressInfo,wrapper1);
    }
}
