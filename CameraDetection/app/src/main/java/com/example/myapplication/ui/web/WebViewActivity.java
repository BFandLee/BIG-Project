package com.example.myapplication.ui.web;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView wv;
    private ValueCallback<Uri[]> filePathCallback; // WebView에 돌려줄 결과
    private final ActivityResultLauncher<Intent> pickMediaLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (filePathCallback == null) return;
                Uri[] uris = null;
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    if (data.getClipData() != null) {               // 다중 선택
                        int n = data.getClipData().getItemCount();
                        uris = new Uri[n];
                        for (int i = 0; i < n; i++) {
                            uris[i] = data.getClipData().getItemAt(i).getUri();
                        }
                    } else if (data.getData() != null) {            // 단일 선택
                        uris = new Uri[]{ data.getData() };
                    }
                }
                filePathCallback.onReceiveValue(uris);
                filePathCallback = null;
            });
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        wv = findViewById(R.id.webview);
        WebSettings s = wv.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setLoadsImagesAutomatically(true);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(wv, true);

        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView view,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                WebViewActivity.this.filePathCallback = filePathCallback;

                // accept="image/*" 우선 반영, 없으면 이미지로 제한
                String[] accept = fileChooserParams.getAcceptTypes();
                String mime = (accept != null && accept.length > 0 && accept[0] != null && !accept[0].isEmpty())
                        ? accept[0] : "image/*";

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(mime);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE);

                // 갤러리 앱만으로 한정하고 싶다면 아래 chooser에 타이틀만 주면 됨
                Intent chooser = Intent.createChooser(intent, "이미지 선택");
                pickMediaLauncher.launch(chooser);
                return true; // 우리가 처리
            }
        });
        wv.loadUrl("http://127.0.0.1:5173/");

        // 최신 뒤로가기 처리
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (wv.canGoBack()) {
                    wv.goBack();
                } else {
                    setEnabled(false); // 콜백 비활성화
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }
}

