package com.bannet.skils.professinoldetails.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bannet.skils.R;


public class ActivityProfileImageShow extends AppCompatActivity {

    String imagename,imageurl;

    ImageView imageView;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image_show);
        getSupportActionBar().hide();
        context=ActivityProfileImageShow.this;


//        ActivityTransition.with(getIntent()).to(findViewById(R.id.sub_imageView)).start(savedInstanceState);


        imageView=findViewById(R.id.sub_imageView);
        imageurl=getIntent().getStringExtra("imageurl");
        imagename=getIntent().getStringExtra("imagename");

        Glide.with(context).load(imageurl+imagename).into(imageView);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();

    }
}