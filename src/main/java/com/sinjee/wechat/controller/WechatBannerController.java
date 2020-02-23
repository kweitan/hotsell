package com.sinjee.wechat.controller;

import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.WechatBannerDTO;
import com.sinjee.wechat.service.WechatBannerService;
import com.sinjee.wechat.vo.WechatBannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 2020 - 01 -26
 *
 * @author kweitan
 */
@RestController
@RequestMapping("wechat/banner")
public class WechatBannerController {

    @Autowired
    private WechatBannerService wechatBannerService ;

    @Value("${myWechat.salt}")
    private String salt ;

    /**
     * banner 缓存 更新的时候 注意更新缓存
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/indexList")
//    @Cacheable(cacheNames = "bannerIndexList", key = "'bannerIndexList'", unless = "#result.getCode() != 0")
    public ResultVO index(){

        List<WechatBannerDTO> wechatBannerDTOList = wechatBannerService.getBannerIndexList();
        List<WechatBannerVO> wechatBannerVOList = new ArrayList<>() ;
        if(null != wechatBannerDTOList && wechatBannerDTOList.size()>0){
            wechatBannerDTOList.stream().forEach(wechatBannerDTO -> {
                //根据商品编码生成唯一HASH编码
                String hashNumber = HashUtil.sign(wechatBannerDTO.getBannerId()+"",salt);
                WechatBannerVO wechatBannerVO = new WechatBannerVO() ;
                CacheBeanCopier.copy(wechatBannerDTO,wechatBannerVO);
                wechatBannerVO.setHashNumber(hashNumber);
                wechatBannerVOList.add(wechatBannerVO);
            });
        }

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(wechatBannerVOList);
        resultVO.setCode(0);
        resultVO.setMessage("成功");

        return resultVO;
    }

}
