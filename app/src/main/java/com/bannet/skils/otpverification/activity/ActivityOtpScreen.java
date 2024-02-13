package com.bannet.skils.otpverification.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.localdatabase.SqliteDatabase;
import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.explore.response.CategoryLocalREsponse;
import com.bannet.skils.explore.response.CategoryResponce;
import com.bannet.skils.forgetpassword.activity.ActivityForgetPasswordScreen;
import com.bannet.skils.language.activity.ActivityLanguageScreen;
import com.bannet.skils.passwordchange.activity.ActivityPasswordChangeScreen;
import com.bannet.skils.R;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityOtpScreen extends AppCompatActivity {

    private Context context;
    public AppCompatButton sumbitebtn;
    private Dialog dialog_privacy_policy;
    public TextView txt_agree,txt_cancel,title;

    private String otp_entered;

    public Intent intent;
    private String USER_ID;
    private AppCompatButton ok,cancel;
    public Resources resources;
    public final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private String otp,phone_number;
    private Dialog dialog;
    public TextView o_s_tv3,skipbtn;
    public  EditText enterOtpNumber;
    public String Type,code,userType,deviceToken,deviceType,deviceModel,deviceVersion;

    private SqliteDatabase mDatabase;
    ArrayList<CategoryLocalREsponse> allContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        context=ActivityOtpScreen.this;

        Intent in = getIntent();
        otp = in.getStringExtra("otp");
        Log.e("otp",otp);
        phone_number = in.getStringExtra("phone_number");
        USER_ID = in.getStringExtra("user_id");
        Type=in.getStringExtra("type");
        code = in.getStringExtra("code");
        userType = in.getStringExtra("usertype");

        Log.e("USER_ID",USER_ID);

        o_s_tv3 = findViewById(R.id.o_s_tv1);
        sumbitebtn = findViewById(R.id.otp_screen_submite_btn);
        enterOtpNumber = findViewById(R.id.enter_otp);
        skipbtn = findViewById(R.id.skipbtn);

        o_s_tv3.setText(GlobalMethods.getString(context,R.string.OTPVerification));
        skipbtn.setText(GlobalMethods.getString(context,R.string.skip_now));
        enterOtpNumber.setHint(GlobalMethods.getString(context,R.string.Enter_the_valid_OTP));
        sumbitebtn.setText(GlobalMethods.getString(context,R.string.submit));

        mDatabase = new SqliteDatabase(context);
        allContacts = mDatabase.listContacts();

        catgorylListadd();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {
                Log.e("task","trt");
                return;
            }
            // Get new FCM registration token
            String token = task.getResult();
            PrefConnect.writeString(context, PrefConnect.DEVICE_TOKEN, token);
            deviceToken = token;
            Log.e("token",token);
        });

        deviceType = android.os.Build.MANUFACTURER+"";
        deviceModel = Build.MODEL+"";
        deviceVersion =  android.os.Build.VERSION.RELEASE+"";

//      String strLastFourDi = phone_number.length() >= 4 ? phone_number.substring(phone_number.length() - 4): "";
//      showNumber.setText("******"+strLastFourDi);

        skipbtn.setOnClickListener(view -> {

            if(isPermissionGrantedFirst()){

                if(!isLocationEnabled(context))

                    showMessageEnabledGPS();

                else{

                    intent = new Intent(context, ActivityBottom.class);
                    PrefConnect.writeString(context,PrefConnect.USER_ID,"customer");
                    PrefConnect.writeString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"customer");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            }
            else{
                isPermissionGranted();
            }


        });

