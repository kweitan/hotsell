package com.sinjee.wechat.controller;

import com.sinjee.common.ResultVOUtil;
import com.sinjee.service.TokenService;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.vo.WechatTokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 小小极客
 * 时间 2020/2/1 22:41
 * @ClassName WechatTokenController
 * 描述 TokenController
 **/
@RestController
@RequestMapping("/wechat")
public class WechatTokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/token")
    public ResultVO token() {
        WechatTokenVO wechatTokenVO = new WechatTokenVO() ;
        wechatTokenVO.setToken(tokenService.createToken());
        return ResultVOUtil.success(wechatTokenVO);
    }


}
