package com.sinjee.wechat.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.sinjee.common.GsonUtil;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.service.BuyerInfoService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private BuyerInfoService buyerInfoService ;

    @Autowired
    private WxMaService wxMaService ;

    /**
     * 登陆接口
     */
    @GetMapping("/login")
    public ResultVO login(String code){
        log.info("接收来自微信客户端的code:{}",code);
        if (StringUtils.isBlank(code)){
            return ResultVOUtil.error(101,"code为空！");
        }

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid() ;
            log.info("接收来自微信客户端的session:{}", GsonUtil.getInstance().toStr(session));
            log.info("接收来自微信客户端的openid:{}",session.getOpenid());
            // 可以增加自己的逻辑，关联业务相关数据
            return ResultVOUtil.success();
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return ResultVOUtil.error(101,"获取openid失败!");
        }

    }

    /**
     * 获取用户信息接口
     */
    @GetMapping("/info")
    public ResultVO info(String sessionKey,String signature, String rawData, String encryptedData, String iv) {

        // 用户信息校验
        if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return ResultVOUtil.error(101,"user check failed");
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        // 可以增加自己的逻辑，关联业务相关数据
        log.info("接收来自微信客户端的userInfo:{}", GsonUtil.getInstance().toStr(userInfo));
        return ResultVOUtil.success();
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
