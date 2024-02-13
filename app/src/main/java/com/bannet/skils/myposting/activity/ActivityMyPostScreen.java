package com.bannet.skils.myposting.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bannet.skils.addposting.activity.ActivityAddPosting;
import com.bannet.skils.Adapter.AdapterMyPosingList;
import com.bannet.skils.myposting.responce.MypostingListModel;
import com.bannet.skils.myposting.responce.deletePostModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMyPostScreen extends AppCompatActivity {

    ImageView open_float,img_back;
    Context context;
    AdapterMyPosingList adapterMyPosingList;
    AdapterMyPosingList.ItemClickListener itemClickListener;
    RecyclerView my_posing_list;
    String USER_ID;

    //language
    Resources resources;
    TextView mp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_screen);

        Objects.requireNonNull(getSupportActionBar()).hide();
        context = ActivityMyPostScreen.this;

        init();
    }

    public void init(){
        open_float =  findViewById(R.id.open_float);
        img_back =  findViewById(R.id.img_back);

        mp1=findViewById(R.id.my1);

        changeLanguage();

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");
        my_posing_list=findViewById(R.id.txt_recycler_view);

        img_back.setOnClickListener(view -> finish());

        open_float.setOnClickListener(view -> {

            Intent in = new Intent(context, ActivityAddPosting.class);
            in.putExtra("type","add_new_post");
            startActivity(in);

        });

        mypostingList(USER_ID);

        itemClickListener = (posting, view) -> detetingpost(USER_ID,posting.getId(),view);

    }
    public void mypostingList(String userid){

        if (GlobalMethods.isNetworkAvailable(context)) {

            Api.getClient().mypostingslist(userid,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<MypostingListModel>() {
                @Override
                public void onResponse(@NonNull Call<MypostingListModel> call, @NonNull Response<MypostingListModel> response) {

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            adapterMyPosingList = new AdapterMyPosingList(context,response.body().getPostingList(), itemClickListener);
                            my_posing_list.setHasFixedSize(true);
                            my_posing_list.setLayoutManager(new LinearLayoutManager(context));
                            my_posing_list.setAdapter(adapterMyPosingList);

                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MypostingListModel> call, @NonNull Throwable t) {


                }
            });

        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }

    private void detetingpost(String user_id, String post_id,View view) {

        if (GlobalMethods.isNetworkAvailable(context)) {


            Api.getClient().detetingpost(user_id,post_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<deletePostModel>() {
                @Override
                public void onResponse(@NonNull Call<deletePostModel> call, @NonNull Response<deletePostModel> response) {

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            view.setVisibility(View.GONE);
                            mypostingList(USER_ID);
                            GlobalMethods.Toast(context,response.body().getMessage());


                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<deletePostModel> call, @NonNull Throwable t) {

                    GlobalMethods.Toast(context,t.getMessage());
                    Log.e("failure res",t.getMessage());
                }
            });

        }
        else {

            GlobalMethods.Toast(context, "No Internet Connection");
            
        }
    }

    private void changeLanguage() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.myPostings));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.myPostings));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.myPostings));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.myPostings));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.myPostings));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.myPostings));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.myPostings));

        }
    }
}