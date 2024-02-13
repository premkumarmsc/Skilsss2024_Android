package com.bannet.skils.bottom.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bannet.skils.Activitys.ActivityPlanDetails;
import com.bannet.skils.R;
import com.bannet.skils.chat.Fragment.FragmentChat;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.addposting.activity.ActivityAddPosting;
import com.bannet.skils.base.AppConstants;
import com.bannet.skils.coupon.view.FragmentCoupon;
import com.bannet.skils.explore.fragment.FragmentExplore;
import com.bannet.skils.hire.view.FragmentHire;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.profile.fragment.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityBottom extends AppCompatActivity {

    public Context context;
    public BottomNavigationView nav;
    public static Fragment fragment = null;
    public String userId;
    public String userRegister;
    public ImageView float_close;
    public FloatingActionButton plusBtn;
    RelativeLayout secondlayouthead;
    LinearLayout shopBtn,postbtn;
    RelativeLayout notScrool;
    public boolean select = true;
    public TextView h3,h4;
    public String fragmentStatus = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bottom);

        context = ActivityBottom.this;

        if(getSupportActionBar() != null){

            getSupportActionBar().hide();

        }

        init();

    }

    private void init(){

        userId = PrefConnect.readString(context,PrefConnect.USER_ID,"");
        userRegister = PrefConnect.readString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"");
        nav =  findViewById(R.id.nav_bar);
        plusBtn = findViewById(R.id.fab);
        secondlayouthead = findViewById(R.id.realtive_head);
        shopBtn = findViewById(R.id.contact_new_ads);
        postbtn = findViewById(R.id.post_job);
        float_close = findViewById(R.id.float_close);
        notScrool = findViewById(R.id.bottom_click);
        h3 = findViewById(R.id.h3);
        h4 = findViewById(R.id.h4);

        h3.setText(GlobalMethods.getString(context,R.string.add_posting));
        h4.setText(GlobalMethods.getString(context,R.string.buy_ads));

        displayView(AppConstants.explor);

        plusBtn.setOnClickListener(view -> {

            if(select){


                plusBtn.setImageResource(R.drawable.close_button);
                secondlayouthead.setVisibility(View.VISIBLE);
                notScrool.setVisibility(View.VISIBLE);
                select = false;
                nav.setOnNavigationItemSelectedListener(null); // Disables click event
                nav.setEnabled(false);

            }
            else {
                select = true;
                plusBtn.setImageResource(R.drawable.bottom_center);
                notScrool.setVisibility(View.GONE);
                secondlayouthead.setVisibility(View.GONE);
                nav.setOnNavigationItemSelectedListener(item -> {

                    switch (item.getItemId()) {

                        case R.id.explore:
                            displayView(AppConstants.explor);
                            break;

                        case R.id.chat:

                            displayView(AppConstants.chat);
                            break;


                        case R.id.saved:

                            displayView(AppConstants.saved);
                            break;

                        case R.id.profile:

                            displayView(AppConstants.profile);
                            break;

                        default:
                            displayView(0);
                            break;
                    }

                    return true;
                });


            }

        });


        notScrool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        shopBtn.setOnClickListener(view14 -> {

            if(userRegister.equals("customer")) {
                userLogin();
            }
            else {

                if(!isLocationEnabled(context))

                    showMessageEnabledGPS();

                else{

                    plusBtn.setImageResource(R.drawable.bottom_center);
                    notScrool.setVisibility(View.GONE);
                    secondlayouthead.setVisibility(View.GONE);
                    Intent in = new Intent(context, ActivityPlanDetails.class);
                    startActivity(in);

                }

            }

        });

        postbtn.setOnClickListener(view13 -> {

            if(userRegister.equals("customer")) {
                userLogin();
            }
            else {

                plusBtn.setImageResource(R.drawable.bottom_center);
                notScrool.setVisibility(View.GONE);
                secondlayouthead.setVisibility(View.GONE);
                Intent in = new Intent(context, ActivityAddPosting.class);
                in.putExtra("type","add_new_post");
                startActivity(in);

            }

        });

        float_close.setOnClickListener(view15 -> {



        });


        nav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.explore:
                    displayView(AppConstants.explor);
                    break;

                case R.id.chat:

                    displayView(AppConstants.chat);
                    break;


                case R.id.saved:

                    displayView(AppConstants.saved);
                    break;

                case R.id.profile:

                    displayView(AppConstants.profile);
                    break;

                default:
                    displayView(0);
                    break;
            }

            return true;
        });

    }
