package com.sinjee.common;

import com.sinjee.interceptor.AccessLimtInterceptor;
import com.sinjee.interceptor.AccessTokenInterceptor;
import com.sinjee.interceptor.ApiIdempotencyInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 小小极客
 * 时间 2019/12/14 12:54
 * @ClassName BeanConfiguration
 * 描述 配制自定义Bean
 **/
@Configuration
@Slf4j
public class BeanConfiguration implements WebMvcConfigurer {

    @Autowired
    private AccessTokenInterceptor accessTokenInterceptor ;

    @Autowired
    private ApiIdempotencyInterceptor apiIdempotencyInterceptor ;

    @Autowired
    private AccessLimtInterceptor accessLimtInterceptor ;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("添加拦截器={}","accessTokenInterceptor");

        /***
         * 拦截微信小程序请求列表
         */
        registry.addInterceptor(accessTokenInterceptor)
                // addPathPatterns 用于添加拦截规则 ， 先把所有路径都加入拦截， 再一个个排除
                .addPathPatterns("/wechat/order/**","/wechat/pay/**",
                        "/wechat/address/**","/wechat/myforward/**"
                        ,"/wechat/review/**","/wechat/token/**");
        // excludePathPatterns 表示改路径不用拦截
//                .excludePathPatterns("/");
        registry.addInterceptor(accessLimtInterceptor)
                // addPathPatterns 用于添加拦截规则 ， 先把所有路径都加入拦截， 再一个个排除
                .addPathPatterns("/wechat/myforward/saveMyForward","/wechat/buyer/service");
        // excludePathPatterns 表示改路径不用拦截
//                .excludePathPatterns("/");

        registry.addInterceptor(apiIdempotencyInterceptor)
                // addPathPatterns 用于添加拦截规则 ， 先把所有路径都加入拦截， 再一个个排除
                .addPathPatterns("/wechat/order/createOrder");

        /***
         * 拦截中台的过滤器
         */

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
