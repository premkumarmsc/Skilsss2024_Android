package com.bannet.skils.searchpage.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.bannet.skils.Activitys.fragment.FragmentPosting;
import com.bannet.skils.Activitys.fragment.FragmentProfessional;
import com.bannet.skils.Adapter.AdapterViewPagerStatic;
import com.bannet.skils.Adapter.SliderAdapter;
import com.bannet.skils.Model.WebModel.AdvertismentModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.home.responce.ViewPageImage;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.SliderView;

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySearchActivity extends AppCompatActivity {

    public Context context;
    public View view;
    public AppCompatButton userBtn, postingBtn;
    public RelativeLayout searchBtn;
    int viewPageImages = 0;
    public String longitude, latitude;
    public String select = "0";
    public EditText search;
    FragmentProfessional fragobj;
    String USER_ID;
    public int currentPage = 1;
    SliderView sliderView;
    SliderAdapter sliderAdapter;
    AdapterViewPagerStatic adapterViewPagerStatic;
    List<ViewPageImage> viewPageImagesstatic;
    public GpsMyLocationProvider provider;
    public  String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = ActivitySearchActivity.this;

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        init();

    }

    private void init() {

        userBtn = findViewById(R.id.user_btn);
        postingBtn = findViewById(R.id.posting_btn);
        searchBtn = findViewById(R.id.search_btn);
        search = findViewById(R.id.edi_search);
        sliderView = findViewById(R.id.view_pager);

        userBtn.setText(GlobalMethods.getString(context, R.string.users));
        postingBtn.setText(GlobalMethods.getString(context, R.string.postings));
        search.setHint(GlobalMethods.getString(context, R.string.search_with_skills_and_name));

        longitude = PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        latitude = PrefConnect.readString(context,PrefConnect.LATITUDE,"");

        query = getIntent().getStringExtra("key");
        if(longitude.isEmpty()){

            getCurrentLocation();
        }
        else {

            getAdvertisment(latitude,longitude);

        }

        Bundle bundle1 = new Bundle();
        bundle1.putString("searchkey", query);
        FragmentProfessional fragobj1 = new FragmentProfessional();
        fragobj1.setArguments(bundle1);
        replaceFragment(fragobj1);
        userBtn.setOnClickListener(view1 -> {

            currentPage = 1;
            userBtn.setBackgroundResource(R.drawable.home_layout_bg);
            postingBtn.setBackgroundResource(0);
            Bundle bundle = new Bundle();
            bundle.putString("searchkey", query);
            FragmentProfessional fragobj = new FragmentProfessional();
            fragobj.setArguments(bundle);
            replaceFragment1(fragobj);

        });

        postingBtn.setOnClickListener(view1 -> {

            currentPage = 2;
            userBtn.setBackgroundResource(0);
            postingBtn.setBackgroundResource(R.drawable.home_layout_bg);
            Bundle bundle = new Bundle();
            bundle.putString("searchkey", query);
            FragmentPosting fragobj = new FragmentPosting();
            fragobj.setArguments(bundle);
            replaceFragment1(fragobj);
        });

        searchBtn.setOnClickListener(view12 -> {

            query = search.getText().toString();

            if (currentPage == 1) {
                Bundle bundle = new Bundle();
                bundle.putString("searchkey", query);
                FragmentProfessional fragobj = new FragmentProfessional();
                fragobj.setArguments(bundle);
                replaceFragment1(fragobj);

            } else {

                Bundle bundle = new Bundle();
                bundle.putString("searchkey", query);
                FragmentPosting fragobj = new FragmentPosting();
                fragobj.setArguments(bundle);
                replaceFragment1(fragobj);

            }

        });

        USER_ID = PrefConnect.readString(context, PrefConnect.USER_ID, "");

    }

    public  void getCurrentLocation( ) {

        provider = new GpsMyLocationProvider(context);
        provider.setLocationUpdateMinDistance(0);
        provider.setLocationUpdateMinTime(0);
        provider.startLocationProvider((location, source) -> {

            provider.stopLocationProvider();
            Log.e("Latitude : ", location.getLatitude() + "\nLongitude : " + location.getLongitude() + "\nAccuracy : " + location.getAccuracy());

            latitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";

            getAdvertisment(latitude,longitude);

        });

    }

    public void getAdvertisment(String latitude, String longitude) {

        if (GlobalMethods.isNetworkAvailable(context)) {
            Api.getClient().getAdsList(latitude, longitude, PrefConnect.readString(context, PrefConnect.LANGUAGE_RESPONCE, "")).enqueue(new Callback<AdvertismentModel>() {
                @Override
                public void onResponse(@NonNull Call<AdvertismentModel> call, @NonNull Response<AdvertismentModel> response) {

                    Log.e("res", new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus().equals("1")) {

                            viewPageImages = response.body().getAdsList().size();

                            sliderAdapter = new SliderAdapter(context, response.body().getAdsList());
                            sliderView.setSliderAdapter(sliderAdapter);
//                            sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                            sliderView.startAutoCycle();

                        } else {

                            getBannerList();

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AdvertismentModel> call, @NonNull Throwable t) {

                }
            });

        } else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context, R.string.No_Internet_Connection));

        }
    }

    public void getBannerList(){
        viewPageImagesstatic = new ArrayList<>();
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.textimaagr_electrical));
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.image));
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.textimaagr_electrical));
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.image));

        adapterViewPagerStatic = new AdapterViewPagerStatic(context, viewPageImagesstatic);
        sliderView.setSliderAdapter(adapterViewPagerStatic);
//      sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.detach(fragment).attach(fragment);
        fragmentTransaction.replace(R.id.framLayout_home,fragment);
        fragmentTransaction.commit();

    }

    private void replaceFragment1(Fragment fragment) {

        Log.e("remove","remove");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framLayout_home,fragment);
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();

    }

}