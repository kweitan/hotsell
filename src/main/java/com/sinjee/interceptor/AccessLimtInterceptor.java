package com.sinjee.interceptor;

import com.sinjee.annotation.AccessLimit;
import com.sinjee.common.KeyUtil;
import com.sinjee.common.RedisUtil;
import com.sinjee.exceptions.LimitAccessException;
import com.sinjee.exceptions.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 创建时间 2020 - 02 -10
 *
 * @author kweitan
 */
@Slf4j
@Component
public class AccessLimtInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (null == accessLimit) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
//            String signal = accessLimit.signal() ;

            String ip = KeyUtil.getIpAddr(request);
            StringBuffer sb = new StringBuffer() ;
            sb.append(request.getContextPath()).append(":").append(request.getServletPath())
                    .append(":").append(ip) ;

            String key = sb.toString() ;
//            if (signal !=null && "".equals(signal)){
//                sb.append(signal) ;
//            }

            long preExpireTime = redisUtil.getExpire(key) ;
            log.info("preExpireTime={}",preExpireTime);
            Object value = redisUtil.getString(key);
            if (null == value) {
                redisUtil.setString(key, 1,seconds);
                return true;
            }
            Integer count = (Integer)value;
            if (count >= maxCount) {
                log.info("请求过于频繁请稍后再试");
                throw new LimitAccessException("请求过于频繁请稍后再试");
            }else {
                redisUtil.increment(key,1);
            }

            long postExpireTime = redisUtil.getExpire(key) ;
            log.info("postExpireTime={}",postExpireTime);
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
