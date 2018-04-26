package com.iflytek.config;

import com.iflytek.filter.RateLimiterFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Value("${application.tps}")
    private String tps;//tps

    @Value("${application.drop}")
    private String isDrop;//超流是否处理

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RateLimiterFilter());//注册rewrite过滤器
        registration.addUrlPatterns("/iflytek/api/suggestion/*");
        registration.addInitParameter(RateLimiterFilter.TPS,this.tps);
        registration.addInitParameter(RateLimiterFilter.IS_DROP, this.isDrop);
        registration.setName("rateLimiterFilter");
        registration.setOrder(1);
        return registration;
    }

}
