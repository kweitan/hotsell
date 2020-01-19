package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.BuyerReviewDTO;

public interface BuyerReviewService {

    /**保存评论**/
    Integer save(BuyerReviewDTO buyerReviewDTO) ;

    /**更新评论信息**/
    Integer update(BuyerReviewDTO buyerReviewDTO);

    /**根据订单编号查找评论信息**/
    BuyerReviewDTO findByOrderNumber(String orderNumber);

    /**根据openid查找评论信息**/
    BuyerReviewDTO findByOpenId(String openId);

    IPage<BuyerReviewDTO> selectBuyerByPage(Integer currentPage, Integer pageSize, String selectName);
}
