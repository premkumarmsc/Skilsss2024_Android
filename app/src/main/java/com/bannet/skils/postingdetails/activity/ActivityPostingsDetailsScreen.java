package com.bannet.skils.postingdetails.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bannet.skils.Adapter.AdapterPostingViewPager;
import com.bannet.skils.Model.PostViewPageImage;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.chat.activity.ActivityChatDetails;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.postingdetails.responce.postingDetailsModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.professinoldetails.responce.professionalDetailsModel;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.SliderView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPostingsDetailsScreen extends AppCompatActivity {

    Context context;
    CardView backbtn;
    TabLayout tab_layout;
    AdapterPostingViewPager adapterViewPager;
    ArrayList<PostViewPageImage> viewPageImages= new ArrayList<>();

    CardView posting_add_fav;
    ImageView favourite,proffImage;
    TextView txt_skills,txt_descripition,txt_title,txt_created_at,postUserName;
    String post_id;
    ProgressDialog progressDialog;
    String USER_ID;
    LinearLayout chat;
    String post_user_id;

    String postImage="",postImageUrl;

    //language
    TextView pd1,pd2,pd3,pd4,postUser;
    public String notification_id="";
    private Boolean fav=false;
    public String type;
    SliderView view_pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postings_screen);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        context= ActivityPostingsDetailsScreen.this;

        init();

    }
    public void init(){

        backbtn=findViewById(R.id.back_btn);
        view_pager =  findViewById(R.id.view_pager);
        tab_layout =  findViewById(R.id.tab_layout);
        posting_add_fav =  findViewById(R.id.add_fav);
        txt_skills =  findViewById(R.id.txt_skills);
        txt_descripition =  findViewById(R.id.txt_descripition);
        txt_title =  findViewById(R.id.txt_title);
        txt_created_at =  findViewById(R.id.txt_created_at);
        chat=findViewById(R.id.chat_btn);
        favourite = findViewById(R.id.fav);
        postUser = findViewById(R.id.post_user);
        proffImage = findViewById(R.id.proff_image);
        postUserName = findViewById(R.id.username);

        pd1=findViewById(R.id.pd1);
        pd2=findViewById(R.id.pd2);
        pd3=findViewById(R.id.pd3);
        pd4=findViewById(R.id.pd4);

        pd1.setText(GlobalMethods.getString(context,R.string.chat));
        pd3.setText(GlobalMethods.getString(context,R.string.skills));
        pd2.setText(GlobalMethods.getString(context,R.string.edi_discription));
        pd4.setText(GlobalMethods.getString(context,R.string.posted_by));


        USER_ID = PrefConnect.readString(context,PrefConnect.USER_ID,"");
        post_id = getIntent().getStringExtra("post_id");
        post_user_id = getIntent().getStringExtra("post_user_id");
        notification_id = getIntent().getStringExtra("notification_id");
        type = getIntent().getStringExtra("type");


        postingDetails(USER_ID,post_id);
        professionalDetails(USER_ID,post_user_id);

        if(USER_ID.equals(post_user_id)){
            chat.setVisibility(View.GONE);
        }

        if(notification_id.equals("")){


        }
        else{

            singlenotificationDelete(notification_id);

        }

        posting_add_fav.setOnClickListener(view -> {

            if(USER_ID.equals("customer")){

                userLogin();
            }
            else {
                if(!fav){

                    callFav(USER_ID,post_id,"add");

                }
                else{

                    callFav(USER_ID,post_id,"edi");

                }

            }

        });

        backbtn.setOnClickListener(view -> finish());

        chat.setOnClickListener(view -> {

            if(USER_ID.equals("customer")){

                userLogin();

            }
            else {

                if(!USER_ID.equals(post_user_id)){

                    Intent in = new Intent(context, ActivityChatDetails.class);
                    in.putExtra("other_user_id",post_user_id);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);

                }

            }

        });
    }

    private void professionalDetails(String user_id, String profe_id) {

        if(GlobalMethods.isNetworkAvailable(ActivityPostingsDetailsScreen.this)){
            Api.getClient().professionalDetails(user_id,profe_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<professionalDetailsModel>() {
                @Override
                public void onResponse(@NonNull Call<professionalDetailsModel> call, @NonNull Response<professionalDetailsModel> response) {

                    Log.e("success", new Gson().toJson(response.body()));

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            postUserName.setText(response.body().getPostDetails().getFirstName());
                            Glide.with(context).load(response.body().getPostDetails().getImageUrl() + response.body().getPostDetails().getImageName()).error(R.drawable.same_profff).into(proffImage);

                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<professionalDetailsModel> call, @NonNull Throwable t) {

                    Log.e("failure res",t.getMessage());

                }
            });
        }else {

            GlobalMethods.Toast(context, "No Internet Connection");

        }

    }
    private void userLogin() {

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_not_logon_dialog);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialoAnimasion;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


        TextView title = dialog.findViewById(R.id.title);
        AppCompatButton login = dialog.findViewById(R.id.login_btn);

        title.setText(GlobalMethods.getString(context,R.string.please_login));
        login.setText(GlobalMethods.getString(context,R.string.go_to_login));

        login.setOnClickListener(view -> {

            Intent intent = new Intent(context, ActivityPhonenumberScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });
    }

    public void singlenotificationDelete(String id){

        if(GlobalMethods.isNetworkAvailable(ActivityPostingsDetailsScreen.this)){

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

    private void postingDetails(String user_id, String post_id) {

        if (GlobalMethods.isNetworkAvailable(context)) {

            progressDialog = ProgressDialog.show(this, "", "Loading...", true);

            Api.getClient().postingDetails(user_id,post_id,"en").enqueue(new Callback<postingDetailsModel>() {

                @Override
                public void onResponse(@NonNull Call<postingDetailsModel> call, @NonNull Response<postingDetailsModel> response) {

                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        assert response.body() != null;

                        Log.e("detail", new Gson().toJson(response.body()));

                        if(response.body().getStatus().equals("1")) {

                            if(response.body().getFavouite().equals("1")){
                                favourite.setImageResource(R.drawable.heart_liked);
                                fav = true;
                            }
                            else {
                                favourite.setImageResource(R.drawable.like);
                                fav = false;
                            }
                            post_user_id=response.body().getPostDetails().getUserId();
                            txt_title.setText(response.body().getPostDetails().getTitle());
                            txt_descripition.setText(response.body().getPostDetails().getDescription());
                            txt_skills.setText(response.body().getPostDetails().getSkillsName());

                            String strDate = response.body().getPostDetails().getCreatedAt();
                            //current date format
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            Date objDate = null;
                            try {
                                objDate = dateFormat.parse(strDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //Expected date format
                            SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd, yyyy");

                            String finalDate = dateFormat2.format(objDate);

                            txt_created_at.setText(finalDate);

                            postImage=response.body().getPostDetails().getPostImages();

                            postImageUrl=response.body().getPostDetails().getPostImageUrl();

                            String[] arrSplit = postImage.split(",");

                            for (String imageName : arrSplit) {

                                String imgurl = postImageUrl;

                                viewPageImages.add(new PostViewPageImage(imageName, imgurl));
                                adapterViewPager = new AdapterPostingViewPager(context, viewPageImages);
                                view_pager.setSliderAdapter(adapterViewPager);
                                view_pager.startAutoCycle();


                            }

                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<postingDetailsModel> call, @NonNull Throwable t) {
                    progressDialog.dismiss();

                    Log.e("failure res",t.getMessage());
                }
            });

        }else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    public void callFav(String user_id,String post_id,String type){
        Log.e("user_id",user_id);
        Log.e("post_id",post_id);

        if(GlobalMethods.isNetworkAvailable(ActivityPostingsDetailsScreen.this)){

            progressDialog = ProgressDialog.show(this, "", "Loading...", true);
            Api.getClient().addOrremoveFavPost(user_id,post_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                    progressDialog.dismiss();
                    Log.e("Favresponse", new Gson().toJson(response.body()));
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){



                            if(!fav){
                                fav = true;
                                favourite.setImageResource(R.drawable.heart_liked);
                            }
                            else{

                                favourite.setImageResource(R.drawable.like);
                                fav = false;
                            }
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {

                    progressDialog.dismiss();

                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

}