package com.bannet.skils.favposting.activity;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.favposting.fragment.FragmentSavedPosting;
import com.bannet.skils.Adapter.AdapterMySavedPostingList;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import com.bannet.skils.R;
import com.bannet.skils.service.GlobalMethods;

import java.util.List;

public class ActivitySavedPostingList extends AppCompatActivity {

    ImageView img_back;
    RecyclerView recycler_fav;
    AdapterMySavedPostingList adapterMySavedPostingList;
    List<ProfessionalList> professionalLists;
    AdapterMySavedPostingList.ItemClickListener itemClickListener;
    Context context;
    TextView txt_heading;
    String USER_ID;

    //language
    Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_postings);
        context = ActivitySavedPostingList.this;

        if(getSupportActionBar() != null){

            getSupportActionBar().hide();

        }

        init();

    }
    public void init(){

        img_back = (ImageView) findViewById(R.id.img_back);
        txt_heading = (TextView) findViewById(R.id.txt_heading);

        txt_heading.setText(GlobalMethods.getString(context,R.string.saved));

        img_back.setOnClickListener(view -> finish());

        initMainFragment();

    }

    public void initMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle in = new Bundle();
        FragmentSavedPosting mFragment = FragmentSavedPosting.newInstance();
        mFragment.setArguments(in);
        transaction.replace(R.id.main_act_container, mFragment, mFragment.getClass().getSimpleName());
        transaction.commit();
    }


}
