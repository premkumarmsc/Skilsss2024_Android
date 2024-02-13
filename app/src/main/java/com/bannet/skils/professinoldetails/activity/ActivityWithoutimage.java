package com.bannet.skils.professinoldetails.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.chat.activity.ActivityChatDetails;
import com.bannet.skils.databinding.ActivityWithoutimageBinding;
import com.bannet.skils.home.responce.profileimagestatusModel;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.postingdetails.responce.RatingForPostModel;
import com.bannet.skils.professinoldetails.responce.professionalDetailsModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityWithoutimage extends AppCompatActivity implements OnMapReadyCallback {

    public Context context;
    private ProgressDialog progressDialog;
    String pressional_id,simage_url,simageName,type,USER_ID;
    ActivityWithoutimageBinding binding;
    private String userName,longitude,lattitude;

    private Boolean fav=false;
    int myrating;
    public String hireType;
    public final int REQUEST_READ_EXTERNAL_STORAGE = 1;

    public GoogleMap gMap;
    private String Latitude,Logitude,usernumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithoutimageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = ActivityWithoutimage.this;

        if(getSupportActionBar() != null){

            getSupportActionBar().hide();

        }

        init();

    }

    private void init(){

        pressional_id= getIntent().getStringExtra("profe_id");
        simage_url= getIntent().getStringExtra("profileurl");
        simageName= getIntent().getStringExtra("profile_name");
        type= getIntent().getStringExtra("type");
        hireType= getIntent().getStringExtra("typehire");
        USER_ID = PrefConnect.readString(context,PrefConnect.USER_ID,"");

        Log.e("hireType...",hireType);
        professionalDetails(USER_ID,pressional_id);


        if(USER_ID.equals(pressional_id)){

            binding.chatBtn.setVisibility(View.GONE);

        }

        if(hireType.equals("hire")){

            binding.hireModule.setVisibility(View.VISIBLE);

        }
        else {

            binding.hireModule.setVisibility(View.GONE);
        }

        binding.skilssname.setText(GlobalMethods.getString(context,R.string.skills));


        longitude = PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude = PrefConnect.readString(context,PrefConnect.LATITUDE,"");

        binding.chatBtn.setOnClickListener(view -> {

            if(!USER_ID.equals(pressional_id)){

                Intent in = new Intent(context, ActivityChatDetails.class);
                in.putExtra("other_user_id",pressional_id);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);

            }
        });

        binding.backBtn.setOnClickListener(view -> finish());


        binding.favBtn.setOnClickListener(view -> {

            if (USER_ID.equals("customer")){

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

        binding.ratingBtn.setOnClickListener(view -> {

            if(USER_ID.equals("customer")){

                userLogin();

            }
            else {
                ShowBottomRatingbar();
            }

        });

        binding.callBtn.setOnClickListener(view -> {

            if(callisPermissionGrantedFirst()){

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + usernumber));
                startActivity(callIntent);

            }
            else{
                callisPermissionGranted();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {

            mapFragment.getMapAsync(this);
        }

    }

    public boolean callisPermissionGrantedFirst() {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED;
        }
        else {

            return true;
        }
    }

    public boolean callisPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
                return true;
            } else {

                //Toast.makeText(Accueil.this, "Permission is revoked", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(ActivityWithoutimage.this, new String[]{Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CALL_PHONE}, REQUEST_READ_EXTERNAL_STORAGE);
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
    private void professionalDetails(String user_id, String profe_id) {

        if(GlobalMethods.isNetworkAvailable(ActivityWithoutimage.this)){
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

                                binding.imgUnfav.setImageResource(R.drawable.heart_liked);
                                fav = true;

                            }else{

                                binding.imgUnfav.setImageResource(R.drawable.like);
                                fav = false;

                            }
                            if(response.body().getIsrated().equals("1")){

                                binding.ratingBar.setRating(Float.parseFloat(response.body().getPostDetails().getRatings()));
                                binding.ratingBtn.setVisibility(View.GONE);

                            }
                            else{

                                binding.ratingBtn.setVisibility(View.VISIBLE);

                            }
                            usernumber = response.body().getPostDetails().getPhone_no();
                            binding.txtSkils.setText(response.body().getPostDetails().getSkillsName());
                            userName=response.body().getPostDetails().getFirstName() + " " + response.body().getPostDetails().getLastName();
                            binding.username.setText(userName);

                            simage_url=response.body().getPostDetails().getImageUrl();
                            binding.ratingBar.setRating(Float.parseFloat(response.body().getPostDetails().getRatings()));
                            simageName=response.body().getPostDetails().getImageName();

                            profile_image_status(pressional_id);

                            Latitude = response.body().getPostDetails().getLatitude();
                            Logitude = response.body().getPostDetails().getLongitude();

                            double blati = Double.parseDouble(response.body().getPostDetails().getLatitude());
                            double blong = Double.parseDouble(response.body().getPostDetails().getLongitude());
                            double mlati = Double.parseDouble(lattitude);
                            double mlong = Double.parseDouble(longitude);

                            double distance = distance(blati,blong,mlati,mlong);

                            double estimation = milesTokm(distance);

                            DecimalFormat tf = new DecimalFormat();
                            tf.setMaximumFractionDigits(1);
                            String eCalculate = tf.format(estimation);

                            binding.txtKm.setText(eCalculate + "Km Away");

                            LatLng latLng = new LatLng(Double.parseDouble(Latitude), Double.parseDouble(Logitude));

                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("i am here");
                            gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                            gMap.addMarker(markerOptions);


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


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

    }

    private static double milesTokm(double distanceInMiles) {
        return distanceInMiles * 1.60934;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);

    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    
    private void profile_image_status(String user_id) {

        if(GlobalMethods.isNetworkAvailable(ActivityWithoutimage.this)){

            Api.getClient().profileimagestatus(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<profileimagestatusModel>() {
                @Override
                public void onResponse(@NonNull Call<profileimagestatusModel> call, @NonNull Response<profileimagestatusModel> response) {

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){


                            Glide.with(context).load(simage_url+simageName).error(R.drawable.profile_image).into(binding.profileImage);

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

    private void ShowBottomRatingbar() {

        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addrating_bottomsheet);

        TextView ard=dialog.findViewById(R.id.ard1);

        RatingBar ratingBar1=dialog.findViewById(R.id.updateratingBar);
        AppCompatButton ratindAddBtn=dialog.findViewById(R.id.txt_rating_update);

        ard.setText(GlobalMethods.getString(context,R.string.how_s_your_experience_so_far));
        ratindAddBtn.setText(GlobalMethods.getString(context,R.string.add_rating));

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialoAnimasion;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


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

        if(GlobalMethods.isNetworkAvailable(ActivityWithoutimage.this)){

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

                                binding.imgUnfav.setImageResource(R.drawable.heart_liked);
                            }
                            else{

                                binding.imgUnfav.setImageResource(R.drawable.like);
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

        if(GlobalMethods.isNetworkAvailable(ActivityWithoutimage.this)){

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
}