package com.wjk.tutuo1.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 添加前面的CORS配置代码

        registry.addMapping("/**")
                .allowedOriginPatterns("https://*")
                .allowedMethods("GET", "POST")
                .allowCredentials(true)
                .exposedHeaders("Authorization");
    }
}
