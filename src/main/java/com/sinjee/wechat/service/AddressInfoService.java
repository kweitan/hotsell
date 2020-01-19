package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.AddressInfoDTO;

public interface AddressInfoService {

    AddressInfoDTO getAddressById(Integer id);

    IPage<AddressInfoDTO> selectAddressByPage(Integer currentPage, Integer pageSize,String selectName);

    Integer save(AddressInfoDTO addressInfoDTO) ;

    Integer update(AddressInfoDTO addressInfoDTO) ;

    Integer delete(Integer id);
}
