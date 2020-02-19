package com.sinjee.aspect;

import com.sinjee.admin.service.SellerInfoService;
import com.sinjee.common.RedisUtil;
import com.sinjee.exceptions.SellerAuthorizeException;
import com.sinjee.wechat.utils.AdminAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author 小小极客
 * 时间 2020/2/19 22:08
 * @ClassName SellerAuthorizeAspect
 * 描述 SellerAuthorizeAspect 登录AOP校验
 **/
@Aspect
@Component
@Slf4j
public class SellerAuthorizeAspect {

    @Autowired
    private RedisUtil redisUtil ;

    @Autowired
    private SellerInfoService sellerInfoService ;

    @Pointcut("execution(public * com.sinjee.admin..controller.Admin*.*(..))" +
            "&& !execution(public * com.sinjee.admin.controller.AdminLoginController.*(..))")
    public void verify() {}

    @Before("verify()")
    public void doVerify() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info("进入登录AOP拦截器");
        String adminToken = request.getHeader("adminToken");// 从 http 请求头中取出 adminToken
        if (StringUtils.isBlank(adminToken)){
            log.info("已经过期,重新登录请求token");
            throw new SellerAuthorizeException();
        }
        Map<String,Object> map = AdminAccessTokenUtil.getMap(adminToken) ;
        //sellerNumber
        String sellerNumber = (String)map.get("sellerNumber") ;

        boolean isExist = redisUtil.existsKey(sellerNumber);
        if (!isExist){
            //从数据库查
            log.info("已经过期,重新登录请求token");
            throw new SellerAuthorizeException();
        }

        Date lastDate = (Date)map.get("adminExpireTime") ;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00")) ;
        calendar.setTime(lastDate);
        long lastTime = calendar.getTimeInMillis() ;

        //表示已经过期
        if (System.currentTimeMillis() > lastTime){
            log.info("已经过期,重新登录请求token");
            throw new SellerAuthorizeException();
        }

        request.setAttribute("sellerNumber",sellerNumber);
    }
}
