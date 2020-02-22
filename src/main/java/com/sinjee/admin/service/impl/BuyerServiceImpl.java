package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.admin.service.BuyerService;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.entity.BuyerInfo;
import com.sinjee.wechat.mapper.BuyerInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/2/22 10:20
 * @ClassName BuyerServiceImpl
 * 描述 BuyerServiceImpl
 **/

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private BuyerInfoMapper buyerInfoMapper ;

    @Override
    public IPage<BuyerInfoDTO> selectBuyerInfoByPage(Integer currentPage, Integer pageSize, String selectName) {
        QueryWrapper<BuyerInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).like("buyer_name",selectName);
        Page<BuyerInfo> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<BuyerInfo> mapPage = buyerInfoMapper.selectPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<BuyerInfo> buyerInfoList = mapPage.getRecords() ;
        List<BuyerInfoDTO> buyerInfoDTOList = BeanConversionUtils.copyToAnotherList(BuyerInfoDTO.class,buyerInfoList);

        Page<BuyerInfoDTO> buyerInfoDTOPage = new Page<>(currentPage,pageSize) ;
        buyerInfoDTOPage.setPages(mapPage.getPages());
        buyerInfoDTOPage.setTotal(mapPage.getTotal());
        buyerInfoDTOPage.setRecords(buyerInfoDTOList) ;

        return buyerInfoDTOPage;
    }
}
