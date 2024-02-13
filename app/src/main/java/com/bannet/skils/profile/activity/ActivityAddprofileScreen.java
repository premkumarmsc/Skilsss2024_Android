package com.bannet.skils.profile.activity;

import androidx.annotation.NonNull;
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
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.profile.CropFragment.FragmentCropAddProfile;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.bannet.skils.Adapter.AdapterCertificationsNew;
import com.bannet.skils.Adapter.AdapterCity;
import com.bannet.skils.Adapter.AdapterCountry;
import com.bannet.skils.Adapter.AdapterState;
import com.bannet.skils.profile.responce.CertificateList;
import com.bannet.skils.profile.responce.CityModel;
import com.bannet.skils.profile.responce.CountryModel;
import com.bannet.skils.profile.responce.ImageUpload;
import com.bannet.skils.profile.responce.StateModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

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

public class ActivityAddprofileScreen extends AppCompatActivity {
    Context context;
    LinearLayout nextbtn;
    ImageView img_back;
    RelativeLayout relative_add_certification;
    ImageView profile_image_view;
    Dialog dialog;
    AppCompatButton ok,cancel;

    RelativeLayout addDistrict,addCountry,addState;
    Dialog dialogbox;
    AppCompatButton certificate_dialogbox_done,certificate_dialogbox_close;
    AppCompatButton closeDialoglocation,state_dialogbox_done,country_dialogbox_done,distric_dialogbox_done;
    RecyclerView recycler_country;
    RecyclerView recycler_city;
    RecyclerView recycler_state;
    public GpsMyLocationProvider  provider;

    Resources resources;

    TextView txt_gallery,txt_take_photo,select_p,c1,or;
    Button btn_cancel_dialog;
    String file_name = "",path;
    File finalFile;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    EditText tfirstName,tlastName,tcompanyName,temailId,tpassword,tdescription;
    EditText tReferCode;
    TextView enterYourAddress;
    String fname,lname,cname,emailid,password,description,certification_name;

    ProgressDialog progressDialog;

    String user_id;
    String file_name_profile = "";
    TextView a_p_s_tv1,a_p_s_tv2,a_p_s_tv3,a_p_s_tv4,a_p_s_tv5,a_p_s_tv6,title;
    AdapterCountry adapterCountry;
    AdapterCountry.ItemClickListener itemClickListener_country;
    List<CountryModel.Country> countryModels;
    AdapterCity adapterCity;
    AdapterCity.ItemClickListener itemClickListener_city;
    List<CityModel.City> cityModels;
    AdapterState adapterState;
    AdapterState.ItemClickListener itemClickListener_state;
    List<StateModel.States> stateModels;
    String state_id,city_id,country_id,certifcate_id="";
    String State_name="",City_name="",Country_name="",opnamecity="";

    String  deviceModel="", deviceVersion="", deviceType="", deviceToken = "";
    AdapterCertificationsNew adapterCertificationsNew;
    AdapterCertificationsNew.ItemClickListener itemClickListenerCertificates;
    List<CertificateList.Certificate> certificateLists;
    String longitude,lattitude;
    String AddresValue;
    ImageView livelocation;

    public TextView h1,h2,h3,h4,h5,h6,h7,h8,h9;
    TextView sc,sco,ss;

    int PERMISSION_ID = 44;
    private LinearLayout codeLayout;
    public String code;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addprofile_screen);
        getSupportActionBar().hide();
        FirebaseApp.initializeApp(this);
        context=ActivityAddprofileScreen.this;


        longitude=PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude=PrefConnect.readString(context,PrefConnect.LATITUDE,"");


        code = getIntent().getStringExtra("code");
        user_id = getIntent().getStringExtra("user_id");

        a_p_s_tv1 = findViewById(R.id.a_p_s_tv1);
        a_p_s_tv1.setText(GlobalMethods.getString(context,R.string.addProfile));
