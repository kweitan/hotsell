package com.sinjee.wechat.controller;

import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.AddressInfoDTO;
import com.sinjee.wechat.service.AddressInfoService;
import com.sinjee.wechat.vo.WechatAddressVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/2/4 10:13
 * @ClassName WechatAddressController
 * 描述 WechatAddressController
 **/
@RestController
@RequestMapping("wechat/address")
@Slf4j
public class WechatAddressController {

    @Autowired
    private AddressInfoService addressInfoService ;

    @GetMapping("/getAddressByOpenid")
    @AccessTokenIdempotency
    public ResultVO getAddressByOpenid(String openid){
        log.info("openid={}",openid);
        AddressInfoDTO addressInfoDTO = addressInfoService.getAddressByOpenid(openid) ;
        if (null == addressInfoDTO || StringUtils.isBlank(addressInfoDTO.getOpenId())){
            ResultVOUtil.error(261,"尚无默认地址");
        }

        WechatAddressVO wechatAddressVO = new WechatAddressVO() ;
        CacheBeanCopier.copy(addressInfoDTO,wechatAddressVO);

        return ResultVOUtil.success(wechatAddressVO) ;
    }

    @GetMapping("/getAllAddressByOpenid")
    @AccessTokenIdempotency
    public ResultVO getAllAddressByOpenid(String openid){
        log.info("openid={}",openid);
        List<AddressInfoDTO> addressInfoDTOList = addressInfoService.getAllAddressByOpenid(openid) ;
        if (null == addressInfoDTOList || addressInfoDTOList.size() == 0){
            ResultVOUtil.error(261,"尚无默认地址");
        }

        return ResultVOUtil.success(BeanConversionUtils.copyToAnotherList(WechatAddressVO.class,addressInfoDTOList)) ;
    }
}
