// ui/detect/DetectActivity.java
package com.example.myapplication.ui.detect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.ai.AiRepository;
import com.example.myapplication.data.ai.dto.DetectResponse;
import com.example.myapplication.data.ai.dto.HealthResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 최소 동작 예:
 * 1) /health 토스트 확인
 * 2) (외부에서) 이미지 URI를 인텐트 data로 받아서 /detect 호출
 * 3) ingredients 토스트 확인
 */
public class DetectActivity extends AppCompatActivity {

    private final AiRepository aiRepo = new AiRepository();

    // (선택) 이 화면에서 직접 갤러리 띄우고 싶을 때만 사용
    private ActivityResultLauncher<String> pick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) 서버 상태 확인: /health  (제네릭 타입 명시!)
        aiRepo.health().enqueue(new Callback<HealthResponse>() {
            @Override public void onResponse(Call<HealthResponse> call, Response<HealthResponse> resp) {
                Toast.makeText(DetectActivity.this,
                        "health: " + (resp.isSuccessful()),
                        Toast.LENGTH_SHORT).show();
            }
            @Override public void onFailure(Call<HealthResponse> call, Throwable t) {
                Toast.makeText(DetectActivity.this, "health 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 2) (선택) 이 화면에서 직접 갤러리 선택을 하고 싶다면 사용
        pick = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                this::onPicked
        );
        // 예) 버튼에서: findViewById(R.id.btnPick).setOnClickListener(v -> pick.launch("image/*"));

        // 3) MainActivity에서 넘어온 URI를 인텐트 data로 수신해 바로 처리
        handleIncomingImage();
    }

    /** MainActivity에서 전달된 이미지를 처리 (Intent data에서 수신) */
    private void handleIncomingImage() {
        Intent intent = getIntent();
        Uri uri = (intent != null) ? intent.getData() : null;

        if (uri == null) {
            // data로 안 받았다면 이전 방식의 extra로도 한 번 더 시도 (호환)
            String s = (intent != null) ? intent.getStringExtra("imageUri") : null;
            if (s != null) {
                try { uri = Uri.parse(s); } catch (Exception ignored) {}
            }
        }

        if (uri == null) {
            // 이 화면에서 직접 고르고 싶다면 아래 주석 해제해서 사용
            // pick.launch("image/*");
            return; // 이미지 없으면 아무 것도 하지 않음
        }

        // ACTION_OPEN_DOCUMENT일 경우에만 persistable 권한이 가능하지만, 시도해도 무해
        try {
            getContentResolver().takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            );
        } catch (Exception ignored) {}

        // 캐시로 복사 → /detect 호출
        uploadForDetect(uri);
    }

    /** 갤러리에서 선택된 Uri 수신 (이 화면에서 직접 선택했을 때용) */
    private void onPicked(Uri uri) {
        if (uri == null) return;
        uploadForDetect(uri);
    }

    /** Uri → 캐시 파일 복사 후 /detect 호출 */
    private void uploadForDetect(Uri uri) {
        File f;
        try {
            f = copyUriToCache(uri, "upload.jpg");
        } catch (SecurityException se) {
            Toast.makeText(this, "URI 권한 오류: " + se.getMessage(), Toast.LENGTH_LONG).show();
            return;
        } catch (Exception e) {
            Toast.makeText(this, "파일 준비 실패: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        aiRepo.detectFromFile(f).enqueue(new Callback<DetectResponse>() {
            @Override public void onResponse(Call<DetectResponse> call, Response<DetectResponse> resp) {
                if (!resp.isSuccessful() || resp.body() == null) {
                    Toast.makeText(DetectActivity.this,
                            "detect 실패: " + resp.code(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(DetectActivity.this,
                        "ingredients: " + resp.body().ingredients,
                        Toast.LENGTH_LONG).show();
                // TODO: resp.body().detections로 UI 반영
            }

            @Override public void onFailure(Call<DetectResponse> call, Throwable t) {
                Toast.makeText(DetectActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Uri 콘텐츠를 앱 캐시 디렉토리로 복사.
     * - 외부 저장소 경로 접근 이슈 없이 Multipart 업로드 가능
     */
    private File copyUriToCache(Uri uri, String name) throws Exception {
        File out = new File(getCacheDir(), name);
        try (InputStream in = getContentResolver().openInputStream(uri);
             FileOutputStream fos = new FileOutputStream(out)) {
            if (in == null) throw new Exception("InputStream is null");
            byte[] buf = new byte[8192];
            int r;
            while ((r = in.read(buf)) != -1) {
                fos.write(buf, 0, r);
            }
        }
        return out;
    }
}
