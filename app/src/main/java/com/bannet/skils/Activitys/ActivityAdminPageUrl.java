package com.bannet.skils.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAdminPageUrl extends AppCompatActivity {

   public Context context;
   public WebView webView;
   public String notificationId;
   public Intent intent;

    private ValueCallback<Uri[]> afterLollipop;
    private ValueCallback<Uri> mUploadMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_url);
        Objects.requireNonNull(getSupportActionBar()).hide();
        context= ActivityAdminPageUrl.this;

        webView=findViewById(R.id.ads_show_webpage);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(EndPoint.ADSPANEL);

        webView.setWebChromeClient(new WebChromeClient() {

            // For Android 3.0+ - undocumented method
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), 101);
                mUploadMessage = uploadMsg;
            }

            // For Android > 4.1 - undocumented method
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 101);

            }

            // For Android > 5.0
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                afterLollipop = filePathCallback;
                startActivityForResult(fileChooserParams.createIntent(), 101);
                return true;

            }

        });

        try {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            } else {
                notificationId = extras.getString("notification_id");

                singlenotificationDelete(notificationId);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void singlenotificationDelete(String id){

        if(GlobalMethods.isNetworkAvailable(ActivityAdminPageUrl.this)){
            Api.getClient().singleNotificationDelete(id, PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {


                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {


                }
            });
        }else {
            GlobalMethods.Toast(context, "No Internet Connection");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case 101:
                if (resultCode == RESULT_OK) {

                    Uri result = intent == null || resultCode != RESULT_OK ? null
                            : intent.getData();
                    if (mUploadMessage != null) {
                        mUploadMessage.onReceiveValue(result);
                    } else if (afterLollipop != null) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            afterLollipop.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                            afterLollipop = null;
                        }
                    }
                    mUploadMessage = null;
                }
        }
    }
}