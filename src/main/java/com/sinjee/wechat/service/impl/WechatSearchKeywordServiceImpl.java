package com.sinjee.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.wechat.dto.WechatSearchKeywordDTO;
import com.sinjee.wechat.entity.WechatSearchKeyword;
import com.sinjee.wechat.mapper.WechatSearchKeywordMapper;
import com.sinjee.wechat.service.WechatSearchKeywordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建时间 2020 - 01 -29
 *
 * @author kweitan
 */
@Service
@Slf4j
public class WechatSearchKeywordServiceImpl implements WechatSearchKeywordService {

    @Autowired
    private WechatSearchKeywordMapper wechatSearchKeywordMapper ;

    @Override
    public Integer save(WechatSearchKeywordDTO wechatSearchKeywordDTO) {
        WechatSearchKeyword wechatSearchKeyword = new WechatSearchKeyword() ;
        CacheBeanCopier.copy(wechatSearchKeywordDTO,wechatSearchKeyword);
        return wechatSearchKeywordMapper.insert(wechatSearchKeyword);
    }

    @Override
    public Integer update(WechatSearchKeywordDTO wechatSearchKeywordDTO) {
        WechatSearchKeyword wechatSearchKeyword = new WechatSearchKeyword() ;
        CacheBeanCopier.copy(wechatSearchKeywordDTO,wechatSearchKeyword);

        QueryWrapper<WechatSearchKeyword> wrapper = new QueryWrapper();
        wrapper.eq("search_keyword_number",wechatSearchKeywordDTO.getSearchKeywordNumber()).eq("enable_flag",1);
        return wechatSearchKeywordMapper.update(wechatSearchKeyword,wrapper);
    }

    @Override
    public Integer enableStatus(String searchKeywordNumber) {
        WechatSearchKeyword wechatSearchKeyword = new WechatSearchKeyword() ;
        wechatSearchKeyword.setSearchKeywordStatus(1);
        QueryWrapper<WechatSearchKeyword> wrapper = new QueryWrapper();
        wrapper.eq("search_keyword_number",searchKeywordNumber).eq("enable_flag",1);
        return wechatSearchKeywordMapper.update(wechatSearchKeyword,wrapper);
    }

    @Override
    public Integer disableStatus(String searchKeywordNumber) {
        WechatSearchKeyword wechatSearchKeyword = new WechatSearchKeyword() ;
        wechatSearchKeyword.setSearchKeywordStatus(0);
        QueryWrapper<WechatSearchKeyword> wrapper = new QueryWrapper();
        wrapper.eq("search_keyword_number",searchKeywordNumber).eq("enable_flag",1);
        return wechatSearchKeywordMapper.update(wechatSearchKeyword,wrapper);
    }

    @Override
    public Integer countKeyword() {
        QueryWrapper<WechatSearchKeyword> wrapper = new QueryWrapper();
        wrapper.eq("search_keyword_status",1).eq("enable_flag",1);
        return wechatSearchKeywordMapper.selectCount(wrapper);
    }

    @Override
    public List<WechatSearchKeywordDTO> getList() {
        QueryWrapper<WechatSearchKeyword> wrapper = new QueryWrapper();
        wrapper.eq("search_keyword_status",1).eq("enable_flag",1);
        List<WechatSearchKeyword> wechatSearchKeywordList = wechatSearchKeywordMapper.selectList(wrapper);
        return BeanConversionUtils.copyToAnotherList(WechatSearchKeywordDTO.class,wechatSearchKeywordList);
    }
}
