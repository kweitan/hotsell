package com.sinjee.interceptor;

import com.sinjee.annotation.ApiIdempotency;
import com.sinjee.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author 小小极客
 * 时间 2020/2/1 22:01
 * @ClassName ApiIdempotencyInterceptor
 * 描述 幂等性接口拦截器
 **/
@Slf4j
@Component
public class ApiIdempotencyInterceptor implements HandlerInterceptor{

    @Autowired
    private TokenService tokenService ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入拦截器ApiIdempotencyInterceptor");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        ApiIdempotency apiIdempotency = method.getAnnotation(ApiIdempotency.class);
        if (apiIdempotency != null) {
            check(request);// 幂等性校验, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private void check(HttpServletRequest request){
        tokenService.checkToken(request);
    }
}
