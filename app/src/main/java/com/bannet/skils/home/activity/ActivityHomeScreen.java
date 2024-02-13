package com.bannet.skils.home.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bannet.skils.R;

public class ActivityHomeScreen extends AppCompatActivity {

    Context context;
    RelativeLayout settings_btn,fragmentProfesnol,fragmentPosting;
    TextView fragmentProfesnoltestview,fragmentPostingtextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getSupportActionBar().hide();
        context= ActivityHomeScreen.this;



//        settings_btn=findViewById(R.id.settings_btn_homepage);
//        fragmentProfesnol=findViewById(R.id.homescreen_fragment_professional);
//        fragmentPosting=findViewById(R.id.homescreen_fragment_posting);
//        fragmentProfesnoltestview=findViewById(R.id.homepage_textvieww_profectional);
//        fragmentPostingtextview=findViewById(R.id.homepage_textvieww_posting);
//
//        CarouselView carouselView=(CarouselView) findViewById(R.id.carousalview_mainpage);
//        carouselView.setPageCount(3);
//        carouselView.setImageListener(new ImageListener() {
//            @Override
//            public void setImageForPosition(int position, ImageView imageView) {
//                switch (position){
//                    case 0:
//                        imageView.setImageResource(R.drawable.c_image1);
//                        break;
//                    case 1:
//                        imageView.setImageResource(R.drawable.c_image1);
//                        break;
//                    case 2:
//                        imageView.setImageResource(R.drawable.c_image1);
//                        break;
//                }
//            }
//        });


    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        fragmentTransaction.commit();
    }
}