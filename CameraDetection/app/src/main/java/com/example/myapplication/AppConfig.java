package com.example.myapplication;


/**
 * BuildConfig 값을 한군데로 모아두는 래퍼.
 * - 다른 모듈/클래스에서 BuildConfig 직접 import 문제를 회피
 * - 추후 게이트웨이로 전환할 때도 여기만 바꾸면 됨
 */
public final class AppConfig {
    private AppConfig() {}
    public static final String AI_BASE_URL = BuildConfig.AI_BASE_URL;   // FastAPI
    public static final String API_BASE_URL = BuildConfig.API_BASE_URL; // Spring Boot
}
