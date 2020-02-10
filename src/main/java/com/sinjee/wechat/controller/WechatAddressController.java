package com.sinjee.wechat.controller;

import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.*;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.AddressInfoDTO;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.form.WechatAddressForm;
import com.sinjee.wechat.service.AddressInfoService;
import com.sinjee.wechat.vo.WechatAddressVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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


    @Autowired
    private RedisUtil redisUtil ;

    @Value("${myWechat.salt}")
    private String salt ;

    @CrossOrigin(origins = "*")
    @GetMapping("/getAddressByOpenid")
    @AccessTokenIdempotency
    public ResultVO getAddressByOpenid(HttpServletRequest request){

        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);
        AddressInfoDTO addressInfoDTO = addressInfoService.getAddressByOpenid(openid) ;
        if (null == addressInfoDTO || StringUtils.isBlank(addressInfoDTO.getOpenid())){
            return ResultVOUtil.error(261,"尚无默认地址");
        }

        WechatAddressVO wechatAddressVO = new WechatAddressVO() ;
        CacheBeanCopier.copy(addressInfoDTO,wechatAddressVO);

        wechatAddressVO.setHashNumber(HashUtil.sign(String.valueOf(wechatAddressVO.getAddressId()),salt));

        return ResultVOUtil.success(wechatAddressVO) ;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getAllAddressByOpenid")
    @AccessTokenIdempotency
    public ResultVO getAllAddressByOpenid(HttpServletRequest request){
        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);
        List<AddressInfoDTO> addressInfoDTOList = addressInfoService.getAllAddressByOpenid(openid) ;
        if (null == addressInfoDTOList || addressInfoDTOList.size() == 0){
            return ResultVOUtil.error(262,"尚无地址");
        }

        List<WechatAddressVO> wechatAddressVOList = new ArrayList<>() ;
        for (AddressInfoDTO dto: addressInfoDTOList){
            WechatAddressVO wechatAddressVO = new WechatAddressVO() ;
            CacheBeanCopier.copy(dto,wechatAddressVO);
            wechatAddressVO.setHashNumber(HashUtil.sign(String.valueOf(dto.getAddressId()),salt));
            wechatAddressVOList.add(wechatAddressVO) ;
        }

        return ResultVOUtil.success(wechatAddressVOList) ;
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/addOrEdit")
    @AccessTokenIdempotency
    public ResultVO addOrEdit(HttpServletRequest request, @Valid WechatAddressForm wechatAddressForm, BindingResult bindingResult){

        log.info("addressInfo={}",GsonUtil.getInstance().toStr(wechatAddressForm));

        String openid = (String)request.getAttribute("openid") ;
        if (bindingResult.hasErrors()){
            return ResultVOUtil.error(263,bindingResult.getFieldError().getDefaultMessage()) ;
        }

        String type = wechatAddressForm.getType() ;
        if (StringUtils.isBlank(type)){
            return ResultVOUtil.error(263,"类型必选") ;
        }

        //检验一致性
        if ("editor".equals(type) && !HashUtil.verify(wechatAddressForm.getNumber(),salt,wechatAddressForm.getHashNumber())){
            return ResultVOUtil.error(263,"数据不一致") ;
        }

        Object object = redisUtil.getString(openid) ;
        if (null == object){
            return ResultVOUtil.error(263,"缓存已经过期") ;
        }
        BuyerInfoDTO buyerInfoDTO = (BuyerInfoDTO)object ;

        AddressInfoDTO addressInfoDTO = new AddressInfoDTO() ;

        addressInfoDTO.setOpenid(openid);
        addressInfoDTO.setAddressLabels(wechatAddressForm.getLabel());
        addressInfoDTO.setBuyerAddress(wechatAddressForm.getAddressInfo());
        addressInfoDTO.setBuyerName(wechatAddressForm.getName());
        addressInfoDTO.setBuyerPhone(wechatAddressForm.getPhone());
        addressInfoDTO.setUpdater(buyerInfoDTO.getBuyerName());
        addressInfoDTO.setUpdateTime(DateUtils.getTimestamp());

        Integer res = 0 ;
        if ("add".equals(type)){
            addressInfoDTO.setCreator(buyerInfoDTO.getBuyerName());
            addressInfoDTO.setUpdateTime(DateUtils.getTimestamp());
            res = addressInfoService.save(addressInfoDTO);
        }else if ("editor".equals(type)){
            addressInfoDTO.setAddressId(Integer.valueOf(wechatAddressForm.getNumber()));
            res = addressInfoService.update(addressInfoDTO);
        }

        if (res > 0){
            return ResultVOUtil.success() ;
        }

        return ResultVOUtil.error(263,"地址保存失败") ;
    }

    /**
     * 更新默认状态
     * @param request
     * @param addressNumber
     * @param hashNumber
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/updateSelectStatus")
    @AccessTokenIdempotency
    public ResultVO updatSelectStatus(HttpServletRequest request, String addressNumber,String hashNumber){
        String openid = (String)request.getAttribute("openid") ;
        log.info("openid={}",openid);
        if (!HashUtil.verify(addressNumber,salt,hashNumber)){
            return ResultVOUtil.error(263,"数据不一致") ;
        }

        Integer res = addressInfoService.updateSelectStatus(openid,Integer.valueOf(addressNumber)) ;
        if (res > 0){
            return ResultVOUtil.success() ;
        }
        return ResultVOUtil.error(263,"地址状态更新失败") ;
    }
}
