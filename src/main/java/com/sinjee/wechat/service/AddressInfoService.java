package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.AddressInfoDTO;

import java.util.List;

public interface AddressInfoService {

    AddressInfoDTO getAddressById(Integer id);

    AddressInfoDTO getAddressByOpenid(String openid);

    IPage<AddressInfoDTO> selectAddressByPage(Integer currentPage, Integer pageSize,String selectName);

    Integer save(AddressInfoDTO addressInfoDTO) ;

    Integer update(AddressInfoDTO addressInfoDTO) ;

    Integer delete(Integer id);

    List<AddressInfoDTO> getAllAddressByOpenid(String openid);

    Integer updateSelectStatus(String openid,Integer addressId) ;
}