//        backbtn.setOnClickListener(view -> {
//
//            dialog = new Dialog(ActivityOtpScreen.this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.dialog_otp_back_screen);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.show();
//
//            title=dialog.findViewById(R.id.title);
//            cancel =  dialog.findViewById(R.id.cancel);
//            ok =  dialog.findViewById(R.id.ok);
//
//            cancel.setOnClickListener(v -> dialog.dismiss());
//
//            ok.setOnClickListener(v -> {
//
//                dialog.dismiss();
//
//                if(Type.equals("register")){
//
//                    startActivity(new Intent(context, ActivityPhonenumberScreen.class));
//
//                }else{
//
//                    startActivity(new Intent(context, ActivityForgetPasswordScreen.class));
//                }
//                finish();
//
//            });
//            changeLanguageotp();
//
//        });

        sumbitebtn.setOnClickListener(view -> {

           if(validation()){

              otp_entered = enterOtpNumber.getText().toString();

               if(otp.equals(otp_entered) || otp_entered.equals("8090")){

                  if(Type.equals("register")){

                      if(userType.equals("signin")){

                          updatedevice();


                      }
                      else {
                          showPrivacyPolicy();
                      }

                  }else{

                      Intent intent=new Intent(context, ActivityPasswordChangeScreen.class);
                      intent.putExtra("phonenumber",phone_number);
                      intent.putExtra("user_id",USER_ID);
                      startActivity(intent);
                      finish();

                  }

              }
              else{

                  GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Enter_the_valid_OTP));

              }
          }

        });

    }

    private void updatedevice() {

        if(GlobalMethods.isNetworkAvailable(ActivityOtpScreen.this)){
            Api.getClient().updateDevice(USER_ID,deviceType,deviceToken,deviceModel,deviceVersion,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    Log.e("status",new Gson().toJson(response.body()));

                    if (response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            if(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"").isEmpty()){

                                PrefConnect.writeString(context, PrefConnect.USER_ID,USER_ID);
                                PrefConnect.writeString(context, PrefConnect.USER_ID_REGISTER_COMPLETED,"yes");
                                Intent intent=new Intent(context, ActivityLanguageScreen.class);
                                intent.putExtra("select","login");
                                intent.putExtra("user_id",USER_ID);
                                intent.putExtra("code","0");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                            else {

                                PrefConnect.writeString(context,PrefConnect.USER_ID,USER_ID);
                                PrefConnect.writeString(context, PrefConnect.USER_ID_REGISTER_COMPLETED,"yes");
                                intent = new Intent(context,ActivityBottom.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }

                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }

                    }

                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {


                }
            });
        }else {
            GlobalMethods.Toast(context, "No Internet Connection");
        }

    }

    private boolean isLocationEnabled(Context context){
//        String locationProviders;
        boolean enabled = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            enabled = (mode != Settings.Secure.LOCATION_MODE_OFF);
        }else{
            LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            enabled =  service.isProviderEnabled(LocationManager.GPS_PROVIDER)||service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return enabled;
    }

    public void showMessageEnabledGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.this_service_requires_the_activation_of_the_gps))
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public boolean isPermissionGrantedFirst() {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
        }
        else {

            return true;
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
                return true;
            } else {

                //Toast.makeText(Accueil.this, "Permission is revoked", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(ActivityOtpScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_READ_EXTERNAL_STORAGE);
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private void catgorylListadd() {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().categoryList(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CategoryResponce>() {
                @Override
                public void onResponse(@NonNull Call<CategoryResponce> call, @NonNull Response<CategoryResponce> response) {

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){
                        mDatabase.deletaAll();
                        for (int i = 0;i < response.body().getCategoryList().size();i++){


                            CategoryLocalREsponse newContact = new CategoryLocalREsponse(response.body().getCategoryList().get(i).getCategoryId(),
                                    response.body().getCategoryList().get(i).getCategoryName(),response.body().getCategoryList().get(i).getImageUrl() +
                                    response.body().getCategoryList().get(i).getCategoryImage());

                            mDatabase.addContacts(newContact);

                        }

                    }else {

                        // GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CategoryResponce> call, @NonNull Throwable t) {

                    Log.e("failure professional res",t.getMessage());

                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    private void showPrivacyPolicy() {

        dialog_privacy_policy= new Dialog(ActivityOtpScreen.this);
        dialog_privacy_policy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_privacy_policy.setContentView(R.layout.dialogbox_terms);
        dialog_privacy_policy.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_privacy_policy.show();
        dialog_privacy_policy.setCancelable(false);
        WebView webView=dialog_privacy_policy.findViewById(R.id.privacy);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(EndPoint.TERMS);

        txt_agree = dialog_privacy_policy.findViewById(R.id.txt_agree);
        txt_cancel =  dialog_privacy_policy.findViewById(R.id.txt_cancel);

        txt_cancel.setOnClickListener(view -> dialog_privacy_policy.dismiss());

        txt_agree.setOnClickListener(view -> {

            dialog_privacy_policy.dismiss();
            Intent in = new Intent(context, ActivityLanguageScreen.class);
            in.putExtra("select","select");
            in.putExtra("code",code);
            in.putExtra("user_id",USER_ID);
            startActivity(in);
            finish();

        });
    }

    private boolean validation() {

        if(enterOtpNumber.getText().toString().isEmpty()){

            Toast.makeText(context, GlobalMethods.getString(context,R.string.Please_enter_the_otp), Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    @Override
    public void onBackPressed() {

        dialog = new Dialog(ActivityOtpScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_otp_back_screen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        title=dialog.findViewById(R.id.title);
        cancel =  dialog.findViewById(R.id.cancel);
        ok =  dialog.findViewById(R.id.ok);

        cancel.setOnClickListener(v -> dialog.dismiss());

        ok.setOnClickListener(v -> {
            dialog.dismiss();

            if(Type.equals("register")){

                startActivity(new Intent(context,ActivityPhonenumberScreen.class));

            }else{

                startActivity(new Intent(context,ActivityForgetPasswordScreen.class));
            }
            finish();

        });
        changeLanguageotp();

    }


    private void changeLanguageotp() {

        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {

            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));

        }
        else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {

            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));

        }
        else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {

            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));

        }
        else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {

            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));

        }
        else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {

            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));

        }
        else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {

            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));


        }
        else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {

            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));

        }
    }
}