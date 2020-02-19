package com.sinjee.interceptor;

import com.sinjee.admin.service.SellerInfoService;
import com.sinjee.annotation.AdminTokenIdempotency;
import com.sinjee.common.RedisUtil;
import com.sinjee.exceptions.SellerAuthorizeException;
import com.sinjee.wechat.utils.AdminAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author 小小极客
 * 时间 2020/2/3 9:29
 * @ClassName AccessTokenInterceptor
 * 描述 AccessTokenInterceptor 暂时使用AOP
 **/
@Slf4j
@Component
public class AdminTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil ;

    @Autowired
    private SellerInfoService sellerInfoService ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {


        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }

        log.info("进入拦截器AdminTokenInterceptor");

        String adminToken = request.getHeader("adminToken");// 从 http 请求头中取出 adminToken

        log.info("adminToken={}",adminToken);
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();

        //检查是否有AdminTokenIdempotency注释，否跳过认证
        AdminTokenIdempotency adminTokenIdempotency = method.getAnnotation(AdminTokenIdempotency.class) ;
        if (null != adminTokenIdempotency){
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

            Date lastDate = (Date)map.get("expiresTime") ;
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
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
