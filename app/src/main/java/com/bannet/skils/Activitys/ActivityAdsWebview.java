package com.bannet.skils.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bannet.skils.R;

import java.util.Objects;

public class ActivityAdsWebview extends AppCompatActivity {

    Context context;
    WebView webView;
    String link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_webview);
        Objects.requireNonNull(getSupportActionBar()).hide();
        context= ActivityAdsWebview.this;

        link=getIntent().getStringExtra("link");

        webView=findViewById(R.id.ads_show_webpage);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(link);



    }
}