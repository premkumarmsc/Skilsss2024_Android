package com.bannet.skils.privacy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bannet.skils.R;
import com.bannet.skils.service.EndPoint;

public class ActivityPrivacy extends AppCompatActivity {

    Context context;
    WebView webView;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        context=ActivityPrivacy.this;
        getSupportActionBar().hide();


        webView=findViewById(R.id.privacy_page);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(EndPoint.PRIVACY);

    }
}