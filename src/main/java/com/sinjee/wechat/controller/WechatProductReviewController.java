package com.sinjee.wechat.controller;

import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.*;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.dto.ProductReviewDTO;
import com.sinjee.wechat.form.ShopCartModel;
import com.sinjee.wechat.form.WechatOrderMasterForm;
import com.sinjee.wechat.form.WechatProductReviewForm;
import com.sinjee.wechat.service.ProductReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/saveProductReview")
    @AccessTokenIdempotency
    public ResultVO save(HttpServletRequest request, @Valid WechatOrderMasterForm wechatOrderMasterForm,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            log.info("show="+bindingResult.getFieldError().getDefaultMessage());
            return ResultVOUtil.error(122,bindingResult.getFieldError().getDefaultMessage());
        }

        if (HashUtil.verify(wechatOrderMasterForm.getOrderNumber(),salt,wechatOrderMasterForm.getHashNumber())){
            return ResultVOUtil.error(122,"数据不一致");
        }

        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);

        log.info("WechatOrderMasterForm={}", GsonUtil.getInstance().toStr(wechatOrderMasterForm));

        List<WechatProductReviewForm> wechatProductReviewFormList = null ;
        try {
            wechatProductReviewFormList = GsonUtil.getInstance().
                    parseString2List(wechatOrderMasterForm.getProductReviewLists(),WechatProductReviewForm.class);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultVOUtil.error(123,"商品数据转换出错");
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
}
