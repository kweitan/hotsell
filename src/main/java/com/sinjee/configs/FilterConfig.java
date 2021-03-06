package com.sinjee.configs;

import com.google.common.collect.Maps;
import com.sinjee.filter.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.Map;

/**
 * 创建时间 2020 - 01 -19
 * XSS过滤器配置
 * @author kweitan
 */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssAndSqlFilter");
        registration.setOrder(Integer.MAX_VALUE);
        Map<String, String> initParameters = Maps.newHashMap();
        //-excludes用于配置不需要参数过滤的请求url;
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
        //-isIncludeRichText默认为true，主要用于设置富文本内容是否需要过滤。
        initParameters.put("isIncludeRichText", "false");
        return registration;
    }
}
