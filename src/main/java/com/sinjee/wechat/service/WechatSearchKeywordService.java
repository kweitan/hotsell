package com.sinjee.wechat.service;

import com.sinjee.wechat.dto.WechatSearchKeywordDTO;

import java.util.List;

public interface WechatSearchKeywordService {

    Integer save(WechatSearchKeywordDTO wechatSearchKeywordDTO) ;

    Integer update(WechatSearchKeywordDTO wechatSearchKeywordDTO) ;

    Integer enableStatus(String searchKeywordNumber) ;

    Integer disableStatus(String searchKeywordNumber) ;

    Integer countKeyword();

    List<WechatSearchKeywordDTO> getList() ;
}
