package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.BuyerInfoDTO;

public interface BuyerInfoService {

    /**保存买家信息**/
    Integer save(BuyerInfoDTO buyerInfoDTO) ;

    /**更新买家信息**/
    Integer update(BuyerInfoDTO buyerInfoDTO);

    /**更具openid查找买家信息**/
    BuyerInfoDTO find(String openId);

    /**拉去客服信息**/
    BuyerInfoDTO findServiceInfo();

    /**拉黑买家信息**/
    Integer updateBlack(String openId);

    IPage<BuyerInfoDTO> selectBuyerByPage(Integer currentPage, Integer pageSize, String selectName);

    boolean saveOrUpdate(BuyerInfoDTO buyerInfoDTO);

    String login(String code);
}
