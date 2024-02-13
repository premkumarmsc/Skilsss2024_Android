package com.bannet.skils.Activitys;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.bannet.skils.Adapter.AdapterCauralView;
import com.bannet.skils.Model.AdsCoverageList;
import com.bannet.skils.Model.ModelCauralView;
import com.bannet.skils.R;
import com.bannet.skils.Adapter.AdapterCity;
import com.bannet.skils.Adapter.AdapterCountry;
import com.bannet.skils.Adapter.AdapterState;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.profile.responce.CityModel;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.profile.responce.CountryModel;
import com.bannet.skils.profile.responce.StateModel;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.IAPHelper;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPlanDetails extends AppCompatActivity  implements IAPHelper.IAPHelperListener{

    Context context;
    String planId = "1",planName,planCoverage,planValidity,AddresValue;
    RelativeLayout planCountry,planState,planeCity,planLocal;
    ImageView addDistrict,addCountry,addState;
    Dialog dialogbox;
    ProgressDialog progressDialog;
    AdapterCountry adapterCountry;
    AdapterCountry.ItemClickListener itemClickListener_country;
    List<CountryModel.Country> countryModels;
    AdapterCity adapterCity;
    AdapterCity.ItemClickListener itemClickListener_city;
    List<CityModel.City> cityModels;
    AdapterState adapterState;
    AdapterState.ItemClickListener itemClickListener_state;
    List<StateModel.States> stateModels;
    String state_id="",city_id="",country_id="";
    String State_name="",City_name="",Country_name="";
    AppCompatButton closeDialoglocation,state_dialogbox_done,country_dialogbox_done,distric_dialogbox_done;
    RecyclerView recycler_country;
    RecyclerView recycler_city;
    RecyclerView recycler_state;
    TextView txt_country,txt_state,txt_city;
    TextView plan_name,plan_cost,plan_validity;
    TextView localAddress;
    LinearLayout planSubmitBtn;
    TextView submit;
    String USER_ID,Address;
    String longitude,lattitude;
    String address="",opnamecity="";
    IAPHelper iapHelper;
    public GpsMyLocationProvider  provider;
    HashMap<String, SkuDetails> skuDetailsHashMap = new HashMap<>();

    Resources resources;
    private String selected ="0";
    TextView pd1,pd2,pd3,pd4,pd5,pd6,pd7,pd8,pd9,sc,ss,sco;

    String plan_local="skilsss_local_plan_29.33";
    String plan_silver="skilsss_silver_plan_59.99";
    String plan_gold="skilsss_gold_plan_149.99";

    //skilsss_gold_plan_149.99
    //skilsss_local_plan_29.33
    //skilsss_silver_plan_59.99
    private List<String> skuList = Arrays.asList(plan_local,plan_silver,plan_gold);

    public ViewPager2 viewPager2;
    public ArrayList<ModelCauralView> cauralViews = new ArrayList<>();
    public AdapterCauralView adapterCauralView;
    public AdapterCauralView.ItemClickListener itemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        getSupportActionBar().hide();
        context = ActivityPlanDetails.this;
        iapHelper = new IAPHelper(this, this, skuList);
        pd1=findViewById(R.id.pd1);
        pd2=findViewById(R.id.pd2);
        pd3=findViewById(R.id.pd3);
        pd4=findViewById(R.id.pd4);
        pd5=findViewById(R.id.pd5);
        pd6=findViewById(R.id.pd6);
        pd7=findViewById(R.id.pd7);
        pd8=findViewById(R.id.pd8);
        pd9=findViewById(R.id.pd9);
        submit = findViewById(R.id.sub_btn);
        viewPager2 = findViewById(R.id.viewPager);


//        planId=getIntent().getStringExtra("plan_id");
//        planName=getIntent().getStringExtra("plan_name");
//        planCoverage=getIntent().getStringExtra("plan_coverage");
//        planValidity=getIntent().getStringExtra("plan_validity");
//        planCost=getIntent().getStringExtra("plan_cost");
//
//        Log.e("plan_id",planId);

        planList();
        submit.setText(GlobalMethods.getString(context,R.string.submit));

        USER_ID=PrefConnect.readString(context,PrefConnect.USER_ID,"");
        Address=PrefConnect.readString(context,PrefConnect.ADDRESS,"");
        lattitude = PrefConnect.readString(context,PrefConnect.LATITUDE,"");
        longitude = PrefConnect.readString(context,PrefConnect.LOGITUDE,"");

        if(longitude.isEmpty()){

            getCurrentLocation();

        }
        else {

            GetAddressSOURCE getAddressSOURCE = new GetAddressSOURCE();
            getAddressSOURCE.execute();

        }

        planCountry=findViewById(R.id.plan_country);
        planState=findViewById(R.id.plan_state);
        planeCity=findViewById(R.id.plan_city);
        planLocal=findViewById(R.id.plan_local);

        localAddress=findViewById(R.id.txt_local);

        addDistrict=findViewById(R.id.planpage_add_district);
        addCountry=findViewById(R.id.planpage_add_country);
        addState=findViewById(R.id.planpage_add_state);



        txt_country=findViewById(R.id.txt_country);
        txt_state=findViewById(R.id.txt_state);
        txt_city=findViewById(R.id.txt_city);

        plan_name=findViewById(R.id.plan_name);
        plan_cost=findViewById(R.id.plan_cost);
        plan_validity=findViewById(R.id.plan_validity);

        planSubmitBtn=findViewById(R.id.plan_submit_btn);

        Places.initialize(getApplicationContext(), EndPoint.apiKey);

        localAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(context);

                startActivityForResult(intent,100);
            }
        });

        changeLanguage();
        String Days = GlobalMethods.getString(context,R.string.days);

