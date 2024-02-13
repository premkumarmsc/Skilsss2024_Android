package com.bannet.skils.login.actvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.forgetpassword.activity.ActivityForgetPasswordScreen;
import com.bannet.skils.language.activity.ActivityLanguageScreen;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.bannet.skils.login.responce.LoginModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityLoginScreen extends AppCompatActivity{

    public AppCompatButton loginbtn;
    public Intent intent;
    public TextView newRegisterbtn;
    public TextView forgetpassword;
    private Context context;
    private Dialog dialog_privacy_policy;
    public TextView txt_agree,txt_cancel,skipBtn;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public String mobilenumber,password;
    private EditText enterMobileNumber,enterPassword;
    private String languageResponce;
    public final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    int PERMISSION_ID = 44;
    public boolean agree=false;

    public TextView l_s_tv1,l_s_tv2,l_s_tv3,l_s_tv4,l_s_tv5;
    public Resources resources;
    private ProgressDialog progressDialog;
    private String  deviceModel, deviceVersion, deviceType, deviceToken = "";
    private SwitchCompat switch_terms;
    private Dialog dialog_terms_check;
    private CountryCodePicker countryCodePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);
        getSupportActionBar().hide();
        FirebaseApp.initializeApp(this);
        context = ActivityLoginScreen.this;

        countryCodePicker = findViewById(R.id.ccp);

        callMultiplePermissions();

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

        enterMobileNumber = findViewById(R.id.enter_user_mobile_number);
        enterPassword = findViewById(R.id.enter_user_password);

        languageResponce = PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"");

        if(languageResponce.isEmpty()){

            languageResponce = "en";

        }

        l_s_tv1 = findViewById(R.id.p_s_tv5);
        l_s_tv2 = findViewById(R.id.p_s_tv6);
        l_s_tv5 = findViewById(R.id.p_s_tv7);
        skipBtn = findViewById(R.id.skipbtn);


        loginbtn =  findViewById(R.id.user_login_button);
        newRegisterbtn = (TextView)findViewById(R.id.new_registeration_btn);
        forgetpassword = (TextView)findViewById(R.id.forget_password);


        l_s_tv1.setText(GlobalMethods.getString(context,R.string.mobile_number));
        l_s_tv2.setText(GlobalMethods.getString(context,R.string.password));
