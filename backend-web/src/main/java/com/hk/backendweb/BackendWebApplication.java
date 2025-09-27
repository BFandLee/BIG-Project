package com.hk.backendweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // 1. import 추가
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// 엔티티(@Entity)를 스캔할 패키지 경로를 지정합니다.
@EntityScan(basePackages = {"com.hk.backendweb", "com.hk.backendweb02"})
// 리포지토리(Repository)를 스캔할 패키지 경로
@EnableJpaRepositories(basePackages = {"com.hk.backendweb", "com.hk.backendweb02"})
// 컴포넌트(@Service 등)를 스캔할 패키지 경로
@SpringBootApplication(scanBasePackages = {"com.hk.backendweb", "com.hk.backendweb02"})
public class BackendWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendWebApplication.class, args);
    }

}