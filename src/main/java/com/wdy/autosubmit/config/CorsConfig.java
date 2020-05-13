package com.wdy.autosubmit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author WDY
 * @Date 2020-05-11 17:28
 * @Description TODO
 */
@Configuration
public class CorsConfig {
    private CorsConfiguration buildConfig(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许跨域访问的域名
        corsConfiguration.addAllowedOrigin("*");
        //请求头
        corsConfiguration.addAllowedHeader("*");
        //请求方法
        corsConfiguration.addAllowedMethod("*");
        //预检请求的有效期，单位秒
        corsConfiguration.setMaxAge(3600L);
        //允许发送cookie
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

}
