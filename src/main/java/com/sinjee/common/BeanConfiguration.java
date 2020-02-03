package com.sinjee.common;

import com.sinjee.interceptor.AccessTokenInterceptor;
import com.sinjee.interceptor.ApiIdempotencyInterceptor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author 小小极客
 * 时间 2019/12/14 12:54
 * @ClassName BeanConfiguration
 * 描述 配制自定义Bean
 **/
@Configurable
public class BeanConfiguration extends WebMvcConfigurationSupport {

    /**
     * 跨域
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 接口幂等性拦截器
        registry.addInterceptor(apiIdempotencyInterceptor());
        registry.addInterceptor(accessTokenInterceptor()) ;
        super.addInterceptors(registry);
    }

    @Bean
    public ApiIdempotencyInterceptor apiIdempotencyInterceptor() {
        return new ApiIdempotencyInterceptor();
    }

    @Bean
    public AccessTokenInterceptor accessTokenInterceptor(){
        return new AccessTokenInterceptor() ;
    }

}
