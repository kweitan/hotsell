package com.sinjee.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.wechat.dto.WechatBannerDTO;
import com.sinjee.wechat.entity.WechatBanner;
import com.sinjee.wechat.mapper.WechatBannerMapper;
import com.sinjee.wechat.service.WechatBannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 创建时间 2020 - 01 -26
 *
 * @author kweitan
 */
@Service
@Slf4j
public class WechatBannerServiceImpl implements WechatBannerService {

    @Autowired
    private WechatBannerMapper wechatBannerMapper ;

    @Override
    public Integer save(WechatBannerDTO wechatBannerDTO) {
        WechatBanner wechatBanner = new WechatBanner() ;
        CacheBeanCopier.copy(wechatBannerDTO,wechatBanner);
        return wechatBannerMapper.insert(wechatBanner);
    }

    @Override
    public Integer update(WechatBannerDTO wechatBannerDTO) {
        WechatBanner wechatBanner = new WechatBanner() ;
        CacheBeanCopier.copy(wechatBannerDTO,wechatBanner);

        QueryWrapper<WechatBanner> wrapper = new QueryWrapper();
        wrapper.eq("banner_id",wechatBannerDTO.getBannerId()).eq("enable_flag",1);
        return wechatBannerMapper.update(wechatBanner,wrapper);
    }

    @Override
    public List<WechatBannerDTO> getBannerIndexList() {
        QueryWrapper<WechatBanner> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1);

        List<WechatBanner> wechatBannerList = wechatBannerMapper.selectList(wrapper) ;
        return BeanConversionUtils.copyToAnotherList(WechatBannerDTO.class,wechatBannerList);
    }

    /**
     *
     * @param currentPage
     * @param pageSize
     * @param region 暂时留着备用
     * @return
     */
    @Override
    public IPage<WechatBannerDTO> getBannerByPage(Integer currentPage, Integer pageSize,String region) {
        QueryWrapper<WechatBanner> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1);
        Page<WechatBanner> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<WechatBanner> mapPage = wechatBannerMapper.selectPage(page,wrapper);

        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<WechatBanner> wechatBannerList = mapPage.getRecords() ;
        List<WechatBannerDTO> wechatBannerDTOList = BeanConversionUtils.copyToAnotherList(WechatBannerDTO.class,wechatBannerList);

        Page<WechatBannerDTO> wechatBannerDTOPage = new Page<>(currentPage,pageSize) ;
        wechatBannerDTOPage.setPages(mapPage.getPages());
        wechatBannerDTOPage.setTotal(mapPage.getTotal());
        wechatBannerDTOPage.setRecords(wechatBannerDTOList) ;
        return wechatBannerDTOPage ;
    }

    @Override
    public WechatBannerDTO selectOne(Integer bannerId) {
        QueryWrapper<WechatBanner> wrapper = new QueryWrapper();
        wrapper.eq("banner_id",bannerId).eq("enable_flag",1);
        WechatBanner wechatBanner = wechatBannerMapper.selectOne(wrapper);
        WechatBannerDTO wechatBannerDTO = new WechatBannerDTO() ;
        CacheBeanCopier.copy(wechatBanner,wechatBannerDTO);
        return wechatBannerDTO;
    }

    @Override
    @Transactional
    public Integer delete(Integer bannerId) {
        QueryWrapper<WechatBanner> wrapper = new QueryWrapper();
        wrapper.eq("banner_id",bannerId).eq("enable_flag",1);
        WechatBanner wechatBanner = new WechatBanner() ;
        wechatBanner.setEnableFlag(0);
        return wechatBannerMapper.update(wechatBanner,wrapper);
    }
}
