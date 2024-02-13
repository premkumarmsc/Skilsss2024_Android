package com.bannet.skils.forgetpassword.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bannet.skils.login.actvity.ActivityLoginScreen;
import com.bannet.skils.otpverification.activity.ActivityOtpScreen;
import com.hbb20.CountryCodePicker;
import com.bannet.skils.Model.ForgotPasswordModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityForgetPasswordScreen extends AppCompatActivity {

    private Context context;
    public TextView backbtn;
    public AppCompatButton submitbtn;

    public String userMobileNumber,otp,USER_ID;
    public String toNumber="",messageContent="";
    public String phn_number;
    public EditText user_mobile_number;
    private String languageResponce;

    public Resources resources;
    public TextView f_p_s_tv1,f_p_s_tv2,f_p_s_tv3;
    public ProgressDialog progressDialog;
    public CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_screen);
        getSupportActionBar().hide();
        context=ActivityForgetPasswordScreen.this;

//        f_p_s_tv1 = findViewById(R.id.f_p_s_tv1);
//        f_p_s_tv2 = findViewById(R.id.f_p_s_tv2);
        f_p_s_tv3 = findViewById(R.id.f_p_s_tv3);


        user_mobile_number = findViewById(R.id.user_mobile_number);
        backbtn =  findViewById(R.id.back_btn);
        submitbtn = findViewById(R.id.submit_screen_btn);
        countryCodePicker = findViewById(R.id.ccp);

        backbtn.setOnClickListener(view -> startActivity(new Intent(context, ActivityLoginScreen.class)));

//        f_p_s_tv1.setText(GlobalMethods.getString(context,R.string.skilsss));
//        f_p_s_tv2.setText(GlobalMethods.getString(context,R.string.ForgotPassword));
        f_p_s_tv3.setText(GlobalMethods.getString(context,R.string.mobile_number));
        backbtn.setText(GlobalMethods.getString(context,R.string.back));
        user_mobile_number.setHint(GlobalMethods.getString(context,R.string.enter_mobile_number));
        submitbtn.setText(GlobalMethods.getString(context,R.string.next));

        submitbtn.setOnClickListener(view -> {

            if(validation()){

                userForgotPassword();

            }

        });



        languageResponce = PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"");

        if(languageResponce.isEmpty()){

            languageResponce = "en";

        }


    }

    public void userForgotPassword(){
        phn_number =  countryCodePicker.getSelectedCountryCodeWithPlus() + user_mobile_number.getText().toString();

        progressDialog = ProgressDialog.show(ActivityForgetPasswordScreen.this, "", "Loading...", true);
        Api.getClient().ForgotPassword(phn_number,languageResponce).enqueue(new Callback<ForgotPasswordModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgotPasswordModel> call, @NonNull Response<ForgotPasswordModel> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()){

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        otp = response.body().getOtp().getOtp();
                        USER_ID=response.body().getUserId();
                        messageContent="Skilsss App OTP For Change Password - "+ otp;
                        toNumber = phn_number;
                        Intent in = new Intent(context, ActivityOtpScreen.class);
                        in.putExtra("otp",otp);
                        in.putExtra("phone_number",phn_number);
                        in.putExtra("user_id",USER_ID);
                        in.putExtra("type","changePassword");
                        in.putExtra("code","0");
                        startActivity(in);
                        finish();

                        ExecuteTaskMsgSendTwillio gt = new ExecuteTaskMsgSendTwillio();
                        gt.execute(toNumber,messageContent);

                    }else {
                        GlobalMethods.Toast(context,response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForgotPasswordModel> call, @NonNull Throwable t) {

                progressDialog.dismiss();

            }
        });

    }

    class ExecuteTaskMsgSendTwillio extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String toNumber=params[0];
            String messageContent=params[1];

            String res = PostDataMsgSendTwillio(toNumber,messageContent);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.e("PostDataMsgSendTwillio", result + " nan");
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                Log.e("JSONobjectTwillio", jsonObject.toString());
                String error_code = jsonObject.getString("error_code");
                String error_message = jsonObject.getString("error_message");
                //Toast(context, message);
                Log.e("error_code", error_code+" ,error_message: "+error_message);


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSONobjectTwillio", e.getMessage());
            }
        }
    }

    //Submit the answer to server using api
    public static String PostDataMsgSendTwillio(String toNumber,String messageContent) {

        Log.e("toNumber", toNumber + " nan");
        Log.e("messageContent", messageContent + " nan");
        String response = "";
        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "From=+13069881772&Body="+messageContent+"&To="+toNumber);

            //  RequestBody body = RequestBody.create(mediaType, "From=+13069881772&Body="+messageContent+"&To="+toNumber);
            Request request = new Request.Builder()
                    .url("https://api.twilio.com/2010-04-01/Accounts/AC4a3f99546a10166610c1e9bfca75cce4/Messages.json")
                    .method("POST", body)
                    .addHeader("Authorization", "Basic QUM0YTNmOTk1NDZhMTAxNjY2MTBjMWU5YmZjYTc1Y2NlNDoyYjkyMzRjN2VkNWIyZmJmNmFlZjNlNzVmOWI4MDcwZA==")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            response = client.newCall(request).execute().body().string();
            return response;

        } catch (Exception exception) {
        }
        return response;
    }


    public Boolean validation(){

        userMobileNumber=user_mobile_number.getText().toString().trim();

        if(userMobileNumber.isEmpty()){
            Toast.makeText(context,GlobalMethods.getString(context,R.string.enter_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        /*else if (userMobileNumber.length() <=7){
            Toast.makeText(context, "Check the MobileNumber ", Toast.LENGTH_SHORT).show();
            return false;
        }*/

        return true;
    }

}