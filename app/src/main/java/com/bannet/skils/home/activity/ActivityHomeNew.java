package com.bannet.skils.home.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bannet.skils.addposting.activity.ActivityAddPosting;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.planlist.activity.ActivityBuyAds;
import com.bannet.skils.chat.activity.ActivityChatList;
import com.bannet.skils.notification.activity.ActivityNotification;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.smarteist.autoimageslider.SliderView;
import com.bannet.skils.Activitys.fragment.FragmentPosting;
import com.bannet.skils.Activitys.fragment.FragmentProfessional;
import com.bannet.skils.Adapter.AdapterViewPager;
import com.bannet.skils.Adapter.AdapterViewPagerStatic;
import com.bannet.skils.Adapter.SliderAdapter;
import com.bannet.skils.profile.activity.ActivityEditProfileScreen;
import com.bannet.skils.home.responce.ViewPageImage;
import com.bannet.skils.Model.WebModel.AdvertismentModel;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bannet.skils.home.responce.profileimagestatusModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHomeNew extends AppCompatActivity {

    ImageView img_chat,img_settings,img_notification,imageHide;
    Context context;
    RelativeLayout realtive_head;


    ViewPager view_pager;
    SliderView sliderView;
    TabLayout tab_layout;
    AdapterViewPager adapterViewPager;
    AdapterViewPagerStatic adapterViewPagerStatic;
    List<ViewPageImage> viewPageImagesstatic;
    int viewPageImages = 0;
    int currentPage = 0;
    LinearLayout layout_professional,layout_posting;
    ImageView floating_plus,float_close;
    RelativeLayout relative_right;
    LinearLayout post_job,contact_new_ads;
    String selected = "1";
    LinearLayout layout_new_post,layout_contact_business;
    FusedLocationProviderClient mFusedLocationClient;
    public String type = "0";
    String aliment = "1";
    SliderAdapter sliderAdapter;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    Dialog builder;
    MediaPlayer mp;
    int PERMISSION_ID = 44;

    CircleImageView profile_img;
    TextView userName;
    String USER_ID;
    public String logitude,latitude;
    ImageView bottom_add_image;
    String imagename,imageurl;
    String Device_token;
    TextView lagitude_view;

    //language
    TextView h1,h2,h3,h4;
    Resources resources;
    public String notificationId="";

    public Location currentLocation;
    public FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = ActivityHomeNew.this;
        getSupportActionBar().hide();

        builder = new Dialog(ActivityHomeNew.this);

        callMultiplePermissions();

        init();
    }
    public  void init(){

        h1=findViewById(R.id.h1);
        h2=findViewById(R.id.h2);
        h3=findViewById(R.id.h3);
        h4=findViewById(R.id.h4);
        changeLanguage();

        h1.setSelected(true);
        h2.setSelected(true);

        profile_img =  findViewById(R.id.user_profile_image_show);
        userName=findViewById(R.id.user_name_show);
        realtive_head =  findViewById(R.id.realtive_head);
        img_chat =  findViewById(R.id.img_chat);
        img_settings =  findViewById(R.id.img_settings);
        img_notification =  findViewById(R.id.img_notification);
        imageHide = findViewById(R.id.imagehide);
        sliderView = findViewById(R.id.view_pager);


        tab_layout =  findViewById(R.id.tab_layout);
        layout_professional =  findViewById(R.id.layout_professional);
        layout_posting =  findViewById(R.id.layout_posting);
        floating_plus =  findViewById(R.id.open_float);
        relative_right =  findViewById(R.id.relative_right);
        float_close =  findViewById(R.id.float_close);
        post_job =  findViewById(R.id.post_job);
        contact_new_ads =  findViewById(R.id.contact_new_ads);
        layout_new_post =  findViewById(R.id.layout_new_post);
        layout_contact_business =  findViewById(R.id.layout_contact_business);
        bottom_add_image =  findViewById(R.id.bottom_add_image);
        int PERMISSION_ID = 44;

        bottom_add_image.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityBuyAds.class);
            startActivity(intent);
        });

        lagitude_view=findViewById(R.id.lagitude_view);

        latitude = PrefConnect.readString(context,PrefConnect.LATITUDE,"");
        logitude = PrefConnect.readString(context,PrefConnect.LOGITUDE,"");


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;

        }
        try {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            } else {

                aliment = extras.getString("type");
                notificationId = extras.getString("notification_id");

                singlenotificationDelete(notificationId);

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {

            if (location != null) {

                currentLocation = location;

                latitude =String.valueOf(currentLocation.getLatitude());
                logitude=String.valueOf(currentLocation.getLongitude());
                lagitude_view.setText(" latitude : "+latitude+" , \n logitude : "+logitude);

            }
        });

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");
        Log.e("USER_ID",USER_ID);
        Device_token=PrefConnect.readString(context,PrefConnect.DEVICE_TOKEN,"");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if(aliment.equals("2")){

            selected = "2";
            layout_professional.setBackgroundResource(R.drawable.rectangle_tw_slected);
            layout_posting.setBackgroundResource(R.drawable.rectangle_tw_unslected);

            replaceFragment(new FragmentPosting());


        }
        else {

            selected = "1";

            layout_professional.setBackgroundResource(R.drawable.rectangle_one_slected);
            layout_posting.setBackgroundResource(R.drawable.rectangle_one_unslected);

            replaceFragment(new FragmentProfessional());

        }

        getAdvertisment();
        getLastLocation();
        userCheck(USER_ID);
        getprofile(USER_ID);

        realtive_head.setOnClickListener(view -> {

        });

        profile_img.setOnClickListener(view -> {

            Intent in = new Intent(context, ActivityEditProfileScreen.class);
            startActivity(in);
            finish();

        });


        post_job.setOnClickListener(view -> {

            relative_right.setVisibility(View.GONE);
            floating_plus.setVisibility(View.VISIBLE);

            Intent in = new Intent(context, ActivityAddPosting.class);
            in.putExtra("type","add_new_post");
            startActivity(in);

        });

        h4.setOnClickListener(v -> {

            relative_right.setVisibility(View.GONE);
            floating_plus.setVisibility(View.VISIBLE);
            Intent in = new Intent(context, ActivityBuyAds.class);
            startActivity(in);

        });
        h3.setOnClickListener(v -> {

            relative_right.setVisibility(View.GONE);
            floating_plus.setVisibility(View.VISIBLE);

            Intent in = new Intent(context, ActivityAddPosting.class);
            in.putExtra("type","add_new_post");
            startActivity(in);

        });

        contact_new_ads.setOnClickListener(view -> {

            relative_right.setVisibility(View.GONE);
            floating_plus.setVisibility(View.VISIBLE);
            Intent in = new Intent(context, ActivityBuyAds.class);
            startActivity(in);

        });

        floating_plus.setOnClickListener(view -> {
            type = "1";
            if(selected.equals("1")){

                relative_right.setVisibility(View.VISIBLE);
                layout_new_post.setVisibility(View.GONE);
                layout_contact_business.setVisibility(View.VISIBLE);
                floating_plus.setVisibility(View.GONE);

            }else {

                relative_right.setVisibility(View.VISIBLE);
                layout_new_post.setVisibility(View.VISIBLE);
                layout_contact_business.setVisibility(View.VISIBLE);
                floating_plus.setVisibility(View.GONE);
            }

        });

        float_close.setOnClickListener(view -> {
            type = "0";
            relative_right.setVisibility(View.GONE);
            floating_plus.setVisibility(View.VISIBLE);

        });

        layout_posting.setOnClickListener(view -> {

            selected = "2";
            layout_professional.setBackgroundResource(R.drawable.rectangle_tw_slected);
            layout_posting.setBackgroundResource(R.drawable.rectangle_tw_unslected);

            replaceFragment(new FragmentPosting());
        });

        layout_professional.setOnClickListener(view -> {

            selected = "1";

            layout_professional.setBackgroundResource(R.drawable.rectangle_one_slected);
            layout_posting.setBackgroundResource(R.drawable.rectangle_one_unslected);

            replaceFragment(new FragmentProfessional());
        });

        img_chat.setOnClickListener(view -> {

            Intent in = new Intent(context, ActivityChatList.class);
            startActivity(in);

        });

        img_notification.setOnClickListener(view -> {

            Intent in = new Intent(context, ActivityNotification.class);
            startActivity(in);

        });



    }


    private void getprofile(String  user_id) {

        if(GlobalMethods.isNetworkAvailable(ActivityHomeNew.this)){

            Api.getClient().getprofile(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<getprofileModel>() {
                @Override
                public void onResponse(@NonNull Call<getprofileModel> call, @NonNull Response<getprofileModel> response) {

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){


                            userName.setText(response.body().getDetails().getFirstName()+" "+response.body().getDetails().getLastName());
                            imagename=response.body().getDetails().getImageName();
                            imageurl=response.body().getDetails().getImageUrl();
                            profile_image_status(imageurl,imagename);

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<getprofileModel> call, @NonNull Throwable t) {


                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        if (checkPermissions()) {

            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {

                            Geocoder geocoder=new Geocoder(ActivityHomeNew.this, Locale.getDefault());

                            try{
                                List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),
                                        1);

                                latitude= String.valueOf(location.getLatitude());
                                logitude= String.valueOf(location.getLongitude());
                                PrefConnect.writeString(context,PrefConnect.LATITUDE,latitude);
                                PrefConnect.writeString(context,PrefConnect.LOGITUDE,logitude);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            latitude= String.valueOf(location.getLatitude());
                            logitude= String.valueOf(location.getLongitude());
                            getAdvertisment();
                            PrefConnect.writeString(context,PrefConnect.LATITUDE,latitude);
                            PrefConnect.writeString(context,PrefConnect.LOGITUDE,logitude);



                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }




    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {


        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude= String.valueOf(mLastLocation.getLatitude());
            logitude= String.valueOf(mLastLocation.getLongitude());

        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }



    private void profile_image_status(String imageurl,String imagename) {
        if(GlobalMethods.isNetworkAvailable(ActivityHomeNew.this)){
            Api.getClient().profileimagestatus(USER_ID,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<profileimagestatusModel>() {
                @Override
                public void onResponse(@NonNull Call<profileimagestatusModel> call, @NonNull Response<profileimagestatusModel> response) {

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            Glide.with(context).load(imageurl+imagename).error(R.drawable.profile_image).into(profile_img);


                        }else {
                            profile_img.setBackgroundResource(R.drawable.profile_image);

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<profileimagestatusModel> call, @NonNull Throwable t) {


                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }

    public void userCheck(String id){

        if(GlobalMethods.isNetworkAvailable(ActivityHomeNew.this)){
            Api.getClient().CheckUser(id).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

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

    public void getAdvertisment(){

        if(GlobalMethods.isNetworkAvailable(ActivityHomeNew.this)){
            Random random = new Random();
            Api.getClient().getAdsList(latitude,logitude,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<AdvertismentModel>() {
                @Override
                public void onResponse(@NonNull Call<AdvertismentModel> call, @NonNull Response<AdvertismentModel> response) {

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            viewPageImages = response.body().getAdsList().size();

                            sliderAdapter = new SliderAdapter(context, response.body().getAdsList());
                            sliderView.setSliderAdapter(sliderAdapter);
//                            sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                            sliderView.startAutoCycle();
//                            calltopImage();

                        }
                        else {

                            getBannerList();

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AdvertismentModel> call, @NonNull Throwable t) {

                }
            });

        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    public void getBannerList(){
        viewPageImagesstatic = new ArrayList<>();
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.textimaagr_electrical));
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.image));
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.textimaagr_electrical));
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.image));

        adapterViewPagerStatic = new AdapterViewPagerStatic (context, viewPageImagesstatic);
        sliderView.setSliderAdapter(adapterViewPagerStatic);
//        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();


    }

    public void singlenotificationDelete(String id){

        if(GlobalMethods.isNetworkAvailable(ActivityHomeNew.this)){
            Api.getClient().singleNotificationDelete(id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {


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

    @Override
    protected void onDestroy() {
        if (builder.isShowing()){
            builder.dismiss();
        }
        if (mp!=null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (builder.isShowing()){
            builder.dismiss();
        }
        if (mp!=null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        if (builder.isShowing()){
            builder.dismiss();
        }
        if (mp!=null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        finish();

       /* if (type.equals("1")){
            type = "0";
            relative_right.setVisibility(View.GONE);
            floating_plus.setVisibility(View.VISIBLE);
        }
        else {
         finish();
        }
*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        init();

    }
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
         fragmentTransaction.replace(R.id.framLayout_home,fragment);
        fragmentTransaction.commit();
    }

    private void callMultiplePermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ EXTERNAL STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE EXTERNAL STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("ACCESS_FINE_LOCATION");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("ACCESS_COARSE_LOCATION");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    // Pre-Marshmallow
                }

                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {
                // Pre-Marshmallow
            }

            return;
        }

    }

    /**
     * add Permissions
     *
     * @param permissionsList
     * @param permission
     * @return
     */
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        } else {
            // Pre-Marshmallow
        }

        return true;
    }

    /**
     * Permissions results
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and others

              /*  perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&*/

                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT)
                            .show();

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void changeLanguage() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            h1.setText(resources.getText(R.string.professiona));
            h2.setText(resources.getText(R.string.postin));
            h3.setText(resources.getText(R.string.add_posting));
            h4.setText(resources.getText(R.string.buy_ads));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            h1.setText(resources.getText(R.string.professiona));
            h2.setText(resources.getText(R.string.postin));
            h3.setText(resources.getText(R.string.add_posting));
            h4.setText(resources.getText(R.string.buy_ads));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            h1.setText(resources.getText(R.string.professiona));
            h2.setText(resources.getText(R.string.postin));
            h3.setText(resources.getText(R.string.add_posting));
            h4.setText(resources.getText(R.string.buy_ads));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();
            h1.setText(resources.getText(R.string.professiona));
            h2.setText(resources.getText(R.string.postin));
            h3.setText(resources.getText(R.string.add_posting));
            h4.setText(resources.getText(R.string.buy_ads));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();
            h1.setText(resources.getText(R.string.professiona));
            h2.setText(resources.getText(R.string.postin));
            h3.setText(resources.getText(R.string.add_posting));
            h4.setText(resources.getText(R.string.buy_ads));
        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();
            h1.setText(resources.getText(R.string.professiona));
            h2.setText(resources.getText(R.string.postin));
            h3.setText(resources.getText(R.string.add_posting));
            h4.setText(resources.getText(R.string.buy_ads));
        }
        else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();
            h1.setText(resources.getText(R.string.professiona));
            h2.setText(resources.getText(R.string.postin));
            h3.setText(resources.getText(R.string.add_posting));
            h4.setText(resources.getText(R.string.buy_ads));

        }
    }
}