package com.bannet.skils.passwordchange.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.bannet.skils.R;
import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.login.actvity.ActivityLoginScreen;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPasswordChangeScreen extends AppCompatActivity {

    Context context;
    TextView back_btn;
    TextView p_st1,p_st2,p_st3,p_st4;

    EditText newPassword,confirmPassword;

    AppCompatButton submitBtn;

    String phonenumber,USER_ID;
    ProgressDialog progressDialog;

    String  deviceModel="", deviceVersion="", deviceType="", deviceToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change_screen);
        getSupportActionBar().hide();
        context=ActivityPasswordChangeScreen.this;

        back_btn=findViewById(R.id.back_btn);
        newPassword=findViewById(R.id.new_password);
        confirmPassword=findViewById(R.id.confirm_password);
        submitBtn=findViewById(R.id.password_submit_btn);
//        p_st1 = findViewById(R.id.p_s_tv1);
//        p_st2 = findViewById(R.id.p_s_tv2);
        p_st3 = findViewById(R.id.p_s_tv3);
        p_st4 = findViewById(R.id.p_s_tv4);


//        p_st1.setText(GlobalMethods.getString(context,R.string.skilsss));
//        p_st2.setText(GlobalMethods.getString(context,R.string.change_password));
        p_st3.setText(GlobalMethods.getString(context,R.string.new_password));
        p_st4.setText(GlobalMethods.getString(context,R.string.confirm_password));
        newPassword.setHint(GlobalMethods.getString(context,R.string.new_password));
        newPassword.setHint(GlobalMethods.getString(context,R.string.confirm_password));
        back_btn.setText(GlobalMethods.getString(context,R.string.back));

        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityLoginScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        phonenumber=getIntent().getStringExtra("phonenumber");
        USER_ID=getIntent().getStringExtra("user_id");

        submitBtn.setOnClickListener(v -> {
            if(validation()){
                passwordUpdate();
            }
        });


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {
                Log.e("TAG", "Fetching FCM registration token failed", task.getException());
                return;
            }
            // Get new FCM registration token
            String token = task.getResult();
            Log.e("Token", token);
            deviceToken = token+"";

        });

        deviceToken=PrefConnect.readString(context,PrefConnect.DEVICE_TOKEN,"");
        deviceType=android.os.Build.MANUFACTURER+"";
        deviceModel = Build.MODEL+"";
        deviceVersion =  android.os.Build.VERSION.RELEASE+"";

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, ActivityLoginScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void passwordUpdate(){
        if(GlobalMethods.isNetworkAvailable(ActivityPasswordChangeScreen.this)){
            progressDialog = ProgressDialog.show(ActivityPasswordChangeScreen.this, "", "Loading...", true);
            Api.getClient().ForgetPasswordChange(phonenumber,confirmPassword.getText().toString(),confirmPassword.getText().toString(),PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            getProfile();



                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    progressDialog.dismiss();

                    Log.e("failure res",t.getMessage());
                }
            });

        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    private void getProfile(){
        if(GlobalMethods.isNetworkAvailable(ActivityPasswordChangeScreen.this)){
            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            Api.getClient().getprofile(USER_ID,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<getprofileModel>() {
                @Override
                public void onResponse(@NonNull Call<getprofileModel> call, @NonNull Response<getprofileModel> response) {
                    Log.e("chatlistprofile", new Gson().toJson(response.body()));
                    progressDialog.dismiss();

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            PrefConnect.writeString(context,PrefConnect.USER_ID,USER_ID);
                            PrefConnect.writeString(context,PrefConnect.USER_NAME,response.body().getDetails().getFirstName()+" "+response.body().getDetails().getLastName());
                            PrefConnect.writeString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"yes");
                            PrefConnect.writeString(context,PrefConnect.USER_IMAGE_URL,response.body().getDetails().getImageUrl());
                            PrefConnect.writeString(context,PrefConnect.USER_PROFILE_VERIFIED_STATUS,response.body().getDetails().getImageApprove());
                            PrefConnect.writeString(context,PrefConnect.USER_IMAGE_NAME,response.body().getDetails().getImageName());
                            PrefConnect.writeString(context,PrefConnect.DEVICE_TOKEN,deviceToken);
                            PrefConnect.writeString(context,PrefConnect.DEVICE_MODEL,deviceModel);
                            PrefConnect.writeString(context,PrefConnect.DEVICE_VERSION,deviceVersion);
                            PrefConnect.writeString(context,PrefConnect.DEVICE_TYPE,deviceType);
                            PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"en");
                            Intent intent=new Intent(context, ActivityBottom.class);
                            startActivity(intent);
                            finish();

                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<getprofileModel> call, Throwable t) {
                    //  progressDialog.dismiss();

                    Log.e("failure responce",t.getMessage());
                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    private Boolean validation(){

        if(newPassword.getText().toString().isEmpty()){
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Please_Enter_New_Password));
            return false;
        }
        else if(confirmPassword.getText().toString().isEmpty()){
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Please_Enter_Confirm_Password));

            return false;
        }
        else if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Please_Enter_Confirm_Password));
            return false;
        }
        return true;
    }
}