//
//    @Override
//    public void onBackPressed() {
//
//        if(select){
//
//            //finish();
//
//        }
//        else {
//
//            select = true;
//            plusBtn.setImageResource(R.drawable.bottom_center);
//            notScrool.setVisibility(View.GONE);
//            secondlayouthead.setVisibility(View.GONE);
//
//        }
//    }

    @Override
    public void onBackPressed() {

        if(select){

            if(fragmentStatus.equals("0")){

                finish();

            }

            else {

                fragmentStatus = "0";

                Intent intent = new Intent(context,ActivityBottom.class);
                startActivity(intent);
                finish();
            }

        }
        else {

            select = true;
            plusBtn.setImageResource(R.drawable.bottom_center);
            notScrool.setVisibility(View.GONE);
            secondlayouthead.setVisibility(View.GONE);

        }
    }
    public void displayView(int position){

        new Handler().post(() -> {

            fragment = null;
            switch (position) {

                case 0:
                    fragmentStatus = "0";
                    fragment = new FragmentExplore();
                    break;
                case 1:
                    if(userRegister.equals("customer")) {
                        userLogin();
                    }
                    else {
                        fragmentStatus = "1";
                        fragment = new FragmentCoupon();
                    }

                    break;
                case 2:

                    if(userRegister.equals("customer")) {
                        userLogin();
                    }
                    else {
                        fragmentStatus = "1";
                        fragment = new FragmentHire();//FragmentHire
                    }

                    break;
                case 3:

                    if(userRegister.equals("customer")) {
                        userLogin();
                    }
                    else {
                        fragmentStatus = "1";
                        fragment = new FragmentProfile();
                    }

                    break;

                default:
                    fragmentStatus = "0";
                    fragment = new FragmentExplore();
                    break;
            }

            openFragment(fragment);
        });
    }

    protected final void  openFragment(final Fragment fragment) {

        if (fragment != null) {

            final FragmentManager fragmentManager;
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.commit();

        }
    }

    private boolean isLocationEnabled(Context context){
//        String locationProviders;
        boolean enabled = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            enabled = (mode != Settings.Secure.LOCATION_MODE_OFF);
        }else{
            LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            enabled =  service.isProviderEnabled(LocationManager.GPS_PROVIDER)||service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return enabled;
    }

    public void showMessageEnabledGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.this_service_requires_the_activation_of_the_gps))
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            nav.setOnNavigationItemSelectedListener(item -> {

                switch (item.getItemId()) {

                    case R.id.explore:
                        displayView(AppConstants.explor);
                        break;

                    case R.id.chat:

                        displayView(AppConstants.chat);
                        break;


                    case R.id.saved:

                        displayView(AppConstants.saved);
                        break;

                    case R.id.profile:

                        displayView(AppConstants.profile);
                        break;

                    default:
                        displayView(0);
                        break;
                }

                return true;
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.explore:
                    displayView(AppConstants.explor);
                    break;

                case R.id.chat:

                    displayView(AppConstants.chat);
                    break;


                case R.id.saved:

                    displayView(AppConstants.saved);
                    break;

                case R.id.profile:

                    displayView(AppConstants.profile);
                    break;

                default:
                    displayView(0);
                    break;
            }

            return true;
        });
    }
}