package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.WechatBannerDTO;

import java.util.List;

public interface WechatBannerService {

    Integer save(WechatBannerDTO wechatBannerDTO);

    Integer update(WechatBannerDTO wechatBannerDTO);

    List<WechatBannerDTO> getBannerIndexList();

    IPage<WechatBannerDTO> getBannerByPage(Integer currentPage, Integer pageSize, String region) ;
}
