package com.bannet.skils.chat.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.Adapter.AdapterChatList;
import com.bannet.skils.chat.responce.ChatListModel;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChatList extends AppCompatActivity {

    ImageView img_back;
    RecyclerView recycler_chat;
    AdapterChatList adapterChatList;
    AdapterChatList.ItemClickListener itemClickListener;
    Context context;
    ProgressDialog progressDialog;
    String USER_ID;

    Resources resources;
    TextView cl1;
    String notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Objects.requireNonNull(getSupportActionBar()).hide();
        context = ActivityChatList.this;

        cl1=findViewById(R.id.ch1);

        changeLanguage();
        init();
    }
    public void init(){

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");
        img_back =  findViewById(R.id.img_back);
        recycler_chat=  findViewById(R.id.recycler_chat);

        img_back.setOnClickListener(view -> finish());

        chatListResposne(USER_ID);
        userCheck(USER_ID);


        try {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            } else {
                notificationId = extras.getString("notification_id");

                Log.e("card_id",notificationId);

                singlenotificationDelete(notificationId);

            }
        }
        catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void singlenotificationDelete(String id){

        if(GlobalMethods.isNetworkAvailable(ActivityChatList.this)){
            Api.getClient().singleNotificationDelete(id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {


                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    Log.e("sucss",new Gson().toJson(response.body()));


                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {


                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    public void userCheck(String id){

        if(GlobalMethods.isNetworkAvailable(ActivityChatList.this)){
            Api.getClient().CheckUser(id).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    Log.e("status",new Gson().toJson(response.body()));

                    if (response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){



                        }
                        else {

                            GlobalMethods.Toast(context,"Your Account has been deleted");
                            PrefConnect.writeString(context, PrefConnect.USER_ID, "");
                            PrefConnect.writeString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"");
                            Intent in = new Intent(context, ActivityPhonenumberScreen.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            finish();

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

    @Override
    protected void onResume() {
        super.onResume();
        userCheck(USER_ID);
    }

    public void chatListResposne(String user_id){

        if(GlobalMethods.isNetworkAvailable(ActivityChatList.this)){

            progressDialog = ProgressDialog.show(this, "", "Loading...", true);
            Api.getClient().chatList(user_id,"en").enqueue(new Callback<ChatListModel>() {

                @Override
                public void onResponse(@NonNull Call<ChatListModel> call, @NonNull Response<ChatListModel> response) {

                    progressDialog.dismiss();

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            adapterChatList = new AdapterChatList(context,itemClickListener,response.body().getChatList());
                            recycler_chat.setHasFixedSize(true);
                            recycler_chat.setLayoutManager(new LinearLayoutManager(context));
                            recycler_chat.setAdapter(adapterChatList);
                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ChatListModel> call, @NonNull Throwable t) {


                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }

    private void changeLanguage() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            cl1.setText(resources.getText(R.string.chatList));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            cl1.setText(resources.getText(R.string.chatList));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            cl1.setText(resources.getText(R.string.chatList));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            cl1.setText(resources.getText(R.string.chatList));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            cl1.setText(resources.getText(R.string.chatList));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            cl1.setText(resources.getText(R.string.chatList));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            cl1.setText(resources.getText(R.string.chatList));

        }
    }
}
