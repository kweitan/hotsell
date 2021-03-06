package com.sinjee.wechat.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.exceptions.MyException;
import com.sinjee.service.TokenService;
import com.sinjee.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 小小极客
 * 时间 2020/2/1 22:41
 * @ClassName WechatTokenController
 * 描述 TokenController
 **/
@RestController
@RequestMapping("wechat/token")
public class WechatTokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private WxMaService wxMaService ;


    @GetMapping("/genToken")
    @AccessTokenIdempotency
    public ResultVO token() {
        Map<String,Object> map = new HashMap<>() ;
        map.put("token",tokenService.createToken());
        return ResultVOUtil.success(map);
    }

    //返回access_token 用于生成菊花二维码
    @GetMapping("/genAccessToken")
    @AccessTokenIdempotency
    public ResultVO genAccessToken() {
        Map<String,Object> map = new HashMap<>() ;

        try {
            //生成菊花二维码
//            wxMaService.getQrcodeService().createWxaCodeUnlimit("","") ;
            map.put("access_token",wxMaService.getAccessToken());
        }catch (Exception e){
            throw new MyException(101,"生成AccessToken失败") ;
        }

        return ResultVOUtil.success(map);
    }


}
