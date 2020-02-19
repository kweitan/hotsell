package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sinjee.admin.dto.SellerInfoDTO;
import com.sinjee.admin.entity.SellerInfo;
import com.sinjee.admin.mapper.SellerInfoMapper;
import com.sinjee.admin.service.SellerInfoService;
import com.sinjee.common.CacheBeanCopier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建时间 2020 - 02 -19
 *
 * @author kweitan
 */
@Service
@Slf4j
public class SellerInfoServiceImpl implements SellerInfoService {
    @Autowired
    private SellerInfoMapper sellerInfoMapper ;

    @Override
    public SellerInfoDTO verifyUser(String username, String password) {

        QueryWrapper<SellerInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("seller_name",username).eq("seller_password",password);
        SellerInfo sellerInfo = sellerInfoMapper.selectOne(wrapper) ;
        if (sellerInfo == null || StringUtils.isBlank(sellerInfo.getSellerNumber())){
            return null ;
        }
        SellerInfoDTO sellerInfoDTO = new SellerInfoDTO() ;
        CacheBeanCopier.copy(sellerInfo,sellerInfoDTO);
        return sellerInfoDTO;
    }

    @Override
    public SellerInfoDTO find(String sellerNumber) {
        QueryWrapper<SellerInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("seller_number",sellerNumber);
        SellerInfo sellerInfo = sellerInfoMapper.selectOne(wrapper) ;
        if (sellerInfo == null || StringUtils.isBlank(sellerInfo.getSellerNumber())){
            return null ;
        }
        SellerInfoDTO sellerInfoDTO = new SellerInfoDTO() ;
        CacheBeanCopier.copy(sellerInfo,sellerInfoDTO);
        return sellerInfoDTO;
    }
}
