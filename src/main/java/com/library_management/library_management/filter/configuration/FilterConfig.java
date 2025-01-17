package com.library_management.library_management.filter.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.library_management.library_management.filter.UserFIlter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<UserFIlter> jwtFilter(UserFIlter jwtFilter){
        FilterRegistrationBean<UserFIlter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/user/admin/*");
        return registrationBean;
    }
}
