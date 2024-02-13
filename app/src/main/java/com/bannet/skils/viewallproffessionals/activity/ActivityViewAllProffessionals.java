package com.bannet.skils.viewallproffessionals.activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bannet.skils.Adapter.AdapterViewPagerStatic;
import com.bannet.skils.Adapter.SliderAdapter;
import com.bannet.skils.Model.WebModel.AdvertismentModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.ApiPage;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.home.responce.ViewPageImage;
import com.bannet.skils.professinoldetails.activity.ActivityProffDetails;
import com.bannet.skils.professinoldetails.activity.ActivityWithoutimage;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import com.bannet.skils.viewallproffessionals.adapter.AdapterViewAllProff;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.SliderView;

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewAllProffessionals extends AppCompatActivity {

    public Context context;
    public ProgressDialog progressDialog;
    public int currentPage = 1;
    public String longitude,lattitude;
    public String search_text;
    public EditText search;
    public String querytext = "";
    public RelativeLayout searchBtn;
    public GpsMyLocationProvider  provider;
    int viewPageImages = 0;
    public ImageView backBtn,backBtn1;
    SliderView sliderView;
    SliderAdapter sliderAdapter;
    AdapterViewPagerStatic adapterViewPagerStatic;
    List<ViewPageImage> viewPageImagesstatic;
    public TextView p1;

    private ArrayList<ProfessionalList.Professional> userModalArrayList;
    private AdapterViewAllProff userRVAdapter;
    private AdapterViewAllProff.itemClikcLissner itemClikcLissner;
    private RecyclerView userRV;
    private ProgressBar loadingPB;
    public NestedScrollView nestedSV;
    int page = 1;
    public String key = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_proffessionals);

        context = ActivityViewAllProffessionals.this;

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        init();

    }

    private void init(){

        search = findViewById(R.id.edi_search);
        searchBtn = findViewById(R.id.search_btn);
        sliderView = findViewById(R.id.view_pager);
        backBtn = findViewById(R.id.back_btn);
        backBtn1 = findViewById(R.id.image_back);
        p1 = findViewById(R.id.p1);

        p1.setText(GlobalMethods.getString(context,R.string.peoples_around_you));
        search.setHint(GlobalMethods.getString(context,R.string.search_with_skills_and_name));

        userModalArrayList = new ArrayList<>();

        // initializing our views.
        userRV = findViewById(R.id.proff_list);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);

        backBtn.setOnClickListener(view -> finish());
        backBtn1.setOnClickListener(view -> finish());

        searchBtn.setOnClickListener(view -> {

                String query;
                query = search.getText().toString();
                if(!query.isEmpty())
                {
                    querytext=query;
                    key = query;
                    userRVAdapter.ClearAll();
                    page = 1;
                    getProfessionalList(lattitude,longitude,query);
                }
                else {
                    querytext="";
                    key = "";
                    userRVAdapter.ClearAll();

                    getProfessionalList(lattitude,longitude,"");

                }
            if(search.getText().toString().isEmpty()){
                currentPage=1;
                key = "";
                userRVAdapter.ClearAll();
                getProfessionalList(lattitude,longitude,"");
                Log.e("emp","empty");
            }

        });

        longitude = PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude = PrefConnect.readString(context,PrefConnect.LATITUDE,"");

        if(longitude.isEmpty()){

            getCurrentLocation();
        }
        else {

            getProfessionalList(lattitude,longitude,key);
            getAdvertisment(lattitude,longitude);

        }

        nestedSV.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                page++;
                Log.e("pageeee",page+"");
                loadingPB.setVisibility(View.VISIBLE);
                getProfessionalList(lattitude,longitude,key);
            }
        });

        itemClikcLissner = professional -> {
            if(professional.getBannerImage().equals("") || professional.getBannerImage().isEmpty()){

                Intent in = new Intent(context, ActivityWithoutimage.class);
                in.putExtra("profe_id",professional.getId());
                in.putExtra("profileurl",professional.getImageUrl());
                in.putExtra("profile_name",professional.getImageName());
                in.putExtra("type","1");
                in.putExtra("typehire","add");
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }
            else {
                Intent in = new Intent(context, ActivityProffDetails.class);
                in.putExtra("profe_id",professional.getId());
                in.putExtra("profileurl",professional.getImageUrl());
                in.putExtra("profile_name",professional.getImageName());
                in.putExtra("latitude",professional.getLatitude());
                in.putExtra("logitude",professional.getLongitude());
                in.putExtra("type","1");
                in.putExtra("typehire","add");
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }
        };


    }

    public  void getCurrentLocation( ) {

        provider = new GpsMyLocationProvider(context);
        provider.setLocationUpdateMinDistance(0);
        provider.setLocationUpdateMinTime(0);
        provider.startLocationProvider((location, source) -> {

            provider.stopLocationProvider();
            Log.e("Latitude : ", location.getLatitude() + "\nLongitude : " + location.getLongitude() + "\nAccuracy : " + location.getAccuracy());

            lattitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";


            getProfessionalList(lattitude,longitude,"");
            getAdvertisment(lattitude,longitude);

        });

    }

    public  void getlatandlag(String object_Name){


        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Log.e("locationAddress", object_Name);
        try {

            List addressList = geocoder.getFromLocationName(object_Name, 1);
            if (addressList != null && addressList.size() > 0) {

                Address address = (Address) addressList.get(0);

                lattitude = String.valueOf(address.getLatitude());
                longitude = String.valueOf(address.getLongitude());
                getProfessionalList(lattitude,longitude,"");

            }
        } catch (IOException e) {

            e.printStackTrace();

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            if(data != null){
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.e("cc",place.getAddress());

                String[] str = Objects.requireNonNull(place.getAddress()).split(",");

                for (int i = 0; i < str.length; i++) {

                   // currentAddress.setText(str[0]);

                    Log.e("currentAddress",str[0]);

                }
                getlatandlag(place.getAddress());
            }

        }
    }

    public void getProfessionalList(String lati,String logi,String key){

        search_text = search.getText().toString()+"";
        Log.e("search_text",search_text);
        if(GlobalMethods.isNetworkAvailable(context)){
            String currentPaged= String.valueOf(currentPage);
            Log.d("page",currentPaged);
            ApiPage.getClient().professionallist(lati,logi,"",key, PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""), String.valueOf(page)).enqueue(new Callback<ProfessionalList>() {
                @Override
                public void onResponse(@NonNull Call<ProfessionalList> call, @NonNull Response<ProfessionalList> response) {
                    Log.e("p_responec",new Gson().toJson(response.body()));
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            int totalCount = response.body().getTotal_count();

                            userModalArrayList.addAll(response.body().getProfessionalList());
                            if(totalCount <=10){
                                loadingPB.setVisibility(View.GONE);
                            }

                            // passing array list to our adapter class.
                            userRVAdapter = new AdapterViewAllProff(context,userModalArrayList,"2",itemClikcLissner);

                            // setting layout manager to our recycler view.
                            userRV.setLayoutManager(new GridLayoutManager(context,2));

                            // setting adapter to our recycler view.
                            userRV.setAdapter(userRVAdapter);

                        }
                        else {
                            loadingPB.setVisibility(View.GONE);
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ProfessionalList> call, @NonNull Throwable t) {

                }
            });

        }else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }

    public void getAdvertisment(String latitude,String longitude) {

        if (GlobalMethods.isNetworkAvailable(context)) {
            Random random = new Random();
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

}