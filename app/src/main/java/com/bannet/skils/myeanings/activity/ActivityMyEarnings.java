package com.bannet.skils.myeanings.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.profile.responce.getprofileModel;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMyEarnings extends AppCompatActivity {

    public Context context;
    public ImageView backBtn;
    public TextView title,balance,edireferCode,withdrawtext,user_name;
    public String referCode;
    public LinearLayout withdrawBtn;
    private String user_id;
    String userBalance;
    private EditText upiId;
    public AppCompatButton referBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_earnings);

        context = ActivityMyEarnings.this;
        getSupportActionBar().hide();

        init();

    }

    private void init(){

        user_id = PrefConnect.readString(context,PrefConnect.USER_ID,"");

        getprofile(user_id);

        title = findViewById(R.id.txt_heading);
        withdrawBtn = findViewById(R.id.request_btn);
        balance = findViewById(R.id.balance);
        upiId = findViewById(R.id.edi_id);
        backBtn = findViewById(R.id.img_back);
        edireferCode = findViewById(R.id.code);
        referBtn = findViewById(R.id.refer_btn);
        withdrawtext = findViewById(R.id.withdrawtext);
        user_name = findViewById(R.id.user_name);

        title.setText(GlobalMethods.getString(context,R.string.my_earnings));
        withdrawtext.setText(GlobalMethods.getString(context,R.string.withdraw_request));

        backBtn.setOnClickListener(view -> finish());

        withdrawBtn.setOnClickListener(view -> {

            String upiID = upiId.getText().toString();
            if(validation(upiID)){

                double uBalnce = Double.parseDouble(userBalance);
                if(uBalnce >= 250){

                    withDrawRequest(upiID);

                }
                else {

                    GlobalMethods.Toast(context,GlobalMethods.getString(context,R.string.Minimum_amount));

                }

            }

        });

        referBtn.setOnClickListener(view -> {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, EndPoint.PLAYSTORE_URL + "\n Referral code : "+ referCode);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        });
    }

    private boolean validation(String userID) {

        if(userID.isEmpty()){

            GlobalMethods.Toast(context,"Please enter your UPI or Paypal Id");
            return false;

        }
        else if(userBalance.equals("0")){

            GlobalMethods.Toast(context,"Not enough balance to withdraw");
            return false;

        }


        return true;

    }

    private void withDrawRequest(String upiID) {

            if(GlobalMethods.isNetworkAvailable(ActivityMyEarnings.this)){
                Api.getClient().withdrawRequest(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""),upiID).enqueue(new Callback<CommonModel>() {

                    @Override
                    public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                        Log.e("status",new Gson().toJson(response.body()));

                        if (response.isSuccessful()){

                            assert response.body() != null;
                            if(response.body().getStatus().equals("1")){

                                GlobalMethods.Toast(context,response.body().getMessage());

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

    private void getprofile(String user_id) {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().getprofile(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<getprofileModel>() {
                @Override
                public void onResponse(@NonNull Call<getprofileModel> call, @NonNull Response<getprofileModel> response) {

                    Log.e("responce",new Gson().toJson(response.body()));
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            userBalance = response.body().getDetails().getEarnings();
                            user_name.setText(response.body().getDetails().getFirstName() + response.body().getDetails().getLastName());
                            referCode = response.body().getDetails().getReferalCode();

                            edireferCode.setText(referCode);

                            if(userBalance.isEmpty()){

                                balance.setText(": 0 Rs");

                            }
                            else {

                                balance.setText(": " + userBalance + " Rs");

                            }

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<getprofileModel> call, @NonNull Throwable t) {
                    //  progressDialog.dismiss();

                    Log.e("failure responce",t.getMessage());
                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }
}