package com.bannet.skils.addposting.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bannet.skils.addposting.fragment.FragmentCropAddPosting;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.bannet.skils.Adapter.AdapterCity;
import com.bannet.skils.Adapter.AdapterCountry;
import com.bannet.skils.Adapter.AdapterPostImage;
import com.bannet.skils.Adapter.AdapterState;
import com.bannet.skils.post.responce.AddPostModel;
import com.bannet.skils.profile.responce.CityModel;
import com.bannet.skils.profile.responce.CountryModel;
import com.bannet.skils.post.responce.EditPostModel;
import com.bannet.skils.profile.responce.ImageUpload;
import com.bannet.skils.post.responce.PostImageModel;
import com.bannet.skils.profile.responce.StateModel;
import com.bannet.skils.postingdetails.responce.postingDetailsModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddPosting extends AppCompatActivity {

    Context context;
    ImageView backbtn;
    RelativeLayout img_select_skill;
    RelativeLayout addDistrict,addCountry,addState;
    EditText edit_post_title,edit_post_descr;
    TextView enterYourAddress;
    Dialog dialogbox;
    AppCompatButton closeDialoglocation,state_dialogbox_done,country_dialogbox_done,distric_dialogbox_done;
    RecyclerView recycler_country;
    RecyclerView recycler_city;
    RecyclerView recycler_state;
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
    String state_name="",city_name="",country_name="",opnamecity="";
    ProgressDialog progressDialog;
    TextView edit_country,edit_state,edit_city;
    String user_id,skill_id="",skill_name="",post_images="",lattitude,longitude;
    LinearLayout relative_submit;
    RecyclerView recycler_images;
    LinearLayout linear_take_img;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    Dialog camera_dialog;
    TextView txt_gallery, txt_take_photo,select_p;
    Button btn_cancel_dialog;
    String file_name = "",path;
    File finalFile;
    ImageView liveAddress;
    ArrayList<String> list = new ArrayList<>();
    List<PostImageModel> testModels = new ArrayList<>();
    AdapterPostImage testAdapter;
    AdapterPostImage.ItemClickListener itemClickListener;
    ArrayList<String> images_name = new ArrayList<>();
    TextView relative_slect_skill;
    String type,post_id="";
    TextView title,btn,or;
    String USER_ID,AddresValue;
    TextView sco,ss,sc,ap1,ap2,ap3,ap4;
    Resources resources;
    RelativeLayout plaseSerach;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posting);
        getSupportActionBar().hide();
        context= ActivityAddPosting.this;


        init();
        //callMultiplePermissions();

    }
    public void init(){

        type=getIntent().getStringExtra("type");
        post_id = getIntent().getStringExtra("post_id");

        ap1 = findViewById(R.id.ap1);


//        ap2 = findViewById(R.id.ap2);
//        ap3 = findViewById(R.id.ap3);
//        ap4 = findViewById(R.id.ap4);
//        or = findViewById(R.id.or);
//
        title = findViewById(R.id.postpage_title);
//        btn = findViewById(R.id.post_page_btn);
//        addDistrict = findViewById(R.id.profilepage_add_district);
//        addCountry = findViewById(R.id.profilepage_add_country);
//        addState = findViewById(R.id.profilepage_add_state);
//        edit_post_title = findViewById(R.id.edit_post_title);
//        edit_post_descr = findViewById(R.id.edit_post_descr);
//        edit_country = findViewById(R.id.edit_country);
//        edit_state = findViewById(R.id.edit_state);
//        edit_city = findViewById(R.id.edit_city);
//        relative_submit = findViewById(R.id.relative_submit);
        backbtn=findViewById(R.id.addposting_screen_back_icon);
//        img_select_skill =  findViewById(R.id.img_select_skill);
//        recycler_images =  findViewById(R.id.recycler_images);
//        linear_take_img =  findViewById(R.id.linear_take_img);
//        relative_slect_skill =  findViewById(R.id.relative_slect_skill);
//        enterYourAddress = findViewById(R.id.edit_address);
//        plaseSerach = findViewById(R.id.place_serach);
//        liveAddress = findViewById(R.id.liveAddress);
//
//        user_id = PrefConnect.readString(context,PrefConnect.USER_ID,"");
//        longitude=PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
//        lattitude=PrefConnect.readString(context,PrefConnect.LATITUDE,"");
//
//        or.setText(GlobalMethods.getString(context,R.string.or));
//        Places.initialize(getApplicationContext(), EndPoint.apiKey);
//
//        liveAddress.setOnClickListener(v -> {
//
//            enterYourAddress.setText(AddresValue);
//            edit_country.setText(GlobalMethods.getString(context,R.string.country_optional));
//            edit_state.setText(GlobalMethods.getString(context,R.string.state_optional));
//            edit_city.setText(GlobalMethods.getString(context,R.string.city_optional));
//            country_name = "";
//            state_name = "";
//            city_name = "";
//            country_id = "";
//            state_id = "";
//            city_id = "";
//        });
//
//        enterYourAddress.setOnClickListener(v -> {
//            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
//
//            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(context);
//
//            startActivityForResult(intent,100);
//        });
//
//
//
//        changeLanguage();
//
//        if(!type.equals("add_new_post")) {
//
//            getpostDetails(USER_ID,post_id);
//            Log.e("post_id",post_id);
//
//        }
//
//        img_select_skill.setOnClickListener(view -> {
//
//            Intent in = new Intent(context, ActivityAddskilsFromPostingPage.class);
//            in.putExtra("type","addpost");
//            startActivity(in);
//
//        });
//
//        relative_submit.setOnClickListener(view -> {
//
//            if(type.equals("add_new_post")){
//
//                if(validation()){
//                    callAddPost();
//                }
//
//            }
//            else{
//                updatepost(post_id);
//            }
//        });

        if(type.equals("add_new_post")) {

            title.setText(GlobalMethods.getString(context,R.string.add_posting));


        }else {

            title.setText(GlobalMethods.getString(context,R.string.edit_posting));


        }
        backbtn.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
            PrefConnect.writeString(context,PrefConnect.USER_SELECTED_SKIS_NAME,"");
            PrefConnect.writeString(context,PrefConnect.POST_ID,"");
        });
