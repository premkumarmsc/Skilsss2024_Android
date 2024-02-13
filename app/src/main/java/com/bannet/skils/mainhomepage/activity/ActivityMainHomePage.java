package com.bannet.skils.mainhomepage.activity;

import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.bannet.skils.R;

public class ActivityMainHomePage extends AppCompatActivity {

    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home_page);

        context = ActivityMainHomePage.this;

        init();

    }

    private void init(){



    }


}