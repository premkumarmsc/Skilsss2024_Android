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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bannet.skils.service.ApiPage;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.viewallpostings.adapter.AdapterViewAllPosting;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.bannet.skils.post.responce.PostinglistModel;
import com.bannet.skils.R;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;

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


public class FragmentPosting extends Fragment {

    View view;
    Context context;
    EditText search;
    LinearLayout locationSearch;
    private TextView currentAddress;
    String USER_ID,longitude,lattitude;

    RecyclerView recycler_posting;

    public ImageView searchView;
    public AppCompatButton searchbtn;
    public String status="";
    FusedLocationProviderClient mFusedLocationClient;
    //language
    Resources resources;
    public TextView arount;
    public String query;
    public GpsMyLocationProvider provider;

    private ArrayList<PostinglistModel.Posting> userModalArrayList;
    private AdapterViewAllPosting userRVAdapter;
    private ProgressBar loadingPB;
    public NestedScrollView nestedSV;
    int page = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context=this.getActivity();
        view= inflater.inflate(R.layout.fragment_posting, container, false);


        init();
        return view;

    }

    private void init(){

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");
        longitude=PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude=PrefConnect.readString(context,PrefConnect.LATITUDE,"");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        search=view.findViewById(R.id.edit_search_text);

        searchView=view.findViewById(R.id.search_visible);
        searchbtn=view.findViewById(R.id.searchbtn);
        currentAddress = view.findViewById(R.id.current_address);
        locationSearch = view.findViewById(R.id.location_search);
        arount = view.findViewById(R.id.arount);

        arount.setText(GlobalMethods.getString(context,R.string.peoples_around_you));

        search.setHint("search with skills and name");
        loadingPB = view.findViewById(R.id.idPBLoading);
        nestedSV = view.findViewById(R.id.idNestedSV);
        recycler_posting=view.findViewById(R.id.post_list);

        userModalArrayList = new ArrayList<>();


        if(getArguments() != null){

            query = getArguments().getString("searchkey");

            if(longitude.isEmpty()){

                getCurrentLocation();

            }
            else {

                if(userModalArrayList.size() >= 1){

                    userRVAdapter.ClearAll();
                    page = 1;
                    Log.e("clear","clear");
                    getPostList(lattitude,longitude,query);

                }
                else {
                    Log.e("cleargrgr","clearfrge");
                    getPostList(lattitude,longitude,query);
                }

            }

        }
        else {

            getCurrentLocation();
        }

        Places.initialize(getActivity(), EndPoint.apiKey);

        locationSearch.setOnClickListener(view -> {
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
                    getPostList(lattitude,longitude,query);
                }
            }
        });

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
            getPostList(lattitude,longitude,query);

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
                getPostList(lattitude,longitude,query);

                Log.e("sues","suujd");
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


    public void getPostList(String lati,String logi,String query){
        Log.e("lati",lati);
        Log.e("logi",logi);
        if (GlobalMethods.isNetworkAvailable(context)) {

            ApiPage.getClient().postingslist(lati,logi,"",query,"en", String.valueOf(page)).enqueue(new Callback<PostinglistModel>() {
                @Override
                public void onResponse(@NonNull Call<PostinglistModel> call, @NonNull Response<PostinglistModel> response) {

                    Log.e("swruweyfe",new Gson().toJson(response.body()));

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){


                            status = response.body().getStatus();

                            int totalCount = response.body().getTotal_count();

                            userModalArrayList.addAll(response.body().getPostingList());
                            if(totalCount <=10){
                                loadingPB.setVisibility(View.GONE);
                            }

                            // passing array list to our adapter class.
                            userRVAdapter = new AdapterViewAllPosting(context,userModalArrayList);

                            // setting layout manager to our recycler view.
                            recycler_posting.setLayoutManager(new LinearLayoutManager(context));

                            // setting adapter to our recycler view.
                            recycler_posting.setAdapter(userRVAdapter);

                        }
                        else {

                            loadingPB.setVisibility(View.GONE);
                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PostinglistModel> call, @NonNull Throwable t) {



                }
            });

        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }

    private void changeLanguage() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {

            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();
            searchbtn.setText(resources.getText(R.string.search));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {

            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();
            searchbtn.setText(resources.getText(R.string.search));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {

            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();
            searchbtn.setText(resources.getText(R.string.search));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {

            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();
            searchbtn.setText(resources.getText(R.string.search));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {

            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();
            searchbtn.setText(resources.getText(R.string.search));
        }
        else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {

            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();
            searchbtn.setText(resources.getText(R.string.search));

            search.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat("10"));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {

            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();
            searchbtn.setText(resources.getText(R.string.search));


        }
    }
}