//
//        addDistrict.setOnClickListener(view -> {
//
//            enterYourAddress.setText("");
//            if(!state_name.equals("")){
//
//                addDistrict();
//
//            }else{
//
//                GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_state));
//
//            }
//
//        });
//
//        if(lattitude.isEmpty()){
//
//            String location = PrefConnect.readString(context,PrefConnect.ADDRESS,"");
//
//            if(location.isEmpty()){
//
//                enterYourAddress.setText(location);
//            }
//            else{
//
//                AddresValue = location;
//                enterYourAddress.setText(resources.getText(R.string.address));
//            }
//
//        }else {
//
//            GetAddressSOURCE getAddressSOURCE = new GetAddressSOURCE();
//            getAddressSOURCE.execute();
//
//        }
//
//        addCountry.setOnClickListener(view -> {
//            enterYourAddress.setText("");
//            addCountry();
//        });
//
//        addState.setOnClickListener(view -> {
//            enterYourAddress.setText("");
//            if(!country_name.equals("")){
//
//                addState();
//
//            }else{
//
//                GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_country));
//
//            }
//        });
//
//        linear_take_img.setOnClickListener(view -> setVerifiedDialog());
//
//        itemClickListener = new AdapterPostImage.ItemClickListener() {
//            @Override
//            public void ItemClick(ImageView image) {
//
//                try {
//
//                    image.setImageDrawable(null);
//                    GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Image_removed));
//
//                }
//
//                catch (Exception e){
//
//                    e.printStackTrace();
//                    GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Some_error_occurred));
//
//                }
//            }
//        };

        initMainFragment();
    }

    public void initMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle in = new Bundle();
        in.putString("type",type);
        in.putString("post_id",post_id);
        FragmentCropAddPosting mFragment = FragmentCropAddPosting.newInstance();
        mFragment.setArguments(in);
        transaction.replace(R.id.main_act_container, mFragment, mFragment.getClass().getSimpleName());
        transaction.commit();
    }

    private  boolean validation(){

        if(edit_post_title.getText().toString().isEmpty()){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.title));
            return false;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PrefConnect.writeString(context,PrefConnect.USER_SELECTED_SKIS_NAME,"");
        PrefConnect.writeString(context,PrefConnect.USER_SELECTED_SKIS_id,"");
        PrefConnect.writeString(context,PrefConnect.POST_ID,"");
    }

    private void getpostDetails(String user_id, String post_id ) {

        if (GlobalMethods.isNetworkAvailable(context)) {

            progressDialog = ProgressDialog.show(this, "", "Loading...", true);

            Api.getClient().postingDetails(user_id,post_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<postingDetailsModel>() {
                @Override
                public void onResponse(@NonNull Call<postingDetailsModel> call, @NonNull Response<postingDetailsModel> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){
                            edit_post_title.setText(response.body().getPostDetails().getTitle());
                            edit_post_descr.setText(response.body().getPostDetails().getDescription());
                            city_id=response.body().getPostDetails().getCityId();
                            state_id=response.body().getPostDetails().getStateId();
                            country_id=response.body().getPostDetails().getCountryId();
                            city_name=response.body().getPostDetails().getCity();
                            country_name=response.body().getPostDetails().getCountry();
                            state_name=response.body().getPostDetails().getState();
                            skill_id=response.body().getPostDetails().getSkillsId();
                            skill_name=response.body().getPostDetails().getSkillsName();
                            relative_slect_skill.setText(skill_name);
                            PrefConnect.writeString(context,PrefConnect.POST_ID,post_id);


                            if(response.body().getPostDetails().getAddress().equals("")){
                                edit_country.setText(GlobalMethods.getString(context,R.string.address));
                            }
                            else{
                                enterYourAddress.setText(response.body().getPostDetails().getAddress());
                            }

                            if(response.body().getPostDetails().getCountry().equals("")){
                                edit_country.setText(GlobalMethods.getString(context,R.string.country_optional));
                            }
                            else{
                                edit_country.setText(response.body().getPostDetails().getCountry());
                            }

                            if(response.body().getPostDetails().getCity().equals("")){
                                edit_city.setText(GlobalMethods.getString(context,R.string.city_optional));
                            }
                            else{
                                edit_city.setText(response.body().getPostDetails().getCity());
                            }
                            if(response.body().getPostDetails().getState().equals("")){
                                edit_state.setText(GlobalMethods.getString(context,R.string.state_optional));
                            }
                            else{
                                edit_state.setText(response.body().getPostDetails().getState());
                            }


                           String image = response.body().getPostDetails().getPostImages();
                           String imageurl = response.body().getPostDetails().getPostImageUrl();
                            Log.e("dfjud",image);
                            String[] arrSplit = image.split(",");


                            for (int i=0; i < arrSplit.length; i++)
                            {

                                String imageName=arrSplit[i];
                                String imgurl=imageurl + arrSplit[i];

                                testModels.add(new PostImageModel(imgurl,arrSplit[i]));

                                testAdapter = new AdapterPostImage(context,itemClickListener,testModels);
                                recycler_images.setHasFixedSize(true);
                                recycler_images.setLayoutManager(new LinearLayoutManager(ActivityAddPosting.this, LinearLayoutManager.HORIZONTAL, false));
                                recycler_images.setAdapter(testAdapter);

                            }

                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<postingDetailsModel> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    GlobalMethods.Toast(context,t.getMessage());
                }
            });

        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }

    private void updatepost(String post_id) {

        String address = enterYourAddress.getText().toString();
        Log.e("address",address);

        StringBuilder temp = new StringBuilder();
        for(int i=0;i<testModels.size();i++ ){

            temp.append(testModels.get(i).getImagename()).append(",");

        }
        Log.e("temp", String.valueOf(temp));
        post_images = String.valueOf(temp);
        if (GlobalMethods.isNetworkAvailable(ActivityAddPosting.this)) {

            progressDialog = ProgressDialog.show(ActivityAddPosting.this, "", "Loading...", true);
            Api.getClient().editPost(user_id,edit_post_title.getText().toString(),edit_post_descr.getText().toString(),
                    skill_id,country_name,state_name,city_name,post_images,lattitude,longitude,skill_name,post_id,country_id,city_id,state_id,address,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<EditPostModel>() {
                @Override
                public void onResponse(@NonNull Call<EditPostModel> call, @NonNull Response<EditPostModel> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){
                            GlobalMethods.Toast(context,response.body().getMessage());
                            PrefConnect.writeString(context,PrefConnect.POST_ID,"");
                            finish();
                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EditPostModel> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                }
            });

        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PrefConnect.writeString(context,PrefConnect.POST_ID,"");
    }

    @Override
    protected void onStop() {
        super.onStop();
        PrefConnect.writeString(context,PrefConnect.POST_ID,"");
    }

    private void addCountry() {
        countryModels = new ArrayList<>();
        dialogbox= new Dialog(ActivityAddPosting.this);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_addcountry);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.show();
        dialogbox.setCancelable(false);
        sco =  dialogbox.findViewById(R.id.sco);
        recycler_country =  dialogbox.findViewById(R.id.recycler_country);
        closeDialoglocation= dialogbox.findViewById(R.id.country_dialogbox_close);
        country_dialogbox_done =  dialogbox.findViewById(R.id.country_dialogbox_done);


        country_dialogbox_done.setOnClickListener(v -> {

            dialogbox.dismiss();
            getlatandlag(country_name);

        });
        closeDialoglocation.setOnClickListener(view -> {
            dialogbox.dismiss();
            country_id = "";
            country_name = "";
        });

        progressDialog = ProgressDialog.show(ActivityAddPosting.this, "", "Loading...", true);
        Api.getClient().getCountry(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CountryModel>() {
            @Override
            public void onResponse(@NonNull Call<CountryModel> call, @NonNull Response<CountryModel> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){
                        countryModels = response.body().getCountryList();
                        adapterCountry = new AdapterCountry(context,itemClickListener_country,countryModels);
                        recycler_country.setHasFixedSize(true);
                        recycler_country.setLayoutManager(new LinearLayoutManager(context));
                        recycler_country.setAdapter(adapterCountry);
                    }else {
                        GlobalMethods.Toast(context,response.body().getMessage());
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<CountryModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
        changeLanguageCountry();

        try{
            itemClickListener_country = (countr_id, country_n) -> {

                country_id = countr_id;
                country_name = country_n;
                edit_country.setText(country_name);

            };
        }catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void addDistrict() {
        dialogbox= new Dialog(ActivityAddPosting.this);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_add_district);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.setCancelable(false);
        sc =  dialogbox.findViewById(R.id.sc);
        recycler_city =  dialogbox.findViewById(R.id.recycler_city);
        distric_dialogbox_done =  dialogbox.findViewById(R.id.distric_dialogbox_done);
        closeDialoglocation= dialogbox.findViewById(R.id.distric_dialogbox_close);
        TextView other_option=dialogbox.findViewById(R.id.other_option);
        LinearLayout city_layout=dialogbox.findViewById(R.id.city_layout);
        EditText othersCity=dialogbox.findViewById(R.id.others_txt_city);

        other_option.setOnClickListener(v -> city_layout.setVisibility(View.VISIBLE));

        closeDialoglocation.setOnClickListener(view -> dialogbox.dismiss());

        distric_dialogbox_done.setOnClickListener(view -> {

            dialogbox.dismiss();
            opnamecity=othersCity.getText().toString();

            if(opnamecity.equals("")){

                edit_city.setText(city_name);
                getlatandlag(city_name);

            }else {

                city_name=opnamecity;
                edit_city.setText(city_name);
                getlatandlag(city_name);

            }


        });
        progressDialog = ProgressDialog.show(ActivityAddPosting.this, "", "Loading...", true);
        Api.getClient().getCity(state_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(@NonNull Call<CityModel> call, @NonNull Response<CityModel> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()){

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        dialogbox.show();
                        cityModels = response.body().getCityList();
                        adapterCity = new AdapterCity(context,itemClickListener_city,cityModels);
                        recycler_city.setHasFixedSize(true);
                        recycler_city.setLayoutManager(new LinearLayoutManager(context));
                        recycler_city.setAdapter(adapterCity);

                    }else {

                        dialogbox.show();
                        GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CityModel> call, @NonNull Throwable t) {

                progressDialog.dismiss();
                dialogbox.dismiss();

            }
        });


        try {

            itemClickListener_city = (id, city_n) -> {

                city_id = id;
                city_name = city_n;


            };

        }catch (Exception e){

            e.printStackTrace();

        }

        changeLanguageCity();

    }

    private void addState() {

        dialogbox= new Dialog(ActivityAddPosting.this);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_addstate);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.setCancelable(false);
        ss =  dialogbox.findViewById(R.id.ss);
        closeDialoglocation =  dialogbox.findViewById(R.id.state_dialogbox_close);
        state_dialogbox_done =  dialogbox.findViewById(R.id.state_dialogbox_done);
        recycler_state =  dialogbox.findViewById(R.id.recycler_state);

        state_dialogbox_done.setOnClickListener(view -> {

            dialogbox.dismiss();
            getlatandlag(state_name);

        });

        try {

            itemClickListener_state = (id, stateName) -> {

                dialogbox.dismiss();
                state_id = id;
                state_name = stateName;
                edit_state.setText(stateName);

            };
        } catch (Exception e) {

            e.printStackTrace();

        }

        closeDialoglocation.setOnClickListener(view -> dialogbox.dismiss());

        changeLanguageState();

        progressDialog = ProgressDialog.show(ActivityAddPosting.this, "", "Loading...", true);
        Api.getClient().getState(country_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<StateModel>() {
            @Override
            public void onResponse(@NonNull Call<StateModel> call, @NonNull Response<StateModel> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()){

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        dialogbox.show();
                        stateModels = response.body().getStatesList();
                        adapterState = new AdapterState(context,itemClickListener_state,stateModels);
                        recycler_state.setHasFixedSize(true);
                        recycler_state.setLayoutManager(new LinearLayoutManager(context));
                        recycler_state.setAdapter(adapterState);

                    }else {

                        dialogbox.dismiss();
                        GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<StateModel> call, @NonNull Throwable t) {

                dialogbox.dismiss();
                progressDialog.dismiss();

            }
        });

        try{

            itemClickListener_state = (id, stat_n) -> {

                state_id = id;
                state_name = stat_n;
                edit_state.setText(state_name);

            };

        }catch (Exception e){

            e.printStackTrace();

        }

    }


    @SuppressLint("StaticFieldLeak")
    public class GetAddressSOURCE extends AsyncTask<String, String, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+lattitude+","+longitude+"&key=AIzaSyBEQZrGethx6g_XIplm9otgQUk1pnzzWoM";


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
            Log.e("result", s + "::");

            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                AddresValue=jsonObject1.getString("formatted_address");

                if(type.equals("add_new_post")) {

                    enterYourAddress.setText(AddresValue);

                }

            } catch (JSONException e) {

                e.printStackTrace();


            }

        }
    }


    public void callAddPost(){

        if (GlobalMethods.isNetworkAvailable(ActivityAddPosting.this)){
            Log.e("user_id",user_id);
            Log.e("user_id",edit_post_title.getText().toString());
            Log.e("user_id",edit_post_descr.getText().toString());
            Log.e("user_id",country_name);
            Log.e("user_id",state_name);
            Log.e("user_id",city_name);
            Log.e("user_id",post_images);
            Log.e("user_id",lattitude);
            Log.e("user_id",longitude);
            Log.e("user_id",skill_name);
            Log.e("user_id",country_id);
            Log.e("user_id",state_id);
            Log.e("user_id",city_id);
            String address = enterYourAddress.getText().toString();
            Log.e("address",address);

            StringBuilder temp = new StringBuilder();
            for(int i=0;i<testModels.size();i++ ){

                temp.append(testModels.get(i).getImagename()).append(",");

            }
            Log.e("temp", String.valueOf(temp));
            post_images = String.valueOf(temp);

            progressDialog = ProgressDialog.show(ActivityAddPosting.this, "", "Loading...", true);
            Api.getClient().addNewPost(user_id,edit_post_title.getText().toString(),edit_post_descr.getText().toString(),
                    skill_id,country_name,state_name,city_name,post_images,lattitude,longitude,skill_name,country_id,state_id,city_id,address,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<AddPostModel>() {
                @Override
                public void onResponse(@NonNull Call<AddPostModel> call, @NonNull Response<AddPostModel> response) {

                    Log.e("add post success",new Gson().toJson(response.body()));
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            GlobalMethods.Toast(context,response.body().getMessage());
                            finish();

                        }else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AddPostModel> call, @NonNull Throwable t) {

                    progressDialog.dismiss();

                }
            });

        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    private void setVerifiedDialog() {
        camera_dialog = new Dialog(this);
        camera_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        camera_dialog.setContentView(R.layout.dialog_photo);
        camera_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        camera_dialog.show();
        camera_dialog.setCancelable(false);
        txt_gallery =  camera_dialog.findViewById(R.id.txt_gallery);
        txt_take_photo =  camera_dialog.findViewById(R.id.txt_take_photo);
        select_p =  camera_dialog.findViewById(R.id.select_p);
        btn_cancel_dialog =  camera_dialog.findViewById(R.id.btn_cancel_dialog);

        btn_cancel_dialog.setOnClickListener(view -> camera_dialog.dismiss());
        txt_gallery.setOnClickListener(view -> {
            camera_dialog.dismiss();
            galleryIntent();
        });

        txt_take_photo.setOnClickListener(view -> {
            camera_dialog.dismiss();
            cameraIntent();
        });
        languagechangecamara();
    }

    private void cameraIntent() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityIfNeeded(cameraIntent, 0);

    }

    private void galleryIntent() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityIfNeeded(pickPhoto , 1);

