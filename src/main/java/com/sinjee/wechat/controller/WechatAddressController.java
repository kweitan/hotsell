package com.sinjee.wechat.controller;

import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.AddressInfoDTO;
import com.sinjee.wechat.form.ShopCartForm;
import com.sinjee.wechat.form.WechatAddressForm;
import com.sinjee.wechat.service.AddressInfoService;
import com.sinjee.wechat.vo.WechatAddressVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @CrossOrigin(origins = "*")
    @GetMapping("/getAddressByOpenid")
    @AccessTokenIdempotency
    public ResultVO getAddressByOpenid(String openid){

        log.info("openid={}",openid);
        AddressInfoDTO addressInfoDTO = addressInfoService.getAddressByOpenid(openid) ;
        if (null == addressInfoDTO || StringUtils.isBlank(addressInfoDTO.getOpenid())){
            return ResultVOUtil.error(261,"尚无默认地址");
        }

        WechatAddressVO wechatAddressVO = new WechatAddressVO() ;
        CacheBeanCopier.copy(addressInfoDTO,wechatAddressVO);

        return ResultVOUtil.success(wechatAddressVO) ;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getAllAddressByOpenid")
    @AccessTokenIdempotency
    public ResultVO getAllAddressByOpenid(String openid){
        log.info("openid={}",openid);
        List<AddressInfoDTO> addressInfoDTOList = addressInfoService.getAllAddressByOpenid(openid) ;
        if (null == addressInfoDTOList || addressInfoDTOList.size() == 0){
            return ResultVOUtil.error(262,"尚无地址");
        }

        return ResultVOUtil.success(BeanConversionUtils.copyToAnotherList(WechatAddressVO.class,addressInfoDTOList)) ;
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/addOrEdit")
    @AccessTokenIdempotency
    public ResultVO addOrEdit(String type,String openid, @Valid WechatAddressForm wechatAddressForm, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultVOUtil.error(263,bindingResult.getFieldError().getDefaultMessage()) ;
        }

        AddressInfoDTO addressInfoDTO = new AddressInfoDTO() ;
        addressInfoDTO.setOpenid(openid);
        addressInfoDTO.setAddressLabels(wechatAddressForm.getLabel());
        addressInfoDTO.setBuyerAddress(wechatAddressForm.getAddress());
        addressInfoDTO.setBuyerName(wechatAddressForm.getName());
        addressInfoDTO.setBuyerPhone(wechatAddressForm.getPhone());
        Integer res = 0 ;
        if ("add".equals(type)){
            res = addressInfoService.save(addressInfoDTO);
        }else if ("edit".equals(type)){
            res = addressInfoService.update(addressInfoDTO);
        }

        if (!(res > 0)){
            return ResultVOUtil.error(263,"地址保存失败") ;
        }

        return ResultVOUtil.success() ;
    }
}
