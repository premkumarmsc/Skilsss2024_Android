package com.bannet.skils.favorite.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.Adapter.AdapterFavouriteList;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.favorite.responce.FavouriteListModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFavourites extends AppCompatActivity {

    ImageView img_back;
    RecyclerView recycler_fav;
    AdapterFavouriteList adapterFavouriteList;
    List<ProfessionalList> favlist;
    AdapterFavouriteList.ItemClickListener itemClickListener;
    Context context;
    TextView txt_heading;
    ProgressDialog progressDialog;
    String USER_ID,proff_id;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourite);
        getSupportActionBar().hide();
        context = ActivityFavourites.this;

        init();

    }
    public void init(){
        img_back = (ImageView) findViewById(R.id.img_back);
        recycler_fav= (RecyclerView) findViewById(R.id.recycler_chat);
        txt_heading = (TextView) findViewById(R.id.txt_heading);

        changeLanguage();

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        chatListResposne(USER_ID);



        itemClickListener = new AdapterFavouriteList.ItemClickListener() {
            @Override
            public void ItemClick(String position, View layout) {
                proff_id = position;
                callFav(USER_ID,proff_id,layout);
            }
        };

    }

    public void chatListResposne(String user_id){

        favlist = new ArrayList<>();
        if (GlobalMethods.isNetworkAvailable(context)) {


            Api.getClient().fav_professionalsList(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<FavouriteListModel>() {
                @Override
                public void onResponse(Call<FavouriteListModel> call, Response<FavouriteListModel> response) {

                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("1")){


                            adapterFavouriteList = new AdapterFavouriteList(context,itemClickListener,response.body().getPostingList());
                            recycler_fav.setHasFixedSize(true);
                            recycler_fav.setLayoutManager(new LinearLayoutManager(context));
                            recycler_fav.setAdapter(adapterFavouriteList);


                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<FavouriteListModel> call, Throwable t) {

                }
            });

        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }

    public void callFav(String user_id,String prof_id,View layout){
        if(GlobalMethods.isNetworkAvailable(ActivityFavourites.this)){

            Api.getClient().addOrremoveFavProfessional(user_id,prof_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {

                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("1")){
                            layout.setVisibility(View.GONE);
                            GlobalMethods.Toast(context,response.body().getMessage());
                            chatListResposne(USER_ID);
                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }

                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {


                    Log.e("failure res",t.getMessage());
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

            txt_heading.setText(resources.getText(R.string.myFavourites));





        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            txt_heading.setText(resources.getText(R.string.myFavourites));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            txt_heading.setText(resources.getText(R.string.myFavourites));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            txt_heading.setText(resources.getText(R.string.myFavourites));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            txt_heading.setText(resources.getText(R.string.myFavourites));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            txt_heading.setText(resources.getText(R.string.myFavourites));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            txt_heading.setText(resources.getText(R.string.myFavourites));

        }
    }
}
