package com.sinjee.common;

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


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("添加拦截器={}","accessTokenInterceptor");
        registry.addInterceptor(accessTokenInterceptor)
                // addPathPatterns 用于添加拦截规则 ， 先把所有路径都加入拦截， 再一个个排除
                .addPathPatterns("/**");
        // excludePathPatterns 表示改路径不用拦截
//                .excludePathPatterns("/");
        registry.addInterceptor(apiIdempotencyInterceptor)
                // addPathPatterns 用于添加拦截规则 ， 先把所有路径都加入拦截， 再一个个排除
                .addPathPatterns("/**");
        // excludePathPatterns 表示改路径不用拦截
//                .excludePathPatterns("/");

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
