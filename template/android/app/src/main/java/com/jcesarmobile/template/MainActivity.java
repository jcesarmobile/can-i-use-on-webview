package com.jcesarmobile.template;

import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewAssetLoader;
import android.os.Bundle;
import android.webkit.*;

public class MainActivity extends AppCompatActivity {

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
        webView.loadUrl("https://appassets.androidplatform.net/assets/www/index.html");
    }
}