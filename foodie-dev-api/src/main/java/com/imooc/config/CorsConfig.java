package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    public CorsConfig(){

    }
    @Bean
    public CorsFilter corsFilter(){
        //1.添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOrigin("http://localhost:8080");
//        config.addAllowedOrigin("http://101.42.168.101:8080");
//        config.addAllowedOrigin("http://192.168.200.128:90");
//        config.addAllowedOrigin("http://yinchuan.work:8080");
        config.addAllowedOrigin("*");

        //设置是否发送cookie信息
        config.setAllowCredentials(true);
        //设置允许请求的方式(get/post)
        config.addAllowedMethod("*");
        //请后端交互有些信息是放在header中的
        config.addAllowedHeader("*");
        //为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**",config);
        return new CorsFilter(corsSource);
    }

}
