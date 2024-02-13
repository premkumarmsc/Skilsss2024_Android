package com.bannet.skils.terms.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bannet.skils.R;
import com.bannet.skils.service.EndPoint;

public class ActivityTerms extends AppCompatActivity {


    Context context;
    WebView webView;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms2);
        context=ActivityTerms.this;
        getSupportActionBar().hide();



        webView=findViewById(R.id.terms_page);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(EndPoint.TERMS);

    }
}