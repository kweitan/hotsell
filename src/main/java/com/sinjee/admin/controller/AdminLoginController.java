package com.sinjee.admin.controller;

import com.sinjee.admin.dto.SellerInfoDTO;
import com.sinjee.admin.service.SellerInfoService;
import com.sinjee.common.*;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.utils.AdminAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 创建时间 2020 - 02 -19
 * 登录模块
 * @author kweitan
 */
@RestController
@RequestMapping("/admin/login")
@Slf4j
public class AdminLoginController {

    @Value("${admin.passwordSalt}")
    private String passwordSalt ;

    @Autowired
    private RedisUtil redisUtil ;

    @Autowired
    private SellerInfoService sellerInfoService ;

    //ip 记录登录次数
    private final static String IP_COUNT = ":IPCOUNT" ;

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResultVO login(HttpServletRequest request, String username,
                          String password, String verifyCode){

        //校验账号是否冻结
        Object beforeVerify = redisUtil.getString(KeyUtil.getIpAddr(request)+IP_COUNT);
        if (null != beforeVerify){
            Integer count = (Integer)beforeVerify;
            if (count > 5){
                ResultVOUtil.error(101,"账号已经冻结1天");
            }
        }

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(verifyCode)){
            return ResultVOUtil.error(101,"用户名密码为空");
        }

        Object obj = redisUtil.getString(KeyUtil.getIpAddr(request)) ;
        if (obj == null){
            return ResultVOUtil.error(101,"验证码已经过期");
        }

        String code = (String)obj ;
        if (!code.equals(verifyCode)){
            return ResultVOUtil.error(101,"验证码不正确");

        }

        SellerInfoDTO sellerInfoDTO = null ;
        try {
            //校验用户密码
            String passEnctry = AESCBCUtil.encrypt(password,passwordSalt) ;
            sellerInfoDTO = sellerInfoService.verifyUser(username,passEnctry) ;
            if (null == sellerInfoDTO){
                String res = "用户名或密码不正确" ;
                Object count = redisUtil.getString(KeyUtil.getIpAddr(request)+IP_COUNT);
                if (null == count){
                    //有效一天
                    redisUtil.setString(KeyUtil.getIpAddr(request)+IP_COUNT,1,86400) ;
                }else {
                    Integer c = (Integer)count ;
                    if (c > 2){
                        res = res + ",验证超过5次不正确,账号锁定一天";
                    }
                    c++ ;
                    redisUtil.setString(KeyUtil.getIpAddr(request)+IP_COUNT,c) ;
                }

                return ResultVOUtil.error(101,res);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultVOUtil.error(101,"密码解密出错");
        }

        //认证成功 返回adminToken
        String adminToken = AdminAccessTokenUtil.sign(sellerInfoDTO.getSellerNumber()) ;
        redisUtil.setString(sellerInfoDTO.getSellerNumber(),sellerInfoDTO, Constant.Redis.EXPIRE_TIME_28DAY) ;

        return ResultVOUtil.success(adminToken) ;
    }

}