//        a_p_s_tv2 = findViewById(R.id.a_p_s_tv2);
//        a_p_s_tv3 = findViewById(R.id.a_p_s_tv3);
//        a_p_s_tv4 = findViewById(R.id.a_p_s_tv4);
//        a_p_s_tv5 = findViewById(R.id.a_p_s_tv5);
//        a_p_s_tv6 = findViewById(R.id.a_p_s_tv6);
//        codeLayout = findViewById(R.id.code_layout);
//        or = findViewById(R.id.or);
//        livelocation = findViewById(R.id.livelocation);
//        tfirstName = findViewById(R.id.enter_your_first_name);
//        tlastName = findViewById(R.id.enter_your_last_name);
//        tcompanyName = findViewById(R.id.enter_your_company_name);
//        temailId = findViewById(R.id.enter_your_email_id);
//        tpassword = findViewById(R.id.enter_your_password);
//        tdescription = findViewById(R.id.enter_your_description);
//        enterYourAddress = findViewById(R.id.enter_your_address);
//        tReferCode = findViewById(R.id.edi_code);
//        addDistrict = findViewById(R.id.profilepage_add_district);
//        addCountry = findViewById(R.id.profilepage_add_country);
//        addState = findViewById(R.id.profilepage_add_state);
//        profile_image_view = findViewById(R.id.profile_image_show);
//        nextbtn = findViewById(R.id.update_btn);
//        relative_add_certification =  findViewById(R.id.relative_add_certification);
        img_back = findViewById(R.id.img_back);
//
//        callMultiplePermissions();
//
//        h1 = findViewById(R.id.h1);
//        h2 = findViewById(R.id.h2);
//        h3 = findViewById(R.id.h3);
//        h4 = findViewById(R.id.h4);
//        h5 = findViewById(R.id.h5);
//        h6 = findViewById(R.id.h6);
//        h7 = findViewById(R.id.h7);
//        h8 = findViewById(R.id.h8);
//        h9 = findViewById(R.id.h9);
//
//        or.setText(GlobalMethods.getString(context,R.string.or));
//
//        h1.setText(GlobalMethods.getString(context,R.string.edi_firstname));
//        h2.setText(GlobalMethods.getString(context,R.string.edi_lastname));
//        h3.setText(GlobalMethods.getString(context,R.string.edi_company));
//        h4.setText(GlobalMethods.getString(context,R.string.edi_email));
//        h5.setText(GlobalMethods.getString(context,R.string.password));
//        h6.setText(GlobalMethods.getString(context,R.string.referral_code));
//        h7.setText(GlobalMethods.getString(context,R.string.address));
//        h8.setText(GlobalMethods.getString(context,R.string.certification));
//        h9.setText(GlobalMethods.getString(context,R.string.description));
//
//
//        if(code.equals("0")){
//
//            codeLayout.setVisibility(View.VISIBLE);
//
//        }
//        else {
//
//            codeLayout.setVisibility(View.GONE);
//
//        }
//
//        longitude = PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
//        lattitude = PrefConnect.readString(context,PrefConnect.LATITUDE,"");
//
//
//        livelocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                enterYourAddress.setText(AddresValue);
//                a_p_s_tv2.setText(GlobalMethods.getString(context,R.string.country_optional));
//                a_p_s_tv3.setText(GlobalMethods.getString(context,R.string.state_optional));
//                a_p_s_tv4.setText(GlobalMethods.getString(context,R.string.city_optional));
//                country_id = "";
//                state_id = "";
//                city_id = "";
//                Country_name = "";
//                State_name = "";
//                City_name = "";
//
//            }
//        });
//
//        Places.initialize(getApplicationContext(), EndPoint.apiKey);
//        enterYourAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
//
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(context);
//
//                startActivityForResult(intent,100);
//            }
//        });

        img_back.setOnClickListener(view -> {

            dialog = new Dialog(ActivityAddprofileScreen.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_otp_back_screen);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            title=dialog.findViewById(R.id.title);
            cancel =  dialog.findViewById(R.id.cancel);
            ok =  dialog.findViewById(R.id.ok);
            cancel.setOnClickListener(v -> dialog.dismiss());
            ok.setOnClickListener(v -> {
                dialog.dismiss();

                startActivity(new Intent(context, ActivityPhonenumberScreen.class));
                finish();

            });

            changeLanguageotp();

        });



