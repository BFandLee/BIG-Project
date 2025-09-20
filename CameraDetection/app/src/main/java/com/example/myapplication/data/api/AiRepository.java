package com.example.myapplication.data.api;

import com.example.myapplication.data.ai.dto.DetectResponse;
import com.example.myapplication.data.ai.dto.HealthResponse;
import com.example.myapplication.data.ai.remote.AiService;
import com.example.myapplication.di.NetworkModule;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * 네트워크 호출 캡슐화.
 * - UI 단에서는 Repository만 사용하게 하여 교체/테스트 용이
 */
public class AiRepository {

    // NetworkModule에서 생성한 Retrofit으로 서비스 구현체 획득
    private final AiService service = NetworkModule.ai().create(AiService.class);

    /** /health 호출 */
    public Call<HealthResponse> health() {
        return service.health();
    }

    /**
     * /detect 호출
     * @param file 업로드할 이미지 파일 (가능하면 업로드 전 리사이즈/압축 권장)
     */
    public Call<DetectResponse> detectFromFile(File file) {
        // Content-Type: image/jpeg (PNG면 image/png로 변경)
        RequestBody body = RequestBody.create(file, MediaType.parse("image/jpeg"));

        // 반드시 서버 인자명과 동일한 파트명 사용: "file"
        MultipartBody.Part part =
                MultipartBody.Part.createFormData("file", file.getName(), body);

        return service.detect(part);
    }
}