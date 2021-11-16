package com.jcesarmobile.geolocation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewAssetLoader;
import android.Manifest;
import android.os.Bundle;
import android.webkit.*;


public class MainActivity extends AppCompatActivity {
    public GeolocationPermissions.Callback callback;
    public String origin;
    ActivityResultLauncher<String> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    callback.invoke(origin, true, false);
                } else {
                    callback.invoke(origin, false, false);
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyWebChromeClient client = new MyWebChromeClient(this);
        setContentView(R.layout.activity_main);
        WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();
        WebView webView = findViewById(R.id.webview);
        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                                         return assetLoader.shouldInterceptRequest(request.getUrl());
                                     }
                                 });
        webView.setWebChromeClient(client);
        webView.loadUrl("https://appassets.androidplatform.net/assets/www/index.html");
    }

    private class MyWebChromeClient extends WebChromeClient {
        private MainActivity myActivity;

        public MyWebChromeClient(MainActivity myActivity) {
            this.myActivity = myActivity;
        }
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            myActivity.callback = callback;
            myActivity.origin = origin;
            locationPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }
}