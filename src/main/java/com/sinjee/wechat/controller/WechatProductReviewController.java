package com.sinjee.wechat.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.OrderMasterDTO;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.admin.service.OrderMasterService;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.*;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.dto.ProductReviewDTO;
import com.sinjee.wechat.form.WechatOrderReviewForm;
import com.sinjee.wechat.form.WechatProductReviewForm;
import com.sinjee.wechat.service.ProductReviewService;
import com.sinjee.wechat.vo.WechatOrderVO;
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

    @Autowired
    private OrderMasterService masterService ;

    @Autowired
    private ProductInfoService productInfoService ;


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
        productReviewDTO.setOrderNumber(wechatOrderReviewForm.getOrderNumber());

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


        //获取orderNumber
        IPage<OrderMasterDTO> orderMasterDTOIPage = masterService.findByOpenId(currentPage,pageSize,openid);

        List<OrderMasterDTO> orderMasterDTOList = orderMasterDTOIPage.getRecords();

        List<WechatOrderVO> wechatOrderVOList = BeanConversionUtils.copyToAnotherList(WechatOrderVO.class,orderMasterDTOList);

        wechatOrderVOList.stream().forEach(wechatOrderVO -> {
            List<ProductReviewDTO> productReviewDTOList =productReviewService.productReviewDTOListByOrderNumber(wechatOrderVO.getOrderNumber());
            List<WechatProductReviewVO> wechatProductReviewVOList = new ArrayList<>() ;
            for (int i=0; i < productReviewDTOList.size(); i++){
                ProductReviewDTO productReviewDTO = productReviewDTOList.get(i);
                ProductInfoDTO productInfoDTO = productInfoService.findByNumber(productReviewDTO.getProductNumber()) ;
                WechatProductReviewVO wechatProductReviewVO = new WechatProductReviewVO() ;
                CacheBeanCopier.copy(productReviewDTO,wechatProductReviewVO);

                wechatProductReviewVO.setProductIcon(productInfoDTO.getProductIcon());
                wechatProductReviewVO.setProductName(productInfoDTO.getProductName());
                wechatProductReviewVOList.add(wechatProductReviewVO) ;
            }
            wechatOrderVO.setProductReviewLists(wechatProductReviewVOList);
        });

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(wechatOrderVOList);
        resultVO.setCurrentPage(currentPage);
        resultVO.setTotalSize(orderMasterDTOIPage.getTotal());
        resultVO.setPageTotal(orderMasterDTOIPage.getPages());
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        return resultVO ;
    }
}
