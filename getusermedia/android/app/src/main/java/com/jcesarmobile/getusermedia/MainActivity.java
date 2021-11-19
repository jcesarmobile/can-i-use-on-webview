package com.jcesarmobile.getusermedia;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewAssetLoader;

import android.Manifest;
import android.os.Bundle;
import android.webkit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private interface PermissionListener {
        void onPermissionSelect(Boolean isGranted);
    }

    private PermissionListener permissionListener;

    ActivityResultCallback<Map<String, Boolean>> permissionCallback = (Map<String, Boolean> isGranted) -> {
        if (permissionListener != null) {
            boolean granted = true;
            for (Map.Entry<String, Boolean> permission : isGranted.entrySet()) {
                if (!permission.getValue()) granted = false;
            }
            permissionListener.onPermissionSelect(granted);
        }
    };
    private ActivityResultLauncher permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissionCallback);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();
        WebView webView = findViewById(R.id.webview);
        WebView.setWebContentsDebuggingEnabled(true);
        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                                         return assetLoader.shouldInterceptRequest(request.getUrl());
                                     }
                                 });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                boolean isRequestPermissionRequired = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M;

                List<String> permissionList = new ArrayList<>();
                if (Arrays.asList(request.getResources()).contains("android.webkit.resource.VIDEO_CAPTURE")) {
                    permissionList.add(Manifest.permission.CAMERA);
                }
                if (Arrays.asList(request.getResources()).contains("android.webkit.resource.AUDIO_CAPTURE")) {
                    permissionList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
                    permissionList.add(Manifest.permission.RECORD_AUDIO);
                }
                if (!permissionList.isEmpty() && isRequestPermissionRequired) {
                    String[] permissions = permissionList.toArray(new String[0]);
                    permissionListener =
                            isGranted -> {
                                if (isGranted) {
                                    request.grant(request.getResources());
                                } else {
                                    request.deny();
                                }
                            };
                    permissionLauncher.launch(permissions);
                } else {
                    request.grant(request.getResources());
                }

            }
        });
        webView.loadUrl("https://appassets.androidplatform.net/assets/www/index.html");
    }
}