//        l_s_tv3.setText(GlobalMethods.getString(context,R.string.mobile_number));
//        l_s_tv4.setText(GlobalMethods.getString(context,R.string.password));
        l_s_tv5.setText(GlobalMethods.getString(context,R.string.donthave_account));
        enterMobileNumber.setHint(GlobalMethods.getString(context,R.string.enter_mobile_number));
        enterPassword.setHint(GlobalMethods.getString(context,R.string.password));
        skipBtn.setText(GlobalMethods.getString(context,R.string.skip_now));
        forgetpassword.setText(GlobalMethods.getString(context,R.string.forgot_password));
        newRegisterbtn.setText(GlobalMethods.getString(context,R.string.create_now));
        loginbtn.setText(GlobalMethods.getString(context,R.string.login));

        newRegisterbtn.setOnClickListener(view -> {

                dialog_terms_check = new Dialog(ActivityLoginScreen.this);
                dialog_terms_check.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_terms_check.setContentView(R.layout.dialogbox_agree_terms);
                dialog_terms_check.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog_terms_check.show();
                dialog_terms_check.setCancelable(true);

                AppCompatButton check = dialog_terms_check.findViewById(R.id.check_btn);
                AppCompatButton cancel = dialog_terms_check.findViewById(R.id.cancel_btn);

                check.setOnClickListener(v -> {

                    dialog_terms_check.dismiss();

                    Intent intent=new Intent(context, ActivityPhonenumberScreen.class);
                    startActivity(intent);

                });

                cancel.setOnClickListener(v -> dialog_terms_check.dismiss());
               /* dialog_terms_check = new Dialog(ActivityLoginScreen.this);
                dialog_terms_check.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_terms_check.setContentView(R.layout.dialogbox_agree_terms);
                dialog_terms_check.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog_terms_check.show();
                dialog_terms_check.setCancelable(true);*/

        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermissionGrantedFirst()){

                    if(!isLocationEnabled(context))

                        showMessageEnabledGPS();

                    else{

                        intent = new Intent(context, ActivityBottom.class);
                        PrefConnect.writeString(context,PrefConnect.USER_ID,"customer");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                }
                else{
                    isPermissionGranted();
                }
            }
        });

        loginbtn.setOnClickListener(view -> {

            if(validation()){

                if(isPermissionGrantedFirst()){

                    if(!isLocationEnabled(context))

                        showMessageEnabledGPS();

                    else{

                        userLogin();

                    }

                }
                else{

                    isPermissionGranted();

                }

            }

        });

        forgetpassword.setOnClickListener(view -> startActivity(new Intent(context, ActivityForgetPasswordScreen.class)));


    }

    private void userLogin() {

        Log.e("deviceToken",deviceToken);
        if (GlobalMethods.isNetworkAvailable(context)) {

            String phn_number =  countryCodePicker.getSelectedCountryCodeWithPlus() + enterMobileNumber.getText().toString();

            progressDialog = ProgressDialog.show(ActivityLoginScreen.this, "", "Loading...", true);
            Api.getClient().userLogin(phn_number,password,deviceToken,deviceModel,deviceVersion,deviceType,languageResponce).enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(@NonNull Call<LoginModel> call, @NonNull Response<LoginModel> response) {

                    Log.d("login",new Gson().toJson(response.body()));
                    progressDialog.dismiss();

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            PrefConnect.writeString(context, PrefConnect.USER_ID, response.body().getDetails().getId());
                            PrefConnect.writeString(context, PrefConnect.USER_ID_REGISTER_COMPLETED,"yes");
                            PrefConnect.writeString(context, PrefConnect.USER_NAME, response.body().getDetails().getFirstName() +" "+response.body().getDetails().getLastName());
                            PrefConnect.writeString(context, PrefConnect.USER_Mobilenumber, response.body().getDetails().getPhoneNo());
                            PrefConnect.writeString(context, PrefConnect.USER_PROFILE_VERIFIED_STATUS,response.body().getDetails().getImageApprove());
                            PrefConnect.writeString(context, PrefConnect.USER_IMAGE_URL,response.body().getDetails().getImageUrl()+response.body().getDetails().getImageName());

                            int language=PrefConnect.readInteger(context,PrefConnect.LANGUAGE,0);

                            if(language==0){

                                Intent in=new Intent(context, ActivityLanguageScreen.class);
                                in.putExtra("select","login");
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();

                            }else{

                                Intent in=new Intent(context, ActivityBottom.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();

                            }

                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginModel> call, @NonNull Throwable t) {

                    progressDialog.dismiss();
                    Log.e("login",t.getMessage());

                }
            });

        }else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
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
                ActivityCompat.requestPermissions(ActivityLoginScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
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

    private void showPrivacyPolicy() {

        dialog_privacy_policy= new Dialog(ActivityLoginScreen.this);
        dialog_privacy_policy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_privacy_policy.setContentView(R.layout.dialogbox_terms);
        dialog_privacy_policy.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_privacy_policy.show();
        dialog_privacy_policy.setCancelable(false);
        WebView webView=dialog_privacy_policy.findViewById(R.id.privacy);
        txt_agree=(TextView) dialog_privacy_policy.findViewById(R.id.txt_agree);
        txt_cancel = (TextView) dialog_privacy_policy.findViewById(R.id.txt_cancel);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(EndPoint.TERMS);

        txt_cancel.setOnClickListener(view -> {

            dialog_privacy_policy.dismiss();
            switch_terms.setChecked(false);

        });
        txt_agree.setOnClickListener(view -> {

            dialog_privacy_policy.dismiss();
            switch_terms.setChecked(true);

        });
    }

    private boolean validation(){

        mobilenumber=enterMobileNumber.getText().toString().trim();
        password=enterPassword.getText().toString().trim();

        if(mobilenumber.isEmpty()){
            Toast.makeText(context,GlobalMethods.getString(context,R.string.enter_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (password.isEmpty()){
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Enter_the_password));

            return false;
        }

        return true;
    }


    private void callMultiplePermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ EXTERNAL STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE EXTERNAL STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("ACCESS_FINE_LOCATION");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("ACCESS_COARSE_LOCATION");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    // Pre-Marshmallow
                }

                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {
                // Pre-Marshmallow
            }

            return;
        }

    }

    /**
     * add Permissions
     *
     * @param permissionsList
     * @param permission
     * @return
     */
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        } else {
            // Pre-Marshmallow
        }

        return true;
    }

    /**
     * Permissions results
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and others

              /*  perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&*/

                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Permissin_is_denied));

                }

                if (requestCode == PERMISSION_ID) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //   getLastLocation();
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



}