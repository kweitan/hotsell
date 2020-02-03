package com.sinjee.wechat.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.sinjee.common.*;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.service.BuyerInfoService;
import com.sinjee.wechat.utils.WechatAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建时间 2020 - 01 -20
 *
 * @author kweitan
 */
@RestController
@Slf4j
@RequestMapping("wechat/buyer")
public class BuyerInfoController {

    @Value("${myWechat.miniSalt}")
    private String miniSalt ;

    @Value("${wechat.md5Salt}")
    private String md5Salt ;

    @Autowired
    private BuyerInfoService buyerInfoService ;

    @Autowired
    private WxMaService wxMaService ;

    @Autowired
    private RedisUtil redisUtil ;


    /**
     * 重新生成token
     */
    @GetMapping("/genToken")
    public ResultVO reGenToken(String code){
        log.info("接收来自微信客户端的code:{}",code);
        if (StringUtils.isBlank(code)){
            return ResultVOUtil.error(101,"code为空！");
        }

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            //1.先查询是否存在openid
            BuyerInfoDTO selectBuyerInfo = buyerInfoService.find(openid) ;
            if (null == selectBuyerInfo || null == selectBuyerInfo.getOpenId()){
                return ResultVOUtil.error(101,"请先授权");
            }

            log.info("openid={}",openid);
            String accessToken = WechatAccessTokenUtil.sign(openid) ;
            Map<String,Object> map = new HashMap<>() ;
            map.put("accessToken",accessToken) ;
            return ResultVOUtil.success(map);
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResultVOUtil.error(101,"获取openid失败!");
        }

    }

    /**
     * 登陆接口
     */
    @GetMapping("/login")
    public ResultVO login(String code){
        log.info("接收来自微信客户端的code:{}",code);
        if (StringUtils.isBlank(code)){
            return ResultVOUtil.error(101,"code为空！");
        }

        String sessionKey = buyerInfoService.login(code);
        Map<String,Object> map = new HashMap<>() ;
        try {
            if (!sessionKey.equals("")){
                map.put("sessionKeys",AESCBCUtil.encrypt(sessionKey,md5Salt)) ;
                return ResultVOUtil.success(map) ;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultVOUtil.error(101,"user check failed");
        }
        return ResultVOUtil.error(101,"user check failed");
    }

    /**
     * 获取用户信息接口
     */
    @PostMapping("/info")
    public ResultVO info(String sessionKeys,String signature, String rawData, String encryptedData, String iv) {

        // 取出sessionkey
        String sessionKey = null;
        try {
            sessionKey = AESCBCUtil.decrypt(sessionKeys,md5Salt);
        } catch (Exception e) {
            return ResultVOUtil.error(101,"user check failed");
        }

        if (null == sessionKey){
            return ResultVOUtil.error(101,"user check failed");
        }

        // 用户信息校验
        if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return ResultVOUtil.error(101,"user check failed");
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        // 可以增加自己的逻辑，关联业务相关数据
        log.info("接收来自微信客户端的userInfo:{}", GsonUtil.getInstance().toStr(userInfo));

        BuyerInfoDTO buyerInfoDTO = new BuyerInfoDTO() ;
        buyerInfoDTO.setAvatarUrl(userInfo.getAvatarUrl());
        buyerInfoDTO.setBuyerCity(userInfo.getCity());
        buyerInfoDTO.setBuyerCountry(userInfo.getCountry());
        buyerInfoDTO.setBuyerGender(Integer.valueOf(userInfo.getGender()));
        buyerInfoDTO.setBuyerProvince(userInfo.getProvince());
        buyerInfoDTO.setBuyerName(userInfo.getNickName());
        buyerInfoDTO.setCreator(userInfo.getNickName());
        buyerInfoDTO.setUpdater(userInfo.getNickName());
        buyerInfoDTO.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        buyerInfoDTO.setOpenId(userInfo.getOpenId());

        Integer success = buyerInfoService.update(buyerInfoDTO) ;
        if (!(success>0)){
            return ResultVOUtil.error(101,"更新user失败");
        }
        //将用户存到redis中
        boolean res = redisUtil.setString(userInfo.getOpenId(),buyerInfoDTO,Long.valueOf(ConfigInfoUtil.expireTime));
        if (!res){
            return ResultVOUtil.error(101,"存放redis失败");
        }

        String accessToken = WechatAccessTokenUtil.sign(userInfo.getOpenId()) ;
        Map<String,Object> map = new HashMap<>() ;
        map.put("accessToken",accessToken) ;
        return ResultVOUtil.success(map);
    }

    /**
     * 获取用户绑定手机号信息
     */
    @GetMapping("/phone")
    public ResultVO phone(String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        // 用户信息校验
        if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return ResultVOUtil.error(101,"user check failed");
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        // 可以增加自己的逻辑，关联业务相关数据
        log.info("接收来自微信客户端的phoneNoInfo:{}", GsonUtil.getInstance().toStr(phoneNoInfo));
        return ResultVOUtil.success();
    }
}
