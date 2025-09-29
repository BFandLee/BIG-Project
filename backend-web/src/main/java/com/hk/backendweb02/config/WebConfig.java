package com.hk.backendweb02.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 1. 모든 API 경로에 대해 CORS 정책 적용
                .allowedOriginPatterns("*") // 2. 모든 출처(Origin)의 요청을 허용 (개발용)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 3. 허용할 HTTP 메서드 명시
                .allowedHeaders("*") // 4. 모든 HTTP 헤더 허용
                .allowCredentials(true) // 5. 자격 증명(쿠키 등) 정보 허용
                .maxAge(3600); // 6. Preflight 요청 캐시 시간(초) 설정
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir);
    }
}