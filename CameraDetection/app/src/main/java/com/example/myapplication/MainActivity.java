package com.example.myapplication;

import android.content.Intent;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.core.content.FileProvider;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.viewmodel.FridgeViewModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 기존 MainActivity.kt 로직을 Java로 옮김:
 * - 문짝 클릭 → 회전 애니메이션
 * - ViewModel(Kotlin)로 상태 관리
 * 참고: 원본의 애니메이션 로직/피벗은 그대로 재현. :contentReference[oaicite:2]{index=2}
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FridgeViewModel viewModel;

    // 카메라 촬영 결과를 받을 Uri (FileProvider로 생성)
    private Uri cameraOutputUri;

    /** 갤러리에서 이미지 하나 선택 */
    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    goToDetect(uri);
                }
            });

    /** 카메라 권한 요청 */
    private final ActivityResultLauncher<String> requestCameraPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    launchCamera();
                } else {
                    Toast.makeText(this, "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                }
            });

    /** 카메라 촬영 (풀사이즈) */
    private final ActivityResultLauncher<Uri> takePicture =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success && cameraOutputUri != null) {
                    goToDetect(cameraOutputUri);
                } else {
                    Toast.makeText(this, "촬영이 취소되었습니다", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding inflate
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Kotlin ViewModel을 Java에서 사용
        viewModel = new ViewModelProvider(this).get(FridgeViewModel.class);

        // 상단 문 클릭 → 상태 토글 + 애니메이션
        binding.topDoor.setOnClickListener(v -> {
            Boolean isOpen = safe(viewModel.getTopDoorOpen().getValue());
            animateDoor(binding.topDoor, !isOpen, true);
            viewModel.toggleTop();
        });

        // 중앙 냉장고(중문) 이미지 클릭 → “카메라/갤러리” 선택 다이얼로그
        binding.middleDoor.setOnClickListener(v -> showPickDialog());

        /*
        // 중단 문 클릭 → 상태 토글 + 애니메이션
        binding.middleDoor.setOnClickListener(v -> {
            Boolean isOpen = safe(viewModel.getMiddleDoorOpen().getValue());
            animateDoor(binding.middleDoor, !isOpen, true);
            viewModel.toggleMiddle();
        });
        
         */

        // 하단 문 클릭 → 내부 화면 토글 (보이기/숨기기)
        binding.bottomDoor.setOnClickListener(v -> {
            int vis = binding.fridgeInside.getVisibility();
            binding.fridgeInside.setVisibility(
                    vis == View.GONE ? View.VISIBLE : View.GONE
            );
        });
    }

    /** null-safe Boolean */
    private boolean safe(Boolean b) { return b != null && b; }

    /**
     * 문짝 회전 애니메이션.
     * - pivotY: 수직 중앙
     * - pivotX: 왼쪽(냉장고 경첩) 기준
     * - open=true 이면 0 → -90도, false면 -90 → 0도
     * (원본과 동일한 수치/인터폴레이터) :contentReference[oaicite:3]{index=3}
     */
    private void animateDoor(View view, boolean open, boolean pivotLeft) {
        view.setPivotY(view.getHeight() / 2f);
        view.setPivotX(pivotLeft ? 0f : view.getWidth());

        float start = open ? 0f : -90f;
        float end   = open ? -90f : 0f;

        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotationY", start, end);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    /** 카메라/갤러리 선택 다이얼로그 */
    private void showPickDialog() {
        String[] items = {"갤러리에서 선택", "카메라로 촬영"};
        new AlertDialog.Builder(this)
                .setTitle("이미지 가져오기")
                .setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items),
                        (dialog, which) -> {
                            if (which == 0) {
                                // 갤러리
                                pickImage.launch("image/*");
                            } else {
                                // 카메라
                                requestCameraPermission.launch(android.Manifest.permission.CAMERA);
                            }
                        })
                .setNegativeButton("취소", null)
                .show();
    }

    /** 카메라 앱 실행 (FileProvider로 출력 위치 Uri 준비) */
    private void launchCamera() {
        File imagesDir = new File(getCacheDir(), "images");
        if (!imagesDir.exists()) imagesDir.mkdirs();

        String name = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".jpg";
        File photo = new File(imagesDir, name);

        cameraOutputUri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".fileprovider", // AndroidManifest의 authorities와 동일해야 함
                photo
        );

        takePicture.launch(cameraOutputUri);
    }

    /** DetectActivity로 이동하며 이미지 Uri 전달 */
    private void goToDetect(Uri uri) {
        Intent i = new Intent(this, com.example.myapplication.ui.detect.DetectActivity.class);

        // 핵심: URI를 인텐트의 data로 직접 전달
        i.setData(uri);

        // 권한 전달(ClipData + FLAG)
        i.setClipData(android.content.ClipData.newUri(getContentResolver(), "image", uri));
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // 안전빵: 우리 앱 패키지에 명시적으로 권한 부여
        grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(i);
    }

}

