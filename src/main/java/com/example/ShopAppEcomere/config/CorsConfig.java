package com.example.ShopAppEcomere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedOriginPatterns("*")  // Thay vì allowedOrigins("*")
                        .allowCredentials(true)      // Nếu FE gửi cookie
                        .allowedHeaders("*");        // Cho phép tất cả headers
            }
        };
    }
}
