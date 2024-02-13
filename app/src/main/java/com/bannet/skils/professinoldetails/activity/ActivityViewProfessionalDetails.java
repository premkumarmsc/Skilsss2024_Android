package com.bannet.skils.professinoldetails.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import com.bannet.skils.chat.activity.ActivityChatDetails;
import com.bannet.skils.chat.activity.ActivityChatList;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bumptech.glide.Glide;
import com.bannet.skils.Adapter.AdapterBannerViewPager;
import com.bannet.skils.home.responce.BannerImage;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.postingdetails.responce.RatingForPostModel;
import com.bannet.skils.professinoldetails.responce.professionalDetailsModel;
import com.bannet.skils.home.responce.profileimagestatusModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewProfessionalDetails extends AppCompatActivity {

    Context context;
    ImageView img_back;
    ImageView img_unfav;
    ImageView img_chat;
    String pressional_id;
    TextView name,skils,txt_time;
    ProgressDialog progressDialog;
    String USER_ID;
    RatingBar ratingBar;
    CircleImageView profile_image;
    String simage_url,simageName;
    AppCompatButton addRating_btn;
    int myrating;
    String post_user_id;
    RelativeLayout chatpost;
    String bannerImage,postImageUrl;
    String ProfileImage;
    ViewPager view_pager;
    int currentPage = 0;
    ArrayList<BannerImage> viewPageImages= new ArrayList<>();
    AdapterBannerViewPager adapterViewPager;

    //language
    TextView pd1,pd2,pd3,ard;
    AppCompatButton ratindAddBtn;
    Resources resources;

    public String userName;
    public String userTime;
    private Boolean fav=false;
    public String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession_details);
        getSupportActionBar().hide();
        context= ActivityViewProfessionalDetails.this;

        init();


    }
    public void init(){

        img_back=findViewById(R.id.img_back);

        pd1=findViewById(R.id.pd1);
        pd2=findViewById(R.id.pd2);
        pd3=findViewById(R.id.pd3);

        img_unfav = findViewById(R.id.img_unfav);
        img_chat = findViewById(R.id.img_chat);
        name=findViewById(R.id.txt_name);
        skils=findViewById(R.id.txt_skils);
        ratingBar = findViewById(R.id.ratingBar);
        txt_time = findViewById(R.id.txt_time);
        profile_image=findViewById(R.id.profile_image);



        pressional_id= getIntent().getStringExtra("profe_id");
        simage_url= getIntent().getStringExtra("profileurl");
        simageName= getIntent().getStringExtra("profile_name");
        type= getIntent().getStringExtra("type");


        USER_ID = PrefConnect.readString(context,PrefConnect.USER_ID,"");

        professionalDetails(USER_ID,pressional_id);



        if(USER_ID.equals(pressional_id)){
            chatpost.setVisibility(View.GONE);

        }
        img_chat.setOnClickListener(view -> {
            Intent in = new Intent(context, ActivityChatList.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        });

        chatpost.setOnClickListener(view -> {

            if(USER_ID.equals("customer")){

                userLogin();

            }
            else {
                if(!USER_ID.equals(pressional_id)){

                    Intent in = new Intent(context, ActivityChatDetails.class);
                    in.putExtra("other_user_id",pressional_id);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                }
            }

        });

        img_back.setOnClickListener(view ->{

//            if(type.equals("1")){
//                Intent in = new Intent(context, ActivityBottom.class);
//                in.putExtra("type","1");
//                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(in);
//                finish();
//            }
//            else {
//                finish();
//            }

            finish();
        });


        img_unfav.setOnClickListener(view -> {

            if(USER_ID.equals("customer")){

                userLogin();
            }
            else {

                if(!fav){
                    callFav(USER_ID,pressional_id,"add");
                    fav = true;

                }
                else{
                    callFav(USER_ID,pressional_id,"edi");
                    fav = false;
                }
            }



        });

        calltopBannerImage();
        changeLanguage();

    }

    @Override
    public void onBackPressed() {
      finish();

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

    private void professionalDetails(String user_id, String profe_id) {

        if(GlobalMethods.isNetworkAvailable(ActivityViewProfessionalDetails.this)){
            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            Api.getClient().professionalDetails(user_id,profe_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<professionalDetailsModel>() {
                @Override
                public void onResponse(@NonNull Call<professionalDetailsModel> call, @NonNull Response<professionalDetailsModel> response) {

                    Log.e("success", new Gson().toJson(response.body()));
                    progressDialog.dismiss();
                    if(response.isSuccessful()){

                        assert response.body() != null;

                        if(response.body().getStatus().equals("1")){

                            if(response.body().getFavouite().equals("1")){

                                img_unfav.setImageResource(R.drawable.heart_liked);
                                fav = true;

                            }else{

                                img_unfav.setImageResource(R.drawable.like);
                                fav = false;

                            }
                            if(response.body().getIsrated().equals("1")){

                                ratingBar.setRating(Float.parseFloat(response.body().getPostDetails().getRatings()));

                            }
                            else{

                                addrating();

                            }

                            post_user_id=response.body().getPostDetails().getId();
                            skils.setText(response.body().getPostDetails().getSkillsName());
                            userName=response.body().getPostDetails().getFirstName() + " " + response.body().getPostDetails().getLastName();
                            name.setText(userName);
                            userTime=response.body().getPostDetails().getAvailableFrom() + "--" + response.body().getPostDetails().getAvailableTo();
                            txt_time.setText(userTime);
                            skils.setText(response.body().getPostDetails().getSkillsName());
                            simage_url=response.body().getPostDetails().getImageUrl();
                            ratingBar.setRating(Float.parseFloat(response.body().getPostDetails().getRatings()));
                            simageName=response.body().getPostDetails().getImageName();
                            ProfileImage=response.body().getPostDetails().getImageName();
                            bannerImage=response.body().getPostDetails().getBannerImage();
                            postImageUrl=response.body().getPostDetails().getImageUrl();

                            profile_image_status(pressional_id);
                            String[] arrSplit = bannerImage.split(",");

                            for (String imageName : arrSplit) {

                                String imgurl = postImageUrl;


                                viewPageImages.add(new BannerImage(imageName, imgurl));
                                adapterViewPager = new AdapterBannerViewPager(context, viewPageImages);
                                view_pager.setAdapter(adapterViewPager);


                            }

                            calltopBannerImage();

                        }else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<professionalDetailsModel> call, @NonNull Throwable t) {

                    progressDialog.dismiss();

                    Log.e("failure res",t.getMessage());

                }
            });
        }else {

            GlobalMethods.Toast(context, "No Internet Connection");

        }

    }

    private void profile_image_status(String user_id) {

        if(GlobalMethods.isNetworkAvailable(ActivityViewProfessionalDetails.this)){

            Api.getClient().profileimagestatus(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<profileimagestatusModel>() {
                @Override
                public void onResponse(@NonNull Call<profileimagestatusModel> call, @NonNull Response<profileimagestatusModel> response) {

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){


                            Glide.with(context).load(simage_url+simageName).error(R.drawable.profile_image).into(profile_image);

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<profileimagestatusModel> call, @NonNull Throwable t) {
                    GlobalMethods.Toast(context,t.getMessage());
                    Log.e("failure responce",t.getMessage());
                }
            });
        }else {
            GlobalMethods.Toast(context, "No Internet Connection");
        }
    }

    private void calltopBannerImage() {

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final Handler handler = new Handler();
        final Runnable Update = () -> {

            if (currentPage == viewPageImages.size()) {
                currentPage = 0;
            }

            try {
                view_pager.setCurrentItem(currentPage++, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000,2000);
    }

    private void addrating() {

        addRating_btn.setVisibility(View.VISIBLE);

        addRating_btn.setOnClickListener(view -> ShowBottomRatingbar());

    }

    private void ShowBottomRatingbar() {

        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addrating_bottomsheet);

        ard=dialog.findViewById(R.id.ard1);
        RatingBar ratingBar1=dialog.findViewById(R.id.updateratingBar);
        ratindAddBtn=dialog.findViewById(R.id.txt_rating_update);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialoAnimasion;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        changeLanguagedialog();

        ratingBar1.setOnRatingBarChangeListener((ratingBar, v, b) -> {

            myrating= (int) ratingBar1.getRating();
            Log.e("traring", String.valueOf(myrating));
        });

        ratindAddBtn.setOnClickListener(view -> {
            addNewRating(USER_ID,pressional_id,myrating);
            dialog.cancel();
        });
    }

    public void callFav(String user_id,String pressional_id,String type){

        if(GlobalMethods.isNetworkAvailable(ActivityViewProfessionalDetails.this)){

            progressDialog = ProgressDialog.show(this, "", "Loading...", true);
            Api.getClient().addOrremoveFavProfessional(user_id,pressional_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                    Log.e("success response", new Gson().toJson(response.body()));
                    progressDialog.dismiss();

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            if(type.equals("add")){

                                img_unfav.setImageResource(R.drawable.heart_liked);
                            }
                            else{

                                img_unfav.setImageResource(R.drawable.like);
                            }


                        }else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {

                    progressDialog.dismiss();
                    Log.e("failure res",t.getMessage());

                }
            });

        }else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    public void addNewRating(String user_id,String proff_id,int myrating){

        if(GlobalMethods.isNetworkAvailable(ActivityViewProfessionalDetails.this)){

            progressDialog = ProgressDialog.show(this, "", "Loading...", true);

             Api.getClient().rateProfessional(user_id,proff_id,myrating,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<RatingForPostModel>() {

                 @Override
                 public void onResponse(@NonNull Call<RatingForPostModel> call, @NonNull Response<RatingForPostModel> response) {

                     Log.e("success response", new Gson().toJson(response.body()));
                     progressDialog.dismiss();

                     if (response.isSuccessful()) {

                         assert response.body() != null;
                         if (response.body().getStatus().equals("1")) {

                             GlobalMethods.Toast(context, response.body().getMessage());
                             finish();

                         }

                     }
                 }

                 @Override
                 public void onFailure(@NonNull Call<RatingForPostModel> call, @NonNull Throwable t) {

                     progressDialog.dismiss();
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

            pd1.setText(resources.getText(R.string.professional_details));
            pd2.setText(resources.getText(R.string.chat));
            addRating_btn.setText(resources.getText(R.string.add_rating));
            pd3.setText(resources.getText(R.string.skills));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.professional_details));
            pd2.setText(resources.getText(R.string.chat));
            addRating_btn.setText(resources.getText(R.string.add_rating));
            pd3.setText(resources.getText(R.string.skills));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.professional_details));
            pd2.setText(resources.getText(R.string.chat));
            addRating_btn.setText(resources.getText(R.string.add_rating));
            pd3.setText(resources.getText(R.string.skills));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.professional_details));
            pd2.setText(resources.getText(R.string.chat));
            addRating_btn.setText(resources.getText(R.string.add_rating));
            pd3.setText(resources.getText(R.string.skills));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.professional_details));
            pd2.setText(resources.getText(R.string.chat));
            addRating_btn.setText(resources.getText(R.string.add_rating));
            pd3.setText(resources.getText(R.string.skills));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.professional_details));
            pd2.setText(resources.getText(R.string.chat));
            addRating_btn.setText(resources.getText(R.string.add_rating));
            pd3.setText(resources.getText(R.string.skills));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.professional_details));
            pd2.setText(resources.getText(R.string.chat));
            addRating_btn.setText(resources.getText(R.string.add_rating));
            pd3.setText(resources.getText(R.string.skills));

        }
    }
    private void changeLanguagedialog() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();


            ard.setText(resources.getText(R.string.add_rating));
            ratindAddBtn.setText(resources.getText(R.string.add_rating));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            ard.setText(resources.getText(R.string.add_rating));
            ratindAddBtn.setText(resources.getText(R.string.add_rating));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            ard.setText(resources.getText(R.string.add_rating));
            ratindAddBtn.setText(resources.getText(R.string.add_rating));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            ard.setText(resources.getText(R.string.add_rating));
            ratindAddBtn.setText(resources.getText(R.string.add_rating));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            ard.setText(resources.getText(R.string.add_rating));
            ratindAddBtn.setText(resources.getText(R.string.add_rating));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            ard.setText(resources.getText(R.string.add_rating));
            ratindAddBtn.setText(resources.getText(R.string.add_rating));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            ard.setText(resources.getText(R.string.add_rating));
            ratindAddBtn.setText(resources.getText(R.string.add_rating));

        }
    }


}