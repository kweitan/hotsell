package com.sinjee.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.entity.BuyerInfo;
import com.sinjee.wechat.mapper.BuyerInfoMapper;
import com.sinjee.wechat.service.BuyerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建时间 2020 - 01 -06
 * 买家信息
 * @author kweitan
 */
@Service
@Slf4j
public class BuyerInfoServiceImpl implements BuyerInfoService {

    @Autowired
    private BuyerInfoMapper buyerInfoMapper;

    @Override
    public Integer save(BuyerInfoDTO buyerInfoDTO) {

        BuyerInfo buyerInfo = new BuyerInfo() ;
        CacheBeanCopier.copy(buyerInfoDTO,buyerInfo);

        return buyerInfoMapper.insert(buyerInfo);
    }

    @Override
    public Integer update(BuyerInfoDTO buyerInfoDTO) {

        BuyerInfo buyerInfo = new BuyerInfo() ;
        CacheBeanCopier.copy(buyerInfoDTO,buyerInfo);

        QueryWrapper<BuyerInfo> wrapper = new QueryWrapper();
        wrapper.eq("open_id",buyerInfoDTO.getOpenId());
        return buyerInfoMapper.update(buyerInfo,wrapper);
    }

    @Override
    public BuyerInfoDTO find(String openId) {
        BuyerInfoDTO buyerInfoDTO = new BuyerInfoDTO();
        QueryWrapper<BuyerInfo> wrapper = new QueryWrapper();
        wrapper.eq("open_id",openId);
        BuyerInfo buyerInfo = buyerInfoMapper.selectOne(wrapper) ;
        CacheBeanCopier.copy(buyerInfo,buyerInfoDTO);
        return buyerInfoDTO;
    }

    @Override
    public Integer updateBlack(String openId) {
        BuyerInfo buyerInfo = new BuyerInfo() ;
        buyerInfo.setEnableFlag(0);
        QueryWrapper<BuyerInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("open_id",openId);
        return buyerInfoMapper.update(buyerInfo,wrapper);
    }

    @Override
    public IPage<BuyerInfoDTO> selectBuyerByPage(Integer currentPage, Integer pageSize, String selectName) {
        QueryWrapper<BuyerInfo> wrapper = new QueryWrapper();
        wrapper.like("buyer_name",selectName);
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
        return buyerInfoDTOPage ;
    }
}
