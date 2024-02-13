package com.bannet.skils.introduction.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bannet.skils.R;
import com.bannet.skils.introduction.adapter.AdapterIntroduction;
import com.bannet.skils.introduction.model.ModelIntroductionImage;
import com.bannet.skils.login.actvity.ActivityLoginScreen;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;

import java.util.ArrayList;
import java.util.List;

public class ActivityIntroduction extends AppCompatActivity {

    public Context context;
    public AdapterIntroduction introductionAdapter;
    public List<ModelIntroductionImage> modelIntroductionImages;
    public Resources resources;
    public ViewPager view_pager;
    public RelativeLayout statBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        context = ActivityIntroduction.this;

        init();

    }

    private void init(){

        view_pager =  findViewById(R.id.view_pager);
        statBtn = findViewById(R.id.start_btn);

        modelIntroductionImages = new ArrayList<>();
        modelIntroductionImages.add(new ModelIntroductionImage(R.drawable.intro1,"", "", "NEXT"));
        modelIntroductionImages.add(new ModelIntroductionImage(R.drawable.intro2,"", "", "NEXT"));
        modelIntroductionImages.add(new ModelIntroductionImage(R.drawable.intro3,"", "", "GET STARTED"));

        introductionAdapter = new AdapterIntroduction(context, modelIntroductionImages);
        view_pager.setAdapter(introductionAdapter);

        statBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view_pager.getCurrentItem() + 1 < introductionAdapter.getCount()) {

                    view_pager.setCurrentItem(view_pager.getCurrentItem() + 1);

                }
                else {

                    Intent intent=new Intent(context, ActivityPhonenumberScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }

            }
        });

        calltopImage();

    }

    public void calltopImage() {

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

    }
}