//        deviceToken=PrefConnect.readString(context,PrefConnect.DEVICE_TOKEN,"");
//        deviceType=android.os.Build.MANUFACTURER+"";
//        deviceModel = Build.MODEL+"";
//        deviceVersion =  android.os.Build.VERSION.RELEASE+"";
//
//        profile_image_view.setOnClickListener(view -> imageSelect());
//
//
//
//        nextbtn.setOnClickListener(view -> {
//
//            if(validation()){
//
//                fname=tfirstName.getText().toString().trim();
//                lname=tlastName.getText().toString().trim();
//                cname=tcompanyName.getText().toString().trim();
//                emailid=temailId.getText().toString().trim();
//                password=tpassword.getText().toString().trim();
//                description=tdescription.getText().toString().trim();
//
//                Intent in = new Intent(context, ActivityAddskils.class);
//                in.putExtra("type","profile_complected");
//                in.putExtra("first_name",fname);
//                in.putExtra("last_name",lname);
//                in.putExtra("image_name",file_name_profile);
//                in.putExtra("company_name",cname);
//                in.putExtra("country",country_id);
//                in.putExtra("state",state_id);
//                in.putExtra("city",city_id);
//                in.putExtra("country_name",Country_name);
//                in.putExtra("state_name",State_name);
//                in.putExtra("city_name",City_name);
//                in.putExtra("about",description);
//                in.putExtra("email",emailid);
//                in.putExtra("password",password);
//                in.putExtra("device_type",deviceType);
//                in.putExtra("device_token",deviceToken);
//                in.putExtra("device_model",deviceModel);
//                in.putExtra("device_version",deviceVersion);
//                in.putExtra("gender","1");
//                in.putExtra("certificate_id",certification_name);
//                in.putExtra("longitude",longitude);
//                in.putExtra("lattitude",lattitude);
//                in.putExtra("refercode",tReferCode.getText().toString());
//                in.putExtra("address",enterYourAddress.getText().toString());
//                in.putExtra("code",code);
//                in.putExtra("user_id",user_id);
//                startActivity(in);
//                finish();
//            }
//
//        });
//
//        relative_add_certification.setOnClickListener(view -> {
//            //startActivity(new Intent(context, ActivityAddCertificatins.class));
//            addCertificate();
//        });
//        addDistrict.setOnClickListener(view -> {
//            enterYourAddress.setText("");
//            if(!State_name.equals("")){
//
//                addDistrict();
//
//            }else{
//
//                GlobalMethods.Toast(context,"Please select your state");
//
//            }
//
//
//        });
//        addCountry.setOnClickListener(view -> {
//
//            enterYourAddress.setText("");
//            addCountry();
//        });
//
//        addState.setOnClickListener(view -> {
//            enterYourAddress.setText("");
//            if(!Country_name.equals("")){
//
//                addState();
//
//            }else{
//
//                GlobalMethods.Toast(context,"Please select your country");
//
//            }
//
//        });
//
//        if(longitude.isEmpty()){
//
//            getCurrentLocation();
//
//        }
//        else {
//
//            GetAddressSOURCE getAddressSOURCE = new GetAddressSOURCE();
//            getAddressSOURCE.execute();
//
//        }
//
//
//        languageChange();

        initMainFragment();
    }


    public void initMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        bundle.putString("user_id", user_id);
        FragmentCropAddProfile mFragment = FragmentCropAddProfile.newInstance();
        mFragment.setArguments(bundle);
        transaction.replace(R.id.main_act_container, mFragment, mFragment.getClass().getSimpleName());
        transaction.commit();
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

            GetAddressSOURCE getAddressSOURCE = new GetAddressSOURCE();
            getAddressSOURCE.execute();

        });

    }

    public boolean validation(){

        fname=tfirstName.getText().toString().trim();
        lname=tlastName.getText().toString().trim();
        cname=tcompanyName.getText().toString().trim();
        emailid=temailId.getText().toString().trim();
        password=tpassword.getText().toString().trim();
        description=tdescription.getText().toString().trim();

        if(fname.isEmpty()){
            Toast.makeText(context,GlobalMethods.getString(context,R.string.enter_your_first_name), Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(lname.isEmpty()){
            Toast.makeText(context, GlobalMethods.getString(context,R.string.enter_your_first_name), Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if (emailid.isEmpty()) {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Email_Required));
            return false;
        }
        else if (!emailValidation(emailid)) {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.please_valid_e_mail));
            return false;
        }
        else if(password.isEmpty()){
            Toast.makeText(context, GlobalMethods.getString(context,R.string.Password_Required), Toast.LENGTH_SHORT).show();
            return  false;
        }

        return  true;

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

                enterYourAddress.setText(AddresValue);



            } catch (JSONException e) {

                e.printStackTrace();


            }

        }
    }


    private boolean emailValidation(String Email) {
        return (!TextUtils.isEmpty(Email) && Patterns.EMAIL_ADDRESS.matcher(Email).matches());
    }

    private void imageSelect() {

        dialogbox = new Dialog(ActivityAddprofileScreen.this);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialog_photo);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.show();
        dialogbox.setCancelable(false);
        txt_gallery = (TextView) dialogbox.findViewById(R.id.txt_gallery);
        txt_take_photo = (TextView) dialogbox.findViewById(R.id.txt_take_photo);
        select_p = (TextView) dialogbox.findViewById(R.id.select_p);
        btn_cancel_dialog = (Button) dialogbox.findViewById(R.id.btn_cancel_dialog);

        btn_cancel_dialog.setOnClickListener(view -> dialogbox.dismiss());

        txt_gallery.setOnClickListener(view -> {
            dialogbox.dismiss();
            galleryIntent();
        });

        txt_take_photo.setOnClickListener(view -> {
            dialogbox.dismiss();
            cameraIntent();
        });
        languagechangecamara();

    }

    private void addState() {
        dialogbox= new Dialog(ActivityAddprofileScreen.this);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_addstate);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.setCancelable(false);
        ss =dialogbox.findViewById(R.id.ss);
        closeDialoglocation=(AppCompatButton) dialogbox.findViewById(R.id.state_dialogbox_close);
        state_dialogbox_done = (AppCompatButton) dialogbox.findViewById(R.id.state_dialogbox_done);
        recycler_state = (RecyclerView) dialogbox.findViewById(R.id.recycler_state);

        state_dialogbox_done.setOnClickListener(view -> {

             dialogbox.dismiss();

             try{
                 itemClickListener_state = (id, state_name) -> {

                     dialogbox.dismiss();
                     state_id = id;
                     State_name = state_name;
                     a_p_s_tv3.setText(state_name);
                 };

             }catch (Exception e){
                e.printStackTrace();
             }

            getlatandlag(State_name);

        });
        changeLanguageState();

        closeDialoglocation.setOnClickListener(view -> dialogbox.dismiss());

        progressDialog = ProgressDialog.show(ActivityAddprofileScreen.this, "", "Loading...", true);
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
          itemClickListener_state = (id, state_name) -> {
               state_id = id;
               Log.e("state_id",id +"test");
              a_p_s_tv3.setText(state_name);
              State_name=state_name;

          };
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void addCountry() {
        countryModels = new ArrayList<>();
        dialogbox= new Dialog(ActivityAddprofileScreen.this);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_addcountry);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.setCancelable(false);
        sco =dialogbox.findViewById(R.id.sco);
        recycler_country = (RecyclerView) dialogbox.findViewById(R.id.recycler_country);
        closeDialoglocation=(AppCompatButton) dialogbox.findViewById(R.id.country_dialogbox_close);
        country_dialogbox_done = (AppCompatButton) dialogbox.findViewById(R.id.country_dialogbox_done);

        country_dialogbox_done.setOnClickListener(view -> {

            dialogbox.dismiss();
            getlatandlag(Country_name);

        });

        closeDialoglocation.setOnClickListener(view -> dialogbox.dismiss());
        changeLanguageCountry();

       progressDialog = ProgressDialog.show(ActivityAddprofileScreen.this, "", "Loading...", true);
        Api.getClient().getCountry(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CountryModel>() {
            @Override
            public void onResponse(@NonNull Call<CountryModel> call, @NonNull Response<CountryModel> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        dialogbox.show();
                        countryModels = response.body().getCountryList();
                        adapterCountry = new AdapterCountry(context,itemClickListener_country,countryModels);
                        recycler_country.setHasFixedSize(true);
                        recycler_country.setLayoutManager(new LinearLayoutManager(context));
                        recycler_country.setAdapter(adapterCountry);

                    }else {

                        dialogbox.dismiss();
                        GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<CountryModel> call, @NonNull Throwable t) {

                dialogbox.dismiss();
                progressDialog.dismiss();

            }
        });

        try{
            itemClickListener_country = (countr_id, country_name) -> {

                country_id = countr_id;
                Log.e("country_id",countr_id +"test");
                a_p_s_tv2.setText(country_name);
                Country_name=country_name;
            };
        }catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void addCertificate(){
        dialogbox= new Dialog(ActivityAddprofileScreen.this);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_addcertificate);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogbox.setCancelable(false);
        recycler_state = (RecyclerView) dialogbox.findViewById(R.id.recycler_state);
        c1 =  dialogbox.findViewById(R.id.c1);
        certificate_dialogbox_close = (AppCompatButton) dialogbox.findViewById(R.id.certificate_dialogbox_close);
        certificate_dialogbox_done=(AppCompatButton) dialogbox.findViewById(R.id.certificate_dialogbox_done);

        certificate_dialogbox_close.setOnClickListener(view -> dialogbox.dismiss());

        certificate_dialogbox_done.setOnClickListener(view -> dialogbox.dismiss());

        changeLanguageCerti();

        progressDialog = ProgressDialog.show(ActivityAddprofileScreen.this, "", "Loading...", true);
        Api.getClient().getCertificate(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CertificateList>() {
            @Override
            public void onResponse(@NonNull Call<CertificateList> call, @NonNull Response<CertificateList> response) {
                progressDialog.dismiss();
                Log.e("certificate",new Gson().toJson(response.body()));
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        dialogbox.show();
                        certificateLists = response.body().getCertificateList();
                        adapterCertificationsNew = new AdapterCertificationsNew(context,itemClickListenerCertificates,certificateLists);
                        recycler_state.setHasFixedSize(true);
                        recycler_state.setLayoutManager(new LinearLayoutManager(context));
                        recycler_state.setAdapter(adapterCertificationsNew);

                    }else {

                        dialogbox.dismiss();
                        GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CertificateList> call, @NonNull Throwable t) {

                dialogbox.dismiss();
                progressDialog.dismiss();
                GlobalMethods.Toast(context,t.getMessage());

            }
        });

        try {
            itemClickListenerCertificates = (id, certificate_name) -> {
                certifcate_id = id;
                certification_name=certificate_name;
                Log.e("certificate_id",certifcate_id+"test");
                a_p_s_tv5.setText(certificate_name);
            };
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void addDistrict() {

        dialogbox= new Dialog(ActivityAddprofileScreen.this);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_add_district);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.setCancelable(false);
        recycler_city = (RecyclerView) dialogbox.findViewById(R.id.recycler_city);
        sc =dialogbox.findViewById(R.id.sc);
        distric_dialogbox_done = (AppCompatButton) dialogbox.findViewById(R.id.distric_dialogbox_done);
        closeDialoglocation=(AppCompatButton) dialogbox.findViewById(R.id.distric_dialogbox_close);
        TextView other_option=dialogbox.findViewById(R.id.other_option);
        LinearLayout city_layout=dialogbox.findViewById(R.id.city_layout);
        EditText othersCity=dialogbox.findViewById(R.id.others_txt_city);

        other_option.setOnClickListener(v -> city_layout.setVisibility(View.VISIBLE));

        closeDialoglocation.setOnClickListener(view -> dialogbox.dismiss());

        distric_dialogbox_done.setOnClickListener(view -> {
            dialogbox.dismiss();


            opnamecity=othersCity.getText().toString();

            if(opnamecity.equals("")){

                getlatandlag(City_name);
                a_p_s_tv4.setText(City_name);

            }else {

                City_name=opnamecity;
                getlatandlag(City_name);
                a_p_s_tv4.setText(City_name);

            }

        });
        changeLanguageCity();
        progressDialog = ProgressDialog.show(ActivityAddprofileScreen.this, "", "Loading...", true);
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

                dialogbox.dismiss();
                progressDialog.dismiss();

            }
        });

        try {
            itemClickListener_city = (id, city_name) -> {

                city_id = id;
                City_name=city_name;

            };
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

    private void cameraIntent()
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityIfNeeded(cameraIntent, 0);

    }

    private void galleryIntent()
    {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityIfNeeded(pickPhoto , 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){

                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    Uri tempUri = getImageUri(context, photo);

                    if(tempUri == null){

                        Toast.makeText(context, GlobalMethods.getString(context,R.string.Upload_Skill_Image), Toast.LENGTH_SHORT).show();

                    }
                    else{

                        profile_image_view.setImageBitmap(photo);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        finalFile = new File(getRealPathFromURI(tempUri));
                        Log.e("path case 0 :",finalFile+" siva");
                        file_name = finalFile.getName();
                        path = finalFile+"";

                        uploadFile();

                    }

                }

                break;

            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();

                    finalFile = new File(getRealPathFromURI(selectedImage));
                    file_name = finalFile.getName();
                    final  int Tumbsise = 64;

                    Bitmap fullSizeBitMap = BitmapFactory.decodeFile(finalFile+"");
                    Bitmap tubImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file_name),Tumbsise,Tumbsise);

                    int imageRotation = getImageRotation(finalFile);
                    if (imageRotation != 0)
                        fullSizeBitMap = getBitmapRotatedByDegree(fullSizeBitMap, imageRotation);

                    Uri tempUri =null;
                    if(fullSizeBitMap != null){

                        tempUri = getImageUri(context, fullSizeBitMap);

                    }
                    else{
                        Toast.makeText(context, GlobalMethods.getString(context,R.string.Profile_Image_Required), Toast.LENGTH_SHORT).show();
                    }

                    if(tempUri == null){

                        Toast.makeText(context, GlobalMethods.getString(context,R.string.Profile_Image_Required), Toast.LENGTH_SHORT).show();

                    }
                    else {


                        finalFile = new File(getRealPathFromURI(tempUri));
                        Log.e("path case 0 :",finalFile+" siva");
                        file_name = finalFile.getName();
                        path = finalFile+"";
                        profile_image_view.setImageBitmap(fullSizeBitMap);
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
                    a_p_s_tv2.setText(GlobalMethods.getString(context,R.string.country_optional));
                    a_p_s_tv3.setText(GlobalMethods.getString(context,R.string.state_optional));
                    a_p_s_tv4.setText(GlobalMethods.getString(context,R.string.city_optional));
                    country_id = "";
                    state_id = "";
                    city_id = "";
                    Country_name = "";
                    State_name = "";
                    City_name = "";
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

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), "profile_image");
        progressDialog = ProgressDialog.show(ActivityAddprofileScreen.this, "", "Loading...", true);
        Api.getClient().profileImageUpload(fileToUpload,filename).enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(@NonNull Call<ImageUpload> call, @NonNull Response<ImageUpload> response) {
                progressDialog.dismiss();
                Log.e("success response", new Gson().toJson(response.body()));

                assert response.body() != null;
                if(response.body().getStatus().equals("1")){
                    file_name_profile= response.body().getFileName();
                }else {
                    GlobalMethods.Toast(context,response.body().getMessage()+"");
                }

            }
            @Override
            public void onFailure(@NonNull Call<ImageUpload> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e("error response",t.getMessage());
                GlobalMethods.Toast(context,t.getMessage()+"");
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
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ EXTERNAL STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE EXTERNAL STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("ACCESS_FINE_LOCATION");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("ACCESS_COARSE_LOCATION");

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
        } else {
            // Pre-Marshmallow
        }
        return true;
    }

    /**
     * Permissions results
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and others

              /*  perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&*/

                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    Toast.makeText(context, "Permissin is denied", Toast.LENGTH_SHORT)
                            .show();

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {

        dialog = new Dialog(ActivityAddprofileScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_otp_back_screen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        title=dialog.findViewById(R.id.title);
        cancel =  dialog.findViewById(R.id.cancel);
        ok =  dialog.findViewById(R.id.ok);
        cancel.setOnClickListener(v -> dialog.dismiss());
        ok.setOnClickListener(v -> {
            dialog.dismiss();

            startActivity(new Intent(context, ActivityPhonenumberScreen.class));
            finish();

        });
        changeLanguageotp();

    }



    private void languageChange() {

        if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==1)
        {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tpassword.setHint(resources.getText(R.string.password));
            tdescription.setHint(resources.getText(R.string.description));
            a_p_s_tv1.setText(resources.getText(R.string.addProfile));
            a_p_s_tv2.setText(resources.getText(R.string.country_optional));
            a_p_s_tv3.setText(resources.getText(R.string.state_optional));
            a_p_s_tv4.setText(resources.getText(R.string.city_optional));
            a_p_s_tv5.setText(resources.getText(R.string.certification));
            a_p_s_tv6.setText(resources.getText(R.string.next));
            enterYourAddress.setText(resources.getText(R.string.address));


        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==2)
        {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tpassword.setHint(resources.getText(R.string.password));
            tdescription.setHint(resources.getText(R.string.description));
            a_p_s_tv1.setText(resources.getText(R.string.addProfile));
            a_p_s_tv2.setText(resources.getText(R.string.country_optional));
            a_p_s_tv3.setText(resources.getText(R.string.state_optional));
            a_p_s_tv4.setText(resources.getText(R.string.city_optional));
            a_p_s_tv5.setText(resources.getText(R.string.certification));
            a_p_s_tv6.setText(resources.getText(R.string.next));
            enterYourAddress.setText(resources.getText(R.string.address));

        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==3)
        {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tpassword.setHint(resources.getText(R.string.password));
            tdescription.setHint(resources.getText(R.string.description));
            a_p_s_tv1.setText(resources.getText(R.string.addProfile));
            a_p_s_tv2.setText(resources.getText(R.string.country_optional));
            a_p_s_tv3.setText(resources.getText(R.string.state_optional));
            a_p_s_tv4.setText(resources.getText(R.string.city_optional));
            a_p_s_tv5.setText(resources.getText(R.string.certification));
            a_p_s_tv6.setText(resources.getText(R.string.next));
            enterYourAddress.setText(resources.getText(R.string.address));


        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==4)
        {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tpassword.setHint(resources.getText(R.string.password));
            tdescription.setHint(resources.getText(R.string.description));
            a_p_s_tv1.setText(resources.getText(R.string.addProfile));
            a_p_s_tv2.setText(resources.getText(R.string.country_optional));
            a_p_s_tv3.setText(resources.getText(R.string.state_optional));
            a_p_s_tv4.setText(resources.getText(R.string.city_optional));
            a_p_s_tv5.setText(resources.getText(R.string.certification));
            a_p_s_tv6.setText(resources.getText(R.string.next));
            enterYourAddress.setText(resources.getText(R.string.address));


        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==5)
        {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tpassword.setHint(resources.getText(R.string.password));
            tdescription.setHint(resources.getText(R.string.description));
            a_p_s_tv1.setText(resources.getText(R.string.addProfile));
            a_p_s_tv2.setText(resources.getText(R.string.country_optional));
            a_p_s_tv3.setText(resources.getText(R.string.state_optional));
            a_p_s_tv4.setText(resources.getText(R.string.city_optional));
            a_p_s_tv5.setText(resources.getText(R.string.certification));
            a_p_s_tv6.setText(resources.getText(R.string.next));
            enterYourAddress.setText(resources.getText(R.string.address));


        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==6)
        {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tpassword.setHint(resources.getText(R.string.password));
            tdescription.setHint(resources.getText(R.string.description));
            a_p_s_tv1.setText(resources.getText(R.string.addProfile));
            a_p_s_tv2.setText(resources.getText(R.string.country_optional));
            a_p_s_tv3.setText(resources.getText(R.string.state_optional));
            a_p_s_tv4.setText(resources.getText(R.string.city_optional));
            a_p_s_tv5.setText(resources.getText(R.string.certification));
            a_p_s_tv6.setText(resources.getText(R.string.next));
            enterYourAddress.setText(resources.getText(R.string.address));


        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==7)
        {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tpassword.setHint(resources.getText(R.string.password));
            tdescription.setHint(resources.getText(R.string.description));
            a_p_s_tv1.setText(resources.getText(R.string.addProfile));
            a_p_s_tv2.setText(resources.getText(R.string.country_optional));
            a_p_s_tv3.setText(resources.getText(R.string.state_optional));
            a_p_s_tv4.setText(resources.getText(R.string.city_optional));
            a_p_s_tv5.setText(resources.getText(R.string.certification));
            a_p_s_tv6.setText(resources.getText(R.string.next));
            enterYourAddress.setText(resources.getText(R.string.address));


        }
    }
    private void changeLanguageotp() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();


            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();


            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();


            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();


            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();


            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            title.setText(resources.getText(R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(resources.getText(R.string.cancel));
            ok.setText(resources.getText(R.string.ok));

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

    private void changeLanguageCerti() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            c1.setText(resources.getText(R.string.select_certifications));
            certificate_dialogbox_done.setText(resources.getText(R.string.done));
            certificate_dialogbox_close.setText(resources.getText(R.string.close));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            c1.setText(resources.getText(R.string.select_certifications));
            certificate_dialogbox_done.setText(resources.getText(R.string.done));
            certificate_dialogbox_close.setText(resources.getText(R.string.close));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            c1.setText(resources.getText(R.string.select_certifications));
            certificate_dialogbox_done.setText(resources.getText(R.string.done));
            certificate_dialogbox_close.setText(resources.getText(R.string.close));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            c1.setText(resources.getText(R.string.select_certifications));
            certificate_dialogbox_done.setText(resources.getText(R.string.done));
            certificate_dialogbox_close.setText(resources.getText(R.string.close));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            c1.setText(resources.getText(R.string.select_certifications));
            certificate_dialogbox_done.setText(resources.getText(R.string.done));
            certificate_dialogbox_close.setText(resources.getText(R.string.close));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            c1.setText(resources.getText(R.string.select_certifications));
            certificate_dialogbox_done.setText(resources.getText(R.string.done));
            certificate_dialogbox_close.setText(resources.getText(R.string.close));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            c1.setText(resources.getText(R.string.select_certifications));
            certificate_dialogbox_done.setText(resources.getText(R.string.done));
            certificate_dialogbox_close.setText(resources.getText(R.string.close));


        }
    }
}