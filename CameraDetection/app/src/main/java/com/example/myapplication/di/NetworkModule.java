// di/NetworkModule.java
package com.example.myapplication.di;

import com.example.myapplication.AppConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * 네트워크 클라이언트 생성/공유 모듈.
 * - OkHttp: 타임아웃/로깅
 * - Retrofit: Moshi 컨버터
 * - baseUrl: AppConfig에서 주입
 */
public final class NetworkModule {
    private NetworkModule() {}

    // 공통 OkHttp 클라이언트 (로깅/타임아웃 설정)
    private static OkHttpClient client() {
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        // BODY: 요청/응답 전문을 로그로 확인 (디버그에만 권장)
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(log)
                .connectTimeout(20, TimeUnit.SECONDS)  // 서버 연결 대기
                .readTimeout(60, TimeUnit.SECONDS)     // 응답 읽기 대기(추론이 오래 걸릴 수 있음)
                .writeTimeout(60, TimeUnit.SECONDS)    // 업로드 대기
                .build();
    }

    // FastAPI용 Retrofit 인스턴스 (싱글톤)
    private static Retrofit aiRetrofit;

    /**
     * FastAPI 서버와 통신하는 Retrofit.
     * baseUrl은 반드시 '/'로 끝나야 함.
     */
    public static Retrofit ai() {
        if (aiRetrofit == null) {
            aiRetrofit = new Retrofit.Builder()
                    .baseUrl(AppConfig.AI_BASE_URL)            // 예: http://10.0.2.2:8000/
                    .addConverterFactory(MoshiConverterFactory.create()) // JSON ↔ DTO
                    .client(client())
                    .build();
        }
        return aiRetrofit;
    }
}
