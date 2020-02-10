package com.sinjee.interceptor;

import com.sinjee.annotation.AccessLimit;
import com.sinjee.common.KeyUtil;
import com.sinjee.common.RedisUtil;
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

            String ip = KeyUtil.getIpAddr(request);

            String key = request.getContextPath() + ":" + request.getServletPath() + ":" + ip ;

            Integer count = (Integer) redisUtil.getString(key);

            if (null == count || -1 == count) {
                redisUtil.setString(key, 1,seconds);
                return true;
            }

            if (count < maxCount) {
                count++;
                redisUtil.setString(key,count);
                return true;
            }

            if (count >= maxCount) {
//                response 返回 json 请求过于频繁请稍后再试
                log.info("请求过于频繁请稍后再试");
                throw new MyException(201,"请求过于频繁请稍后再试");
            }
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