//        Intent pickPhoto = new Intent(Intent.ACTION_PICK);
//        pickPhoto.setType("image/*"); // Filter to only show image files
//        startActivityForResult(pickPhoto, 1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){

                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");

                    Uri tempUri = getImageUri(context, photo);

                    if(tempUri == null){


                    }
                    else{

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        finalFile = new File(getRealPathFromURI(tempUri));
                        file_name = finalFile.getName();
                        path = finalFile+"";
                        list.add(path);

                        Log.e("path",path);
                        uploadFile();

                    }

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();

                    finalFile = new File(getRealPathFromURI(selectedImage));
                    Log.e("path case 1 :",finalFile+" siva");
                    file_name = finalFile.getName();

                    Bitmap fullSizeBitMap = BitmapFactory.decodeFile(finalFile+"");

                    int imageRotation = getImageRotation(finalFile);
                    if (imageRotation != 0)
                        fullSizeBitMap = getBitmapRotatedByDegree(fullSizeBitMap, imageRotation);

                    Uri tempUri = null;
                    if(fullSizeBitMap != null){

                        tempUri = getImageUri(context, fullSizeBitMap);

                    }

                    if(tempUri != null){

                        finalFile = new File(getRealPathFromURI(tempUri));
                        Log.e("path case 0 :",finalFile+" siva");
                        file_name = finalFile.getName();
                        path = finalFile+"";
                        list.add(path);
                        uploadFile();
                    }
                }
                break;

            case 100:

                if(resultCode == RESULT_OK){

                    Log.e("place","pla");

                    Place place = Autocomplete.getPlaceFromIntent(imageReturnedIntent);

                    enterYourAddress.setText(place.getAddress());
                    getlatandlag(place.getAddress());
                    edit_country.setText(GlobalMethods.getString(context,R.string.country_optional));
                    edit_state.setText(GlobalMethods.getString(context,R.string.state_optional));
                    edit_city.setText(GlobalMethods.getString(context,R.string.city_optional));
                    country_name = "";
                    state_name = "";
                    city_name = "";
                    country_id = "";
                    state_id = "";
                    city_id = "";
                }
                break;

        }
    }

    private static int getImageRotation(final File imageFile) {

        ExifInterface exif = null;
        int exifRotation = 0;

        try {
            exif = new ExifInterface(imageFile.getPath());
            exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif == null)
            return 0;
        else
            return exifToDegrees(exifRotation);
    }

    private static int exifToDegrees(int rotation) {
        if (rotation == ExifInterface.ORIENTATION_ROTATE_90)
            return 90;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_180)
            return 180;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_270)
            return 270;

        return 0;
    }

    private static Bitmap getBitmapRotatedByDegree(Bitmap bitmap, int rotationDegree) {
        Matrix matrix = new Matrix();
        matrix.preRotate(rotationDegree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    private void uploadFile() {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(path);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file_name", file.getName(), requestBody);

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), "post_images");
        progressDialog = ProgressDialog.show(ActivityAddPosting.this, "", "Loading...", true);
        Api.getClient().profileImageUpload(fileToUpload,filename).enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(@NonNull Call<ImageUpload> call, @NonNull Response<ImageUpload> response) {
                progressDialog.dismiss();
                assert response.body() != null;
                if(response.body().getStatus().equals("1")){
                   String img = response.body().getFileName();
                    testModels.add(new PostImageModel(response.body().getFileUrl(),img));
                    images_name.add(img);
                    String formattedString = images_name.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "").
                             replace(" ","")//remove the left bracket
                            .trim();
                    Log.e("selected img",formattedString +"test");

                    testAdapter = new AdapterPostImage(context,itemClickListener,testModels);
                    recycler_images.setHasFixedSize(true);
                    recycler_images.setLayoutManager(new LinearLayoutManager(ActivityAddPosting.this, LinearLayoutManager.HORIZONTAL, false));
                    recycler_images.setAdapter(testAdapter);

                }else {
                    progressDialog.dismiss();
                    GlobalMethods.Toast(context,response.body().getMessage()+"");
                }

            }
            @Override
            public void onFailure(@NonNull Call<ImageUpload> call, @NonNull Throwable t) {

                progressDialog.dismiss();

            }
        });

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void callMultiplePermissions() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();

        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("ACCESS_FINE_LOCATION");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("ACCESS_COARSE_LOCATION");
        if (!addPermission(permissionsList, Manifest.permission.READ_MEDIA_IMAGES))
            permissionsNeeded.add("READ_MEDIA_IMAGES");
        if (!addPermission(permissionsList, Manifest.permission.READ_MEDIA_VIDEO))
            permissionsNeeded.add("READ_MEDIA_VIDEO");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }

                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_MEDIA_IMAGES, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_MEDIA_VIDEO, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);


                if (perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {

                    Toast.makeText(context, "Permissin is denied", Toast.LENGTH_SHORT).show();

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            if(PrefConnect.readString(context,PrefConnect.USER_SELECTED_SKIS_NAME,"").isEmpty()){

                if(!type.equals("add_new_post")) {



                }
                relative_slect_skill.setText(GlobalMethods.getString(context,R.string.selectSkills));

            }
            else{

                skill_name = PrefConnect.readString(context,PrefConnect.USER_SELECTED_SKIS_NAME,"");
                skill_id = PrefConnect.readString(context,PrefConnect.USER_SELECTED_SKIS_id,"");
                relative_slect_skill.setText(PrefConnect.readString(context,PrefConnect.USER_SELECTED_SKIS_NAME,""));


            }

            PrefConnect.writeString(context,PrefConnect.USER_SELECTED_SKIS_NAME,"");
            PrefConnect.writeString(context,PrefConnect.USER_SELECTED_SKIS_id,"");


        }catch (Exception e){

            e.printStackTrace();

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
                Log.e("lit", String.valueOf(lattitude));
                Log.e("lon", String.valueOf(longitude));


            }
        } catch (IOException e) {

            e.printStackTrace();

        }


    }

    private void changeLanguage() {

        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            ap1.setText(resources.getText(R.string.title));
            ap2.setText(resources.getText(R.string.description));
            ap3.setText(resources.getText(R.string.selectSkills));
            ap4.setText(resources.getText(R.string.add_photos));
            edit_city.setText(resources.getText(R.string.select_city));
            edit_country.setText(resources.getText(R.string.select_country));
            edit_state.setText(resources.getText(R.string.select_state));
            edit_post_title.setHint(resources.getText(R.string.title));
            edit_post_descr.setHint(resources.getText(R.string.description));
            enterYourAddress.setHint(resources.getText(R.string.address));


            if(type.equals("add_new_post")) {

                title.setText(resources.getText(R.string.add_posting));
                btn.setText(resources.getText(R.string.submit));

            }else {

                title.setText(resources.getText(R.string.edit_posting));
                btn.setText(resources.getText(R.string.UPDATE));

            }

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {

            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            ap1.setText(resources.getText(R.string.title));
            ap2.setText(resources.getText(R.string.description));
            ap3.setText(resources.getText(R.string.selectSkills));
            ap4.setText(resources.getText(R.string.add_photos));
            edit_city.setText(resources.getText(R.string.select_city));
            edit_country.setText(resources.getText(R.string.select_country));
            edit_state.setText(resources.getText(R.string.select_state));
            edit_post_title.setHint(resources.getText(R.string.title));
            edit_post_descr.setHint(resources.getText(R.string.description));
            enterYourAddress.setHint(resources.getText(R.string.address));

            if(type.equals("add_new_post")) {

                title.setText(resources.getText(R.string.add_posting));
                btn.setText(resources.getText(R.string.submit));

            }else {

                title.setText(resources.getText(R.string.edit_posting));
                btn.setText(resources.getText(R.string.UPDATE));

            }

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {

            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            ap1.setText(resources.getText(R.string.title));
            ap2.setText(resources.getText(R.string.description));
            ap3.setText(resources.getText(R.string.selectSkills));
            ap4.setText(resources.getText(R.string.add_photos));
            edit_city.setText(resources.getText(R.string.select_city));
            edit_country.setText(resources.getText(R.string.select_country));
            edit_state.setText(resources.getText(R.string.select_state));
            edit_post_title.setHint(resources.getText(R.string.title));
            edit_post_descr.setHint(resources.getText(R.string.description));
            enterYourAddress.setHint(resources.getText(R.string.address));

            if(type.equals("add_new_post")) {

                title.setText(resources.getText(R.string.add_posting));
                btn.setText(resources.getText(R.string.submit));

            }else {

                title.setText(resources.getText(R.string.edit_posting));
                btn.setText(resources.getText(R.string.UPDATE));

            }


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {

            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            ap1.setText(resources.getText(R.string.title));
            ap2.setText(resources.getText(R.string.description));
            ap3.setText(resources.getText(R.string.selectSkills));
            ap4.setText(resources.getText(R.string.add_photos));
            edit_city.setText(resources.getText(R.string.select_city));
            edit_country.setText(resources.getText(R.string.select_country));
            edit_state.setText(resources.getText(R.string.select_state));
            edit_post_title.setHint(resources.getText(R.string.title));
            edit_post_descr.setHint(resources.getText(R.string.description));
            enterYourAddress.setHint(resources.getText(R.string.address));

            if(type.equals("add_new_post")) {
                title.setText(resources.getText(R.string.add_posting));
                btn.setText(resources.getText(R.string.submit));

            }else {
                title.setText(resources.getText(R.string.edit_posting));
                btn.setText(resources.getText(R.string.UPDATE));

            }

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            ap1.setText(resources.getText(R.string.title));
            ap2.setText(resources.getText(R.string.description));
            ap3.setText(resources.getText(R.string.selectSkills));
            ap4.setText(resources.getText(R.string.add_photos));
            edit_city.setText(resources.getText(R.string.select_city));
            edit_country.setText(resources.getText(R.string.select_country));
            edit_state.setText(resources.getText(R.string.select_state));
            edit_post_title.setHint(resources.getText(R.string.title));
            edit_post_descr.setHint(resources.getText(R.string.description));
            enterYourAddress.setHint(resources.getText(R.string.address));

            if(type.equals("add_new_post")) {
                title.setText(resources.getText(R.string.add_posting));
                btn.setText(resources.getText(R.string.submit));

            }else {
                title.setText(resources.getText(R.string.edit_posting));
                btn.setText(resources.getText(R.string.UPDATE));

            }



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            ap1.setText(resources.getText(R.string.title));
            ap2.setText(resources.getText(R.string.description));
            ap3.setText(resources.getText(R.string.selectSkills));
            ap4.setText(resources.getText(R.string.add_photos));
            edit_city.setText(resources.getText(R.string.select_city));
            edit_country.setText(resources.getText(R.string.select_country));
            edit_state.setText(resources.getText(R.string.select_state));
            edit_post_title.setHint(resources.getText(R.string.title));
            edit_post_descr.setHint(resources.getText(R.string.description));
            enterYourAddress.setHint(resources.getText(R.string.address));

            if(type.equals("add_new_post")) {
                title.setText(resources.getText(R.string.add_posting));
                btn.setText(resources.getText(R.string.submit));

            }else {
                title.setText(resources.getText(R.string.edit_posting));
                btn.setText(resources.getText(R.string.UPDATE));

            }


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            ap1.setText(resources.getText(R.string.title));
            ap2.setText(resources.getText(R.string.description));
            ap3.setText(resources.getText(R.string.selectSkills));
            ap4.setText(resources.getText(R.string.add_photos));
            edit_city.setText(resources.getText(R.string.select_city));
            edit_country.setText(resources.getText(R.string.select_country));
            edit_state.setText(resources.getText(R.string.select_state));
            edit_post_title.setHint(resources.getText(R.string.title));
            edit_post_descr.setHint(resources.getText(R.string.description));
            enterYourAddress.setHint(resources.getText(R.string.address));

            if(type.equals("add_new_post")) {
                title.setText(resources.getText(R.string.add_posting));
                btn.setText(resources.getText(R.string.submit));

            }else {
                title.setText(resources.getText(R.string.edit_posting));
                btn.setText(resources.getText(R.string.UPDATE));

            }

        }
    }
    private void changeLanguageCountry() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));
            closeDialoglocation.setText(resources.getText(R.string.close));
            country_dialogbox_done.setText(resources.getText(R.string.done));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));
            closeDialoglocation.setText(resources.getText(R.string.close));
            country_dialogbox_done.setText(resources.getText(R.string.done));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));
            closeDialoglocation.setText(resources.getText(R.string.close));
            country_dialogbox_done.setText(resources.getText(R.string.done));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));
            closeDialoglocation.setText(resources.getText(R.string.close));
            country_dialogbox_done.setText(resources.getText(R.string.done));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));
            closeDialoglocation.setText(resources.getText(R.string.close));
            country_dialogbox_done.setText(resources.getText(R.string.done));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));
            closeDialoglocation.setText(resources.getText(R.string.close));
            country_dialogbox_done.setText(resources.getText(R.string.done));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            sco.setText(resources.getText(R.string.select_country));
            closeDialoglocation.setText(resources.getText(R.string.close));
            country_dialogbox_done.setText(resources.getText(R.string.done));


        }
    }
    private void changeLanguageState() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));
            closeDialoglocation.setText(resources.getText(R.string.close));
            state_dialogbox_done.setText(resources.getText(R.string.done));





        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));
            closeDialoglocation.setText(resources.getText(R.string.close));
            state_dialogbox_done.setText(resources.getText(R.string.done));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));
            closeDialoglocation.setText(resources.getText(R.string.close));
            state_dialogbox_done.setText(resources.getText(R.string.done));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));
            closeDialoglocation.setText(resources.getText(R.string.close));
            state_dialogbox_done.setText(resources.getText(R.string.done));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));
            closeDialoglocation.setText(resources.getText(R.string.close));
            state_dialogbox_done.setText(resources.getText(R.string.done));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));
            closeDialoglocation.setText(resources.getText(R.string.close));
            state_dialogbox_done.setText(resources.getText(R.string.done));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            ss.setText(resources.getText(R.string.select_state));
            closeDialoglocation.setText(resources.getText(R.string.close));
            state_dialogbox_done.setText(resources.getText(R.string.done));


        }
    }
    private void changeLanguageCity() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            closeDialoglocation.setText(resources.getText(R.string.close));
            distric_dialogbox_done.setText(resources.getText(R.string.done));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            closeDialoglocation.setText(resources.getText(R.string.close));
            distric_dialogbox_done.setText(resources.getText(R.string.done));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            closeDialoglocation.setText(resources.getText(R.string.close));
            distric_dialogbox_done.setText(resources.getText(R.string.done));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            closeDialoglocation.setText(resources.getText(R.string.close));
            distric_dialogbox_done.setText(resources.getText(R.string.done));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            closeDialoglocation.setText(resources.getText(R.string.close));
            distric_dialogbox_done.setText(resources.getText(R.string.done));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            closeDialoglocation.setText(resources.getText(R.string.close));
            distric_dialogbox_done.setText(resources.getText(R.string.done));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            closeDialoglocation.setText(resources.getText(R.string.close));
            distric_dialogbox_done.setText(resources.getText(R.string.done));



        }
    }
    private void languagechangecamara() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));
        }

    }





}