//        plan_name.setText("       :   "+planName+" ("+planCoverage+")");
//        plan_cost.setText("       :   "+" $ " +planCost);
//        plan_validity.setText("     :   "+planValidity+ "( "+Days+" )");

        addDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDistrict();
            }
        });
        addCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCountry();
            }
        });
        addState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addState();
            }
        });
        planSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (planId) {

                    case "1":

                        launch(plan_local);

                       // planOne(USER_ID, planId);
                        break;

                    case "2":
                        if (plan1()) {

                            launch(plan_silver);
                           // planTwo(USER_ID, planId);

                        }
                        break;

                    case "3":
                        if (plan2()) {

                           launch(plan_gold);
                           // planThree(USER_ID, planId);

                        }
                        break;

                    case "4":
                        if (plan3()) {

                          //  launch(TEST);
                         //   planFour(USER_ID, planId);

                        }
                        break;
                }
            }
        });

        itemClickListener = new AdapterCauralView.ItemClickListener() {
            @Override
            public void ItemClick(String position) {

            }
        };

    }

    public void planList(){

        if(GlobalMethods.isNetworkAvailable(ActivityPlanDetails.this)){
            progressDialog = ProgressDialog.show(this, "", "Loading...", true);
            Api.getClient().showPlan(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<AdsCoverageList>() {
                @Override
                public void onResponse(@NonNull Call<AdsCoverageList> call, @NonNull Response<AdsCoverageList> response) {
                    progressDialog.dismiss();
                    assert response.body() != null;

                    if(response.body().getStatus().equals("1")){

                        adapterCauralView = new AdapterCauralView(context,response.body().getPlanList(),viewPager2,itemClickListener);
                        viewPager2.setAdapter(adapterCauralView);
                        viewPager2.setOffscreenPageLimit(3);
                        viewPager2.setClipChildren(false);
                        viewPager2.setClipToPadding(false);

                        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

                        compositePageTransformer.addTransformer((page, position) -> {

                            float r = 1 -Math.abs(position);
                            page.setScaleY(0.85f + r * 0.15f);

                        });

                        viewPager2.setPageTransformer(compositePageTransformer);

                        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                                Log.e("position", String.valueOf(position));

                                if(position == 0){

                                    if(selected.equals("0")){
                                        planId = "1";


                                    }
                                    else {

                                        planId = "1";
                                        planLocal.setVisibility(View.VISIBLE);
                                        localAddress.setText(AddresValue);
                                        planState.setVisibility(View.GONE);
                                        planeCity.setVisibility(View.GONE);
                                        planCountry.setVisibility(View.GONE);
                                        txt_country.setText(GlobalMethods.getString(context,R.string.country_optional));
                                        txt_state.setText(GlobalMethods.getString(context,R.string.state_optional));
                                        txt_city.setText(GlobalMethods.getString(context,R.string.city_optional));


                                    }

                                }
                                else if(position == 1){

                                    selected = "1";
                                    planId = "2";
                                    planLocal.setVisibility(View.GONE);
                                    planCountry.setVisibility(View.VISIBLE);
                                    planState.setVisibility(View.VISIBLE);
                                    planeCity.setVisibility(View.VISIBLE);
                                    txt_country.setText(GlobalMethods.getString(context,R.string.country_optional));
                                    txt_state.setText(GlobalMethods.getString(context,R.string.state_optional));
                                    txt_city.setText(GlobalMethods.getString(context,R.string.city_optional));

                                }
                                else if(position == 2){

                                    planId = "3";
                                    selected = "1";
                                    planLocal.setVisibility(View.GONE);
                                    planCountry.setVisibility(View.VISIBLE);
                                    planState.setVisibility(View.VISIBLE);
                                    planeCity.setVisibility(View.GONE);
                                    txt_country.setText(GlobalMethods.getString(context,R.string.country_optional));
                                    txt_state.setText(GlobalMethods.getString(context,R.string.state_optional));
                                    txt_city.setText(GlobalMethods.getString(context,R.string.city_optional));
                                }
                                else if(position == 3){

                                    planId = "4";
                                    selected = "1";
                                    planLocal.setVisibility(View.GONE);
                                    planState.setVisibility(View.GONE);
                                    planeCity.setVisibility(View.GONE);
                                    planCountry.setVisibility(View.VISIBLE);
                                    txt_country.setText(GlobalMethods.getString(context,R.string.country_optional));
                                    txt_state.setText(GlobalMethods.getString(context,R.string.state_optional));
                                    txt_city.setText(GlobalMethods.getString(context,R.string.city_optional));

                                }

                            }

                            @Override
                            public void onPageSelected(int position) {
                                super.onPageSelected(position);

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                super.onPageScrollStateChanged(state);

                            }
                        });

                    }
                }

                @Override
                public void onFailure(@NonNull Call<AdsCoverageList> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    GlobalMethods.Toast(context,t.getMessage());
                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 100:

                if(resultCode == RESULT_OK){

                    Place place = Autocomplete.getPlaceFromIntent(data);

                    localAddress.setText(place.getAddress());
                    getlatandlag(place.getAddress());

                }
                break;
        }
    }

    private void planActive(String user_id, String plan_id) {

        if (GlobalMethods.isNetworkAvailable(ActivityPlanDetails.this)) {

            progressDialog = ProgressDialog.show(ActivityPlanDetails.this, "", "Loading...", true);
            Api.getClient().purchasePlan(user_id,plan_id,city_id,state_id,country_id,City_name,State_name,Country_name,"5363523872","Android",
                    "30",lattitude,longitude,address,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            GlobalMethods.Toast(context,response.body().getMessage());

                            Intent intent=new Intent(context,ActivityAdminPageUrl.class);
                            startActivity(intent);
                            finish();


                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                    else {

                        GlobalMethods.Toast(context,GlobalMethods.getString(context,R.string.please_try_again_later));

                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });

        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }

    public  void getCurrentLocation( ) {

        provider = new GpsMyLocationProvider(context);
        provider.setLocationUpdateMinDistance(0);
        provider.setLocationUpdateMinTime(0);
        provider.startLocationProvider((location, source) -> {

            provider.stopLocationProvider();

            lattitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";

            PrefConnect.writeString(context,PrefConnect.LATITUDE,lattitude);
            PrefConnect.writeString(context,PrefConnect.LOGITUDE,longitude);

            GetAddressSOURCE getAddressSOURCE = new GetAddressSOURCE();
            getAddressSOURCE.execute();

        });

    }


    @SuppressLint("StaticFieldLeak")
    public class GetAddressSOURCE extends AsyncTask<String, String, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+lattitude+","+longitude+"&key="+EndPoint.apiKey;

            String result = null;
            String inputLine;

            try {

                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();

                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null

                while ((inputLine = reader.readLine()) != null) {

                    stringBuilder.append(inputLine);

                }

                reader.close();
                streamReader.close();
                result = stringBuilder.toString();

            } catch (Exception e) {

                e.printStackTrace();

            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                AddresValue=jsonObject1.getString("formatted_address");

                localAddress.setText(AddresValue);

            }
            catch (JSONException e) {

                e.printStackTrace();

            }

        }
    }


    private Boolean plan1(){
        if(country_id.equals("")){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_country));
            return  false;
        }
        else if(state_id.equals("")){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_state));
            return  false;
        }
        else if(city_id.equals("")){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_city));
            return  false;
        }
        return true;
    }
    private Boolean plan2(){

        if(country_id.equals("")){
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_country));
            return  false;
        }else if(state_id.equals("")){
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_state));
            return  false;
        }
        return true;
    }
    private Boolean plan3(){

       if(country_id.equals("")){
           GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_country));
            return  false;
       }
        return true;
    }


    private  void planOne(String user_id, String plan_id){
        String planOneLocation=localAddress.getText().toString();
        address=planOneLocation;
        getlatandlag(planOneLocation);

    }
    private  void planTwo(String user_id,String plan_i){

        if(validationcity()){
            getlatandlag(City_name);
        }

    }
    private  void planThree(String user_id,String plan_id){

        if(validationstate()){
            getlatandlag(State_name);
        }


    }
    private  void planFour(String user_id,String plan_id){


        if(validationcountry()){
            getlatandlag(Country_name);
        }

    }
    private  Boolean validationcountry(){
        if(Country_name.equals("")){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_city));
            return false;
        }

        return true;
    }
    private  Boolean validationstate(){

        if(Country_name.equals("")){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_country));
            return false;

        }
        else if(State_name.equals("")){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_state));
            return false;
        }
        return true;
    }
    private  Boolean validationcity(){

        if(Country_name.equals("")){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_country));
            return false;

        }else if(State_name.equals("")){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_state));
            return false;

        }else if(City_name.equals("")) {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_city));
            return false;

        }
        return true;
    }

    public  void getlatandlag(String object_Name){

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {

            List addressList = geocoder.getFromLocationName(object_Name, 1);
            if (addressList != null && addressList.size() > 0) {
                android.location.Address address = (Address) addressList.get(0);

                lattitude = String.valueOf(address.getLatitude());
                longitude = String.valueOf(address.getLongitude());

            }

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    private void addDistrict() {

        dialogbox= new Dialog(ActivityPlanDetails.this);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_add_district);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.show();
        dialogbox.setCancelable(false);
        sc=dialogbox.findViewById(R.id.sc);
        recycler_city = (RecyclerView) dialogbox.findViewById(R.id.recycler_city);
        distric_dialogbox_done = (AppCompatButton) dialogbox.findViewById(R.id.distric_dialogbox_done);
        closeDialoglocation=(AppCompatButton) dialogbox.findViewById(R.id.distric_dialogbox_close);
        TextView other_option=dialogbox.findViewById(R.id.other_option);
        LinearLayout city_layout=dialogbox.findViewById(R.id.city_layout);
        EditText othersCity=dialogbox.findViewById(R.id.others_txt_city);

        other_option.setOnClickListener(v -> city_layout.setVisibility(View.VISIBLE));

        closeDialoglocation.setOnClickListener(view -> {
            dialogbox.dismiss();
            city_id = "";
            City_name = "";
        });
        distric_dialogbox_done.setOnClickListener(view -> {

            dialogbox.dismiss();
            opnamecity=othersCity.getText().toString();

            if(opnamecity.equals("")){

                getlatandlag(City_name);
                txt_city.setText(City_name);

            }else {

                City_name=opnamecity;
                getlatandlag(City_name);
                txt_city.setText(City_name);

            }

        });
        changeLanguageCity();
        progressDialog = ProgressDialog.show(ActivityPlanDetails.this, "", "Loading...", true);
        Api.getClient().getCity(state_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("1")){
                        cityModels = response.body().getCityList();
                        adapterCity = new AdapterCity(context,itemClickListener_city,cityModels);
                        recycler_city.setHasFixedSize(true);
                        recycler_city.setLayoutManager(new LinearLayoutManager(context));
                        recycler_city.setAdapter(adapterCity);

                    }else {
                        GlobalMethods.Toast(context,response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CityModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

        try {
            itemClickListener_city = new AdapterCity.ItemClickListener() {
                @Override
                public void ItemClick(String id, String city_name) {
                    city_id = id;
                    city_name = city_name;
                    txt_city.setText(city_name);
                    City_name=city_name;
                }
            };
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void addState() {
        dialogbox= new Dialog(ActivityPlanDetails.this);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_addstate);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.show();
        dialogbox.setCancelable(false);
        closeDialoglocation=(AppCompatButton) dialogbox.findViewById(R.id.state_dialogbox_close);
        state_dialogbox_done = (AppCompatButton) dialogbox.findViewById(R.id.state_dialogbox_done);
        recycler_state = (RecyclerView) dialogbox.findViewById(R.id.recycler_state);

        ss=dialogbox.findViewById(R.id.ss);
        state_dialogbox_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogbox.dismiss();
            }
        });

        closeDialoglocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogbox.dismiss();
                state_id = "";
                State_name = "";
            }
        });
        changeLanguageState();

        progressDialog = ProgressDialog.show(ActivityPlanDetails.this, "", "Loading...", true);
        Api.getClient().getState(country_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<StateModel>() {
            @Override
            public void onResponse(Call<StateModel> call, Response<StateModel> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("1")){

                        stateModels = response.body().getStatesList();
                        adapterState = new AdapterState(context,itemClickListener_state,stateModels);
                        recycler_state.setHasFixedSize(true);
                        recycler_state.setLayoutManager(new LinearLayoutManager(context));
                        recycler_state.setAdapter(adapterState);

                    }else {
                        GlobalMethods.Toast(context,response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<StateModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

        try{
            itemClickListener_state = new AdapterState.ItemClickListener() {
                @Override
                public void ItemClick(String id, String state_name) {
                    state_id = id;
                    state_name = state_name;
                    txt_state.setText(state_name);
                    State_name=state_name;

                }
            };
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void addCountry() {
        countryModels = new ArrayList<>();
        dialogbox = new Dialog(ActivityPlanDetails.this);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_addcountry);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.show();
        dialogbox.setCancelable(false);
        sco=dialogbox.findViewById(R.id.sco);
        recycler_country = (RecyclerView) dialogbox.findViewById(R.id.recycler_country);
        closeDialoglocation = (AppCompatButton) dialogbox.findViewById(R.id.country_dialogbox_close);
        country_dialogbox_done = (AppCompatButton) dialogbox.findViewById(R.id.country_dialogbox_done);

        country_dialogbox_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogbox.dismiss();
            }
        });

        closeDialoglocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogbox.dismiss();
                country_id = "";
                Country_name = "";
            }
        });
        changeLanguageCountry();

        progressDialog = ProgressDialog.show(ActivityPlanDetails.this, "", "Loading...", true);
        Api.getClient().getCountry(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CountryModel>() {
            @Override
            public void onResponse(Call<CountryModel> call, Response<CountryModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("1")) {
                        countryModels = response.body().getCountryList();
                        adapterCountry = new AdapterCountry(context, itemClickListener_country, countryModels);
                        recycler_country.setHasFixedSize(true);
                        recycler_country.setLayoutManager(new LinearLayoutManager(context));
                        recycler_country.setAdapter(adapterCountry);
                    } else {
                        GlobalMethods.Toast(context, response.body().getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<CountryModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

        try {
            itemClickListener_country = new AdapterCountry.ItemClickListener() {
                @Override
                public void ItemClick(String countr_id, String country_name) {

                    country_id = countr_id;
                    country_name = country_name;
                    txt_country.setText(country_name);
                    Country_name = country_name;
                }
            };
        } catch (Exception e) {
            e.printStackTrace();

        }


    }
    private void changeLanguage() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.select_your_location));
            pd2.setText(resources.getText(R.string.plan_details));
            pd3.setText(resources.getText(R.string.plan));
            pd4.setText(resources.getText(R.string.cost));
            pd5.setText(resources.getText(R.string.validity));
            pd6.setText(resources.getText(R.string.notes));
            pd7.setText(resources.getText(R.string.notes_1));
            pd8.setText(resources.getText(R.string.notes_2));
            pd9.setText(resources.getText(R.string.notes_3));
            txt_country.setText(resources.getText(R.string.country_optional));
            txt_state.setText(resources.getText(R.string.state_optional));
            txt_city.setText(resources.getText(R.string.city_optional));






        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.select_your_location));
            pd2.setText(resources.getText(R.string.plan_details));
            pd3.setText(resources.getText(R.string.plan));
            pd4.setText(resources.getText(R.string.cost));
            pd5.setText(resources.getText(R.string.validity));
            pd6.setText(resources.getText(R.string.notes));
            pd7.setText(resources.getText(R.string.notes_1));
            pd8.setText(resources.getText(R.string.notes_2));
            pd9.setText(resources.getText(R.string.notes_3));
            txt_country.setText(resources.getText(R.string.country_optional));
            txt_state.setText(resources.getText(R.string.state_optional));
            txt_city.setText(resources.getText(R.string.city_optional));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.select_your_location));
            pd2.setText(resources.getText(R.string.plan_details));
            pd3.setText(resources.getText(R.string.plan));
            pd4.setText(resources.getText(R.string.cost));
            pd5.setText(resources.getText(R.string.validity));
            pd6.setText(resources.getText(R.string.notes));
            pd7.setText(resources.getText(R.string.notes_1));
            pd8.setText(resources.getText(R.string.notes_2));
            pd9.setText(resources.getText(R.string.notes_3));
            txt_country.setText(resources.getText(R.string.country_optional));
            txt_state.setText(resources.getText(R.string.state_optional));
            txt_city.setText(resources.getText(R.string.city_optional));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.select_your_location));
            pd2.setText(resources.getText(R.string.plan_details));
            pd3.setText(resources.getText(R.string.plan));
            pd4.setText(resources.getText(R.string.cost));
            pd5.setText(resources.getText(R.string.validity));
            pd6.setText(resources.getText(R.string.notes));
            pd7.setText(resources.getText(R.string.notes_1));
            pd8.setText(resources.getText(R.string.notes_2));
            pd9.setText(resources.getText(R.string.notes_3));
            txt_country.setText(resources.getText(R.string.country_optional));
            txt_state.setText(resources.getText(R.string.state_optional));
            txt_city.setText(resources.getText(R.string.city_optional));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.select_your_location));
            pd2.setText(resources.getText(R.string.plan_details));
            pd3.setText(resources.getText(R.string.plan));
            pd4.setText(resources.getText(R.string.cost));
            pd5.setText(resources.getText(R.string.validity));
            pd6.setText(resources.getText(R.string.notes));
            pd7.setText(resources.getText(R.string.notes_1));
            pd8.setText(resources.getText(R.string.notes_2));
            pd9.setText(resources.getText(R.string.notes_3));
            txt_country.setText(resources.getText(R.string.country_optional));
            txt_state.setText(resources.getText(R.string.state_optional));
            txt_city.setText(resources.getText(R.string.city_optional));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.select_your_location));
            pd2.setText(resources.getText(R.string.plan_details));
            pd3.setText(resources.getText(R.string.plan));
            pd4.setText(resources.getText(R.string.cost));
            pd5.setText(resources.getText(R.string.validity));
            pd6.setText(resources.getText(R.string.notes));
            pd7.setText(resources.getText(R.string.notes_1));
            pd8.setText(resources.getText(R.string.notes_2));
            pd9.setText(resources.getText(R.string.notes_3));
            txt_country.setText(resources.getText(R.string.country_optional));
            txt_state.setText(resources.getText(R.string.state_optional));
            txt_city.setText(resources.getText(R.string.city_optional));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            pd1.setText(resources.getText(R.string.select_your_location));
            pd2.setText(resources.getText(R.string.plan_details));
            pd3.setText(resources.getText(R.string.plan));
            pd4.setText(resources.getText(R.string.cost));
            pd5.setText(resources.getText(R.string.validity));
            pd6.setText(resources.getText(R.string.notes));
            pd7.setText(resources.getText(R.string.notes_1));
            pd8.setText(resources.getText(R.string.notes_2));
            pd9.setText(resources.getText(R.string.notes_3));
            txt_country.setText(resources.getText(R.string.country_optional));
            txt_state.setText(resources.getText(R.string.state_optional));
            txt_city.setText(resources.getText(R.string.city_optional));


        }
    }

    private void changeLanguageCountry() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));


        }
    }
    private void changeLanguageState() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));






        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));


        }
    }
    private void changeLanguageCity() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));



        }
    }



    private void launch(String sku){
        if(!skuDetailsHashMap.isEmpty())
            iapHelper.launchBillingFLow(skuDetailsHashMap.get(sku));
    }

    @Override
    public void onSkuListResponse(HashMap<String, SkuDetails> skuDetails) {
        skuDetailsHashMap = skuDetails;


        Iterator myVeryOwnIterator = skuDetails.keySet().iterator();
        while(myVeryOwnIterator.hasNext()) {
            String key=(String)myVeryOwnIterator.next();
            SkuDetails value=(SkuDetails)skuDetails.get(key);
            Log.d("payment","Key: "+key+" Value: "+value.getSku());

        }
    }

    @Override
    public void onPurchasehistoryResponse(List<Purchase> purchasedItems) {
        if (purchasedItems != null) {
            for (Purchase purchase : purchasedItems) {
                //Update UI and backend according to purchased items if required
                // Like in this project I am updating UI for purchased items
                String sku = purchase.getSkus().get(0).toString();
            }
        }
    }







    @Override
    public void onPurchaseCompleted(Purchase purchase) {
        Toast.makeText(getApplicationContext(), "Purchase Successful", Toast.LENGTH_SHORT).show();
        //  callUpdate(id);
        // updatePurchase(purchase);
        int status =purchase.getPurchaseState();
        //  setAdsLocationOnOffApi("0", "1");

        planActive(USER_ID,planId);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iapHelper != null)
            iapHelper.endConnection();
    }

}