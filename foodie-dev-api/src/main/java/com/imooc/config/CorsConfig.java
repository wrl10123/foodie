package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    public CorsConfig() {
    }

    /**
     * 解决跨越问题
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        //1. 添加cors配置信息
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");

        //设置是否发送cookie信息
        configuration.setAllowCredentials(true);
        //设置允许请求的方法：GET、POST等等
        configuration.addAllowedMethod("*");
        //设置允许的header
        configuration.addAllowedHeader("*");

        //2.为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(corsSource);
    }

}
