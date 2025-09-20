package com.example.myapplication.data.ai.remote;

// data/ai/remote/AiService.java

import com.example.myapplication.data.ai.dto.DetectResponse;
import com.example.myapplication.data.ai.dto.HealthResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * FastAPI 엔드포인트 정의.
 * - 경로/메서드/파라미터가 서버와 정확히 일치해야 함.
 */
public interface AiService {

    /** 건강 체크: GET /health → { "ok": true } */
    @GET("health")
    Call<HealthResponse> health();

    /**
     * YOLO 추론: POST /detect (multipart/form-data)
     * - 파일 파트명은 FastAPI 인자명(File(...))과 동일해야 함: "file"
     * - 응답: DetectResponse(JSON) 파싱
     */
    @Multipart
    @POST("detect")
    Call<DetectResponse> detect(@Part MultipartBody.Part file);
}