package com.sinjee.wechat.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.annotation.AccessLimit;
import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.DateUtils;
import com.sinjee.common.RedisUtil;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.dto.MyForwardDTO;
import com.sinjee.wechat.service.MyForwardService;
import com.sinjee.wechat.vo.WechatMyForwardVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/2/8 22:18
 * @ClassName WechatMyForwardController
 * 描述 WechatMyForwardController
 **/
@RestController
@RequestMapping("wechat/myforward")
@Slf4j
public class WechatMyForwardController {

    @Autowired
    private RedisUtil redisUtil ;

    @Autowired
    private MyForwardService myForwardService ;

    @Autowired
    private ProductInfoService productInfoService ;

    @PostMapping("/saveMyForward")
    @AccessTokenIdempotency
    @AccessLimit
    public ResultVO save(HttpServletRequest request,String productNumber) {
        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);

        //取出用户信息
        Object object = redisUtil.getString(openid) ;
        if (null == object){
            return ResultVOUtil.error(121,"用户缓存已经过期") ;
        }
        BuyerInfoDTO buyerInfoDTO = (BuyerInfoDTO)object ;

        MyForwardDTO dto = myForwardService.selectOneMyForward(productNumber,openid) ;
        if (null != dto && StringUtils.isNotBlank(dto.getProductNumber())){
            return ResultVOUtil.error(121,"产品已分享") ;
        }

        ProductInfoDTO productInfoDTO = productInfoService.findByNumber(productNumber) ;
        if (null == productInfoDTO || StringUtils.isBlank(productInfoDTO.getProductNumber())){
            return ResultVOUtil.error(121,"无分享产品") ;
        }

        MyForwardDTO myForwardDTO = new MyForwardDTO() ;
        myForwardDTO.setOpenid(openid);
        myForwardDTO.setProductDescription(productInfoDTO.getProductDescription());
        myForwardDTO.setProductIcon(productInfoDTO.getProductIcon());
        myForwardDTO.setProductNumber(productInfoDTO.getProductNumber());
        myForwardDTO.setProductPrice(productInfoDTO.getProductPrice());
        myForwardDTO.setProductName(productInfoDTO.getProductName());

        myForwardDTO.setCreator(buyerInfoDTO.getBuyerName());
        myForwardDTO.setCreateTime(DateUtils.getTimestamp());
        myForwardDTO.setUpdater(buyerInfoDTO.getBuyerName());
        myForwardDTO.setUpdateTime(DateUtils.getTimestamp());

        Integer res = myForwardService.save(myForwardDTO) ;
        if (res > 0) {
            return ResultVOUtil.success() ;
        }
        return ResultVOUtil.error(121,"分享失败") ;
    }

    /**
     * 获取我的分享列表
     * @param request
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/selectMyForwardByOpenid")
    @AccessTokenIdempotency
    public ResultVO selectMyForwardByOpenid(HttpServletRequest request,
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

        IPage<MyForwardDTO> myForwardDTOIPage = myForwardService
                .selectMyForwardByPage(currentPage,pageSize,openid) ;

        List<MyForwardDTO> myForwardDTOList = myForwardDTOIPage.getRecords() ;
        List<WechatMyForwardVO> wechatMyForwardVOList = new ArrayList<>() ;
        if(null != myForwardDTOList && myForwardDTOList.size()>0){
            myForwardDTOList.stream().forEach(myForwardDTO-> {
                WechatMyForwardVO wechatMyForwardVO = new WechatMyForwardVO() ;
                CacheBeanCopier.copy(myForwardDTO, wechatMyForwardVO);
                wechatMyForwardVOList.add(wechatMyForwardVO);
            });
        }

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(wechatMyForwardVOList);
        resultVO.setCurrentPage(currentPage);
        resultVO.setTotalSize(myForwardDTOIPage.getTotal());
        resultVO.setPageTotal(myForwardDTOIPage.getPages());
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        return resultVO ;
    }
}
