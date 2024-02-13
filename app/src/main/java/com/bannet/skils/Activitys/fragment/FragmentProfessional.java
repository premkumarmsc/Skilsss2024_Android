package com.bannet.skils.Activitys.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bannet.skils.service.ApiPage;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.professinoldetails.activity.ActivityProffDetails;
import com.bannet.skils.professinoldetails.activity.ActivityWithoutimage;
import com.bannet.skils.viewallproffessionals.adapter.AdapterViewAllProff;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import com.bannet.skils.R;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentProfessional extends Fragment {

    View view;
    EditText search;
    public TextView currentAddress;
    Context context;
    String USER_ID,longitude,lattitude;
    public LinearLayout locationsearch;
    RecyclerView recycler_professions;
    //Language
    Resources resources;
    public AppCompatButton searchBtn;
    public String  status="";
    public String Address;
    public TextView arount;
    public GpsMyLocationProvider provider;
    private String query="";

    private ArrayList<ProfessionalList.Professional> userModalArrayList;
    private AdapterViewAllProff userRVAdapter;
    private AdapterViewAllProff.itemClikcLissner itemClikcLissner;
    private ProgressBar loadingPB;
    public NestedScrollView nestedSV;
    int page = 1;
    public String key = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_proffes, container, false);
        context= getActivity();



        init();

        return view;

    }

    private void init(){

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");

        longitude=PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude=PrefConnect.readString(context,PrefConnect.LATITUDE,"");
        Address=PrefConnect.readString(context,PrefConnect.ADDRESS,"");

        loadingPB = view.findViewById(R.id.idPBLoading);
        nestedSV = view.findViewById(R.id.idNestedSV);
        recycler_professions=view.findViewById(R.id.proff_list);
        locationsearch = view.findViewById(R.id.location_search);
        currentAddress = view.findViewById(R.id.current_address);
        arount = view.findViewById(R.id.arount);

      //  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        arount.setText(GlobalMethods.getString(context,R.string.peoples_around_you));
        userModalArrayList = new ArrayList<>();

        if(getArguments() != null){

            query = getArguments().getString("searchkey");

            if(longitude.isEmpty()){

                getCurrentLocation();

            }
            else {

                Log.e("sisxe",userModalArrayList.size()+"");

                if(userModalArrayList.size() >= 1){

                    userRVAdapter.ClearAll();
                    page = 1;
                    Log.e("clear","clear");
                    getProfessionalList(lattitude,longitude,query);

                }
                else {
                    Log.e("cleargrgr","clearfrge");
                    getProfessionalList(lattitude,longitude,query);
                }

            }

        }

        Places.initialize(getActivity(),EndPoint.apiKey);

        locationsearch.setOnClickListener(view -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(context);

            startActivityForResult(intent,100);
        });



        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    Log.e("pageeee",page+"");
                    loadingPB.setVisibility(View.VISIBLE);
                    getProfessionalList(lattitude,longitude,query);
                }
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

            userRVAdapter.ClearAll();
            page = 1;
            getProfessionalList(lattitude,longitude,query);

        });

    }

    public void getProfessionalList(String lati,String logi,String key){

        if(GlobalMethods.isNetworkAvailable(context)){

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
                            recycler_professions.setLayoutManager(new GridLayoutManager(context,2));

                            // setting adapter to our recycler view.
                            recycler_professions.setAdapter(userRVAdapter);

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

                    currentAddress.setText(str[0]);

                    Log.e("currentAddress",str[0]);

                }
                getlatandlag(place.getAddress());
            }

        }
    }


    private void changeLanguage() {

        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {

            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {

            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();
            search.setHint(resources.getText(R.string.search_with_skills_and_name));
            searchBtn.setText(resources.getText(R.string.search));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {

            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();
            search.setHint(resources.getText(R.string.search_with_skills_and_name));
            searchBtn.setText(resources.getText(R.string.search));

            search.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat("10"));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {

            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();
            search.setHint(resources.getText(R.string.search_with_skills_and_name));
            searchBtn.setText(resources.getText(R.string.search));

            search.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat("10"));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {

            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();
            search.setHint(resources.getText(R.string.search_with_skills_and_name));
            searchBtn.setText(resources.getText(R.string.search));

            search.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat("10"));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {

            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();
            search.setHint(resources.getText(R.string.search_with_skills_and_name));
            searchBtn.setText(resources.getText(R.string.search));

            search.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat("10"));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {

            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();
            search.setHint(resources.getText(R.string.search_with_skills_and_name));
            searchBtn.setText(resources.getText(R.string.search));

        }
    }
}