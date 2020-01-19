package com.sinjee.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.wechat.dto.BuyerReviewDTO;
import com.sinjee.wechat.entity.BuyerReview;
import com.sinjee.wechat.mapper.BuyerReviewMapper;
import com.sinjee.wechat.service.BuyerReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
@Service
@Slf4j
public class BuyerReviewServiceImpl implements BuyerReviewService {

    @Autowired
    private BuyerReviewMapper buyerReviewMapper ;

    @Override
    public Integer save(BuyerReviewDTO buyerReviewDTO) {
        BuyerReview buyerReview = new BuyerReview() ;
        CacheBeanCopier.copy(buyerReviewDTO,buyerReview);
        return buyerReviewMapper.insert(buyerReview);
    }

    @Override
    public Integer update(BuyerReviewDTO buyerReviewDTO) {
        BuyerReview buyerReview = new BuyerReview() ;
        CacheBeanCopier.copy(buyerReviewDTO,buyerReview);

        QueryWrapper<BuyerReview> wrapper = new QueryWrapper();
        wrapper.eq("open_id",buyerReviewDTO.getOpenId());
        return buyerReviewMapper.update(buyerReview,wrapper);
    }

    @Override
    public BuyerReviewDTO findByOrderNumber(String orderNumber) {
        BuyerReviewDTO buyerReviewDTO = new BuyerReviewDTO() ;
        QueryWrapper<BuyerReview> wrapper = new QueryWrapper();
        wrapper.eq("order_number",orderNumber);
        BuyerReview buyerReview = buyerReviewMapper.selectOne(wrapper);
        CacheBeanCopier.copy(buyerReview,buyerReviewDTO);
        return buyerReviewDTO;
    }

    @Override
    public BuyerReviewDTO findByOpenId(String openId) {
        BuyerReviewDTO buyerReviewDTO = new BuyerReviewDTO() ;
        QueryWrapper<BuyerReview> wrapper = new QueryWrapper();
        wrapper.eq("open_id",openId);
        BuyerReview buyerReview = buyerReviewMapper.selectOne(wrapper);
        CacheBeanCopier.copy(buyerReview,buyerReviewDTO);
        return buyerReviewDTO;
    }

    @Override
    public IPage<BuyerReviewDTO> selectBuyerByPage(Integer currentPage, Integer pageSize, String selectName) {
        QueryWrapper<BuyerReview> wrapper = new QueryWrapper();
        wrapper.like("buyer_review_content",selectName);
        Page<BuyerReview> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<BuyerReview> mapPage = buyerReviewMapper.selectPage(page,wrapper);

        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<BuyerReview> buyerReviewList = mapPage.getRecords() ;
        List<BuyerReviewDTO> buyerReviewDTOList = BeanConversionUtils.CopyToAnotherList(BuyerReviewDTO.class,buyerReviewList);

        Page<BuyerReviewDTO> buyerReviewDTOPage = new Page<>(currentPage,pageSize) ;
        buyerReviewDTOPage.setPages(mapPage.getPages());
        buyerReviewDTOPage.setTotal(mapPage.getTotal());
        buyerReviewDTOPage.setRecords(buyerReviewDTOList) ;
        return buyerReviewDTOPage ;
    }
}
