package com.agile.framework.autoconfigure;

import com.agile.framework.config.JsonRequestWrapperFilter;
import com.agile.framework.filter.GlobalFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Filter 自动配置类
 *
 * @author chenzhanshang
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnWebApplication
public class WebFilterAutoConfiguration {

    @Bean
    public FilterRegistrationBean<JsonRequestWrapperFilter> filterRegister(){
        FilterRegistrationBean<JsonRequestWrapperFilter> register = new FilterRegistrationBean<>();
        register.setFilter(new JsonRequestWrapperFilter()); //创建并注册TestFilter
        register.addUrlPatterns("*"); //拦截的路径（对所有请求拦截）
        register.setName("JsonRequestWrapperFilter"); //拦截器的名称
        register.setOrder(-200);
        return  register;
    }

    /**
     * 注册 Global Filter
     */
    @Bean
    public FilterRegistrationBean<GlobalFilter> globalFilterFilterRegistrationBean() {
        GlobalFilter globalFilter = new GlobalFilter();
        FilterRegistrationBean<GlobalFilter> registration = new FilterRegistrationBean<>(globalFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(Short.MIN_VALUE);
        return registration;
    }

}
