package com.bannet.skils.notification.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bannet.skils.Adapter.AdapterNotificationList;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.notification.responce.NotificationModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityNotification extends AppCompatActivity {

    RecyclerView recycler_notification;
    ImageView img_back;
    AdapterNotificationList adapterNotificationList;
    AdapterNotificationList.ItemClickListener itemClickListener;
    List<NotificationModel.Notification> notificationLists;
    Context context;
    String user_id;
    ProgressDialog progressDialog;

    //language
    TextView n1;
    Resources resources;
    String USER_ID,notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().hide();
        context = ActivityNotification.this;

        init();
    }
    public void init(){

        n1=findViewById(R.id.n1);

        changeLanguage();

        USER_ID=PrefConnect.readString(context,PrefConnect.USER_ID,"");
        recycler_notification = (RecyclerView) findViewById(R.id.recycler_notification);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        user_id = PrefConnect.readString(context,PrefConnect.USER_ID,"");

        try {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            } else {
                notificationId = extras.getString("notification_id");

                Log.e("card_id",notificationId);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        notificationResponse();
    }

    public void singlenotificationDelete(String id){

        if(GlobalMethods.isNetworkAvailable(ActivityNotification.this)){
            Api.getClient().singleNotificationDelete(id, PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {


                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {


                }
            });
        }
        else {
            GlobalMethods.Toast(context, "No Internet Connection");
        }
    }
   public void notificationResponse(){

        notificationLists = new ArrayList<>();
        if(GlobalMethods.isNetworkAvailable(ActivityNotification.this)){
            progressDialog = ProgressDialog.show(this, "", "Loading...", true);
            Api.getClient().getNotificationList(USER_ID,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    Log.e("sss",new Gson().toJson(response.body()));
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("1")){
                            notificationLists = response.body().getNotificationList();
                            adapterNotificationList = new AdapterNotificationList(context,itemClickListener,notificationLists);
                            recycler_notification.setHasFixedSize(true);
                            recycler_notification.setLayoutManager(new LinearLayoutManager(context));
                            recycler_notification.setAdapter(adapterNotificationList);

                            notificationDelete();

                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {
                    progressDialog.dismiss();
                    GlobalMethods.Toast(context,t.getMessage());

                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }


    public void notificationDelete(){

        notificationLists = new ArrayList<>();
        if(GlobalMethods.isNetworkAvailable(ActivityNotification.this)){
            Api.getClient().deleteNotification(USER_ID,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {


                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    Log.e("sucss",new Gson().toJson(response.body()));


                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {progressDialog.dismiss();
                    GlobalMethods.Toast(context,t.getMessage());

                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }

    private void changeLanguage() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            n1.setText(resources.getText(R.string.notifications));






        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            n1.setText(resources.getText(R.string.notifications));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            n1.setText(resources.getText(R.string.notifications));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            n1.setText(resources.getText(R.string.notifications));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            n1.setText(resources.getText(R.string.notifications));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            n1.setText(resources.getText(R.string.notifications));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            n1.setText(resources.getText(R.string.notifications));

        }
    }
}