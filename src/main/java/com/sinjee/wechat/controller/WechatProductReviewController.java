package com.sinjee.wechat.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.*;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.dto.ProductReviewDTO;
import com.sinjee.wechat.form.WechatOrderMasterForm;
import com.sinjee.wechat.form.WechatOrderReviewForm;
import com.sinjee.wechat.form.WechatProductReviewForm;
import com.sinjee.wechat.service.ProductReviewService;
import com.sinjee.wechat.vo.WechatProductReviewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/2/8 16:22
 * @ClassName WechatProductReviewController
 * 描述 WechatProductReviewController
 **/
@RestController
@RequestMapping("wechat/review")
@Slf4j
public class WechatProductReviewController {

    @Value("${myWechat.salt}")
    private String salt ;

    @Autowired
    private RedisUtil redisUtil ;

    @Autowired
    private ProductReviewService productReviewService ;

    @PostMapping("/saveProductReview")
    @AccessTokenIdempotency
    public ResultVO save(HttpServletRequest request, @Valid WechatOrderReviewForm wechatOrderReviewForm,
                         BindingResult bindingResult) {
        log.info("wechatOrderReviewForm={}",GsonUtil.getInstance().toStr(wechatOrderReviewForm));
        log.info("wechatOrderDetailReviewForm={}",GsonUtil.getInstance().toStr(wechatOrderReviewForm.getProductReviewLists()));

        if (bindingResult.hasErrors()){
            log.info("show="+bindingResult.getFieldError().getDefaultMessage());
            return ResultVOUtil.error(122,bindingResult.getFieldError().getDefaultMessage());
        }

        if (!HashUtil.verify(wechatOrderReviewForm.getOrderNumber(),salt,wechatOrderReviewForm.getHashNumber())){
            return ResultVOUtil.error(122,"数据不一致");
        }

        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);

        //log.info("WechatOrderMasterForm={}", GsonUtil.getInstance().toStr(wechatOrderReviewForm));

        List<WechatProductReviewForm> wechatProductReviewFormList = null;

        try {
            wechatProductReviewFormList = GsonUtil.getInstance().
                    parseString2List(wechatOrderReviewForm.getProductReviewLists(),WechatProductReviewForm.class);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultVOUtil.error(123,"商品数据转换出错");
        }

        if (null == wechatProductReviewFormList || wechatProductReviewFormList.size() <1){
            return ResultVOUtil.error(123,"无商品数据");
        }

        //取出用户信息
        Object object = redisUtil.getString(openid) ;
        if (null == object){
            return ResultVOUtil.error(121,"缓存已经过期") ;
        }
        BuyerInfoDTO buyerInfoDTO = (BuyerInfoDTO)object ;

        ProductReviewDTO productReviewDTO = new ProductReviewDTO() ;
        productReviewDTO.setPersonIcon(buyerInfoDTO.getAvatarUrl());
        productReviewDTO.setPersonName(buyerInfoDTO.getBuyerName());

        productReviewDTO.setOpenid(openid);
        productReviewDTO.setCreator(buyerInfoDTO.getBuyerName());
        productReviewDTO.setCreateTime(DateUtils.getTimestamp());
        productReviewDTO.setUpdater(buyerInfoDTO.getBuyerName());
        productReviewDTO.setUpdateTime(DateUtils.getTimestamp());

        productReviewDTO.setWechatProductReviewFormList(wechatProductReviewFormList);

        productReviewService.save(productReviewDTO) ;

        return ResultVOUtil.success();
    }

    /**
     * 获取我的评论
     * @param request
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/selectProductReviewByOpenid")
    @AccessTokenIdempotency
    public ResultVO selectProductReviewByOpenid(HttpServletRequest request,
                                                @RequestParam(value = "currentPage", defaultValue = "1")
                                                            Integer currentPage,
                                                @RequestParam(value = "pageSize", defaultValue = "8")
                                                            Integer pageSize) {

        //页大小
        if (currentPage > 20){
            currentPage = 20 ;
        }

        if(pageSize > 10){
            pageSize = 10 ;
        }

        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);

        IPage<ProductReviewDTO> productReviewDTOIPage = productReviewService
                .selectProductReviewByPage(currentPage,pageSize,openid) ;

        List<ProductReviewDTO> productReviewDTOList = productReviewDTOIPage.getRecords() ;
        List<WechatProductReviewVO> wechatProductReviewVOList = new ArrayList<>() ;
        if(null != productReviewDTOList && productReviewDTOList.size()>0){
            productReviewDTOList.stream().forEach(productReviewDTO-> {
                WechatProductReviewVO wechatProductReviewVO = new WechatProductReviewVO() ;
                CacheBeanCopier.copy(productReviewDTO, wechatProductReviewVO);
                wechatProductReviewVOList.add(wechatProductReviewVO);
            });
        }

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(wechatProductReviewVOList);
        resultVO.setCurrentPage(currentPage);
        resultVO.setTotalSize(productReviewDTOIPage.getTotal());
        resultVO.setPageTotal(productReviewDTOIPage.getPages());
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        return resultVO ;
    }
}
