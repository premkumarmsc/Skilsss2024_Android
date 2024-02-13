package com.bannet.skils.profile.CropFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bannet.skils.Adapter.AdapterCertificationsNew;
import com.bannet.skils.Adapter.AdapterCity;
import com.bannet.skils.Adapter.AdapterCountry;
import com.bannet.skils.Adapter.AdapterState;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.explore.fragment.FragmentExplore;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.crop.fragment.PictureSelectorFragment;
import com.bannet.skils.login.actvity.ActivityLoginScreen;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.profile.responce.CertificateList;
import com.bannet.skils.profile.responce.CityModel;
import com.bannet.skils.profile.responce.CountryModel;
import com.bannet.skils.profile.responce.DeleteProfile;
import com.bannet.skils.profile.responce.EditProfileModel;
import com.bannet.skils.profile.responce.ImageUpload;
import com.bannet.skils.profile.responce.StateModel;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bumptech.glide.Glide;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCropEditProfile extends PictureSelectorFragment {


    ImageView mPictureIv;
    Context context;
    LinearLayout updatebtn;
    Dialog delete_dialog;
    TextView txt_cancel,Account_delete_recreate;
    RelativeLayout relative_certificate;
    ProgressDialog progressDialog;
    RelativeLayout addDistrict,addCountry,addState;
    TextView userProfileDelete,dc1,dc2;

    Dialog dialogbox;
    TextView txt_gallery,txt_take_photo,select_p;
    Button btn_cancel_dialog;
    AppCompatButton closeDialoglocation,state_dialogbox_done,country_dialogbox_done,distric_dialogbox_done;

    EditText tfirstName,tlastName,tcompanyName,temailId,tdescription;
    TextView enterYourAddress;
    String fname,lname,cname,emailid,description;
    String phone_no;
    String USER_ID;
    String  SKILS_ID,SKILS_FROM,SKILS_TO;

    AdapterCountry adapterCountry;
    AdapterCountry.ItemClickListener itemClickListener_country;
    List<CountryModel.Country> countryModels;
    AdapterCity adapterCity;
    AdapterCity.ItemClickListener itemClickListener_city;
    List<CityModel.City> cityModels;
    AdapterState adapterState;
    ImageView livelocation;
    AdapterState.ItemClickListener itemClickListener_state;
    List<StateModel.States> stateModels;
    AdapterCertificationsNew adapterCertificationsNew;
    AdapterCertificationsNew.ItemClickListener itemClickListenerCertificates;
    List<CertificateList.Certificate> certificateLists;

    RecyclerView recycler_country;
    RecyclerView recycler_city;
    RecyclerView recycler_state;
    String name_state="",name_city="",name_country="",name_certification;
    TextView tcity,tcountry,tstate,or;
    String state_id="",city_id="",country_id="",certifcate_id="",opnamecity="";
    AppCompatButton certificate_dialogbox_done,certificate_dialogbox_close;

    Resources resources;
    TextView tcertificate,e_p_s_tv7;
    String longitude,lattitude;

    String file_name = "",path;
    File finalFile;
    public TextView h1,h2,h3,h4,h7,h8,h9;
    String file_name_profile;
    String LANGUAGE_ID,DEVICE_TOKEN,DEVICE_MODEL,DEVICE_VERSION,DEVICE_TYPE,AddresValue;
    public View view;
    TextView sc,ss,sco,c1;
    private int imgHight , imgWidth;
    public GpsMyLocationProvider  provider;

    public static FragmentCropEditProfile newInstance() {
        return new FragmentCropEditProfile();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_crop_edit_profile, container, false);

        context = getActivity();
        init(view);
        return view;
    }

    private void init(View view){

        tcity=view.findViewById(R.id.e_p_s_tv2);
        tstate=view.findViewById(R.id.e_p_s_tv3);
        tcountry=view.findViewById(R.id.e_p_s_tv4);
        tcertificate=view.findViewById(R.id.e_p_s_tv5);
        e_p_s_tv7=view.findViewById(R.id.e_p_s_tv7);
        or = view.findViewById(R.id.or);

        or.setText(GlobalMethods.getString(context,R.string.or));

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");

        tfirstName=view.findViewById(R.id.edit_your_first_name);
        tlastName=view.findViewById(R.id.edit_your_last_name);
        tcompanyName=view.findViewById(R.id.edit_your_company_name);
        temailId=view.findViewById(R.id.edit_your_emailid);
        tdescription=view.findViewById(R.id.edit_your_description);
        livelocation = view.findViewById(R.id.livelocation);

        addDistrict = view.findViewById(R.id.profilepage_add_district);
        addCountry = view.findViewById(R.id.profilepage_add_country);
        addState = view.findViewById(R.id.profilepage_add_state);
        enterYourAddress = view.findViewById(R.id.edit_your_address);

        updatebtn=view.findViewById(R.id.update_btn);
        relative_certificate = view.findViewById(R.id.relative_certificate);

        userCheck(USER_ID);
        getprofile(USER_ID);

        h1 = view.findViewById(R.id.h1);
        h2 = view.findViewById(R.id.h2);
        h3 = view.findViewById(R.id.h3);
        h4 = view.findViewById(R.id.h4);
        h7 = view.findViewById(R.id.h7);
        h8 = view.findViewById(R.id.h8);
        h9 = view.findViewById(R.id.h9);


        h1.setText(GlobalMethods.getString(context,R.string.edi_firstname));
        h2.setText(GlobalMethods.getString(context,R.string.edi_lastname));
        h3.setText(GlobalMethods.getString(context,R.string.edi_company));
        h4.setText(GlobalMethods.getString(context,R.string.edi_email));
        h7.setText(GlobalMethods.getString(context,R.string.address));
        h8.setText(GlobalMethods.getString(context,R.string.certification));
        h9.setText(GlobalMethods.getString(context,R.string.description));

        LANGUAGE_ID=PrefConnect.readString(context,PrefConnect.LANGUAGE_ID,"");
        DEVICE_MODEL=PrefConnect.readString(context,PrefConnect.DEVICE_MODEL,"");
        DEVICE_TOKEN=PrefConnect.readString(context,PrefConnect.DEVICE_TOKEN,"");
        DEVICE_TYPE=PrefConnect.readString(context,PrefConnect.DEVICE_TYPE,"");
        DEVICE_VERSION=PrefConnect.readString(context,PrefConnect.DEVICE_VERSION,"");
        longitude=PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude=PrefConnect.readString(context,PrefConnect.LATITUDE,"");
        Log.e("longitude",longitude);
        Log.e("lattitude",lattitude);


        if(lattitude.isEmpty()){

            String location = PrefConnect.readString(context,PrefConnect.ADDRESS,"");

            if(location.isEmpty()){

                enterYourAddress.setText(location);
            }
            else{

                AddresValue = location;
                enterYourAddress.setText(resources.getText(R.string.address));
            }

        }else {

            GetAddressSOURCE getAddressSOURCE = new GetAddressSOURCE();
            getAddressSOURCE.execute();

        }

        relative_certificate.setOnClickListener(view8 -> addCertificate());

        livelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enterYourAddress.setText(AddresValue);
                tcountry.setText(GlobalMethods.getString(context,R.string.country_optional));
                tstate.setText(GlobalMethods.getString(context,R.string.state_optional));
                tcity.setText(GlobalMethods.getString(context,R.string.city_optional));
                country_id = "";
                state_id = "";
                city_id = "";
                name_country = "";
                name_state = "";
                name_city = "";


            }
        });

        //getCurrentLocation1();

        updatebtn.setOnClickListener(view7 -> {

            if(validation()){
                geteditprofile();
            }

        });

        Places.initialize(requireActivity(), EndPoint.apiKey);

        enterYourAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(context);

                startActivityForResult(intent,100);
            }
        });
        languageChange();

        addDistrict.setOnClickListener(view3 -> {

            enterYourAddress.setText("");
            if(!name_state.equals("")){

                addDistrict();

            }else{

                GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_state));

            }

        });

        addCountry.setOnClickListener(view1 -> {
            enterYourAddress.setText("");
            addCountry();
        });

        addState.setOnClickListener(view2 -> {

            enterYourAddress.setText("");
            if(!name_country.equals("")){

                addState();

            }else{

                GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_country));

            }

        });


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPictureIv = view.findViewById(R.id.main_frag_picture_iv);
        initEvents();
    }

    public  void getCurrentLocation1() {

        provider = new GpsMyLocationProvider(context);
        provider.setLocationUpdateMinDistance(0);
        provider.setLocationUpdateMinTime(0);
        provider.startLocationProvider((location, source) -> {

            provider.stopLocationProvider();
            Log.e("Latitude : ", location.getLatitude() + "\nLongitude : " + location.getLongitude() + "\nAccuracy : " + location.getAccuracy());

            lattitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";

            PrefConnect.writeString(context,PrefConnect.LATITUDE,lattitude);
            PrefConnect.writeString(context,PrefConnect.LOGITUDE,longitude);

            GetAddressSOURCE getAddressSOURCE = new GetAddressSOURCE();
            getAddressSOURCE.execute();
        });

    }

    public void initEvents() {
        // 设置图片点击监听
        mPictureIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

        // 设置裁剪图片结果监听
        setOnPictureSelectedListener(new OnPictureSelectedListener() {
            @Override
            public void onPictureSelected(Uri fileUri, Bitmap bitmap) {
                mPictureIv.setImageBitmap(bitmap);

                String filePath = fileUri.getEncodedPath();
                String imagePath = Uri.decode(filePath);
                uploadFile(imagePath);
            }
        });
    }

    public void userCheck(String id){

        if(GlobalMethods.isNetworkAvailable(getActivity())){
            Api.getClient().CheckUser(id).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    Log.e("status",new Gson().toJson(response.body()));

                    if (response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){



                        }
                        else {

                            GlobalMethods.Toast(context,"Your Account has been deleted");
                            PrefConnect.writeString(context, PrefConnect.USER_ID, "");
                            PrefConnect.writeString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"");
                            Intent in = new Intent(context, ActivityPhonenumberScreen.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            requireActivity().finish();

                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {


                }
            });
        }else {
            GlobalMethods.Toast(context, "No Internet Connection");
        }
    }

    private void getprofile(String user_id) {

        if(GlobalMethods.isNetworkAvailable(getActivity())){
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            Api.getClient().getprofile(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<getprofileModel>() {
                @Override
                public void onResponse(@NonNull Call<getprofileModel> call, @NonNull Response<getprofileModel> response) {
                    Log.e("getprofile", new Gson().toJson(response.body()));
                    progressDialog.dismiss();

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            Log.e("profile_image",response.body().getDetails().getImageUrl()+response.body().getDetails().getImageName());
                            Glide.with(context).load(response.body().getDetails().getImageUrl()+response.body().getDetails().getImageName()).error(R.drawable.profile_image).into(mPictureIv);
                            tfirstName.setText(response.body().getDetails().getFirstName());
                            tlastName.setText(response.body().getDetails().getLastName());
                            tcompanyName.setText(response.body().getDetails().getCompanyName());
                            temailId.setText(response.body().getDetails().getEmail());
                            tdescription.setText(response.body().getDetails().getAbout());


                            if(response.body().getDetails().getCityName().equals("")){

                                tcity.setText(GlobalMethods.getString(context,R.string.city_optional));
                            }
                            else{

                                tcity.setText(response.body().getDetails().getCityName());
                            }

                            if(response.body().getDetails().getStateName().equals("")){

                                tstate.setText(GlobalMethods.getString(context,R.string.state_optional));
                            }
                            else{

                                tstate.setText(response.body().getDetails().getStateName());
                            }

                            if(response.body().getDetails().getCountryName().equals("")){

                                tcountry.setText(GlobalMethods.getString(context,R.string.country_optional));
                            }
                            else{

                                tcountry.setText(response.body().getDetails().getCountryName());
                            }

                            tcertificate.setText(response.body().getDetails().getCertification());
                            SKILS_ID=response.body().getDetails().getSkilId();
                            SKILS_FROM=response.body().getDetails().getAvailableFrom();
                            SKILS_TO=response.body().getDetails().getAvailableTo();
                            file_name_profile=response.body().getDetails().getImageName();
                            country_id=response.body().getDetails().getCountry();
                            city_id=response.body().getDetails().getCity();
                            state_id=response.body().getDetails().getState();
                            name_country=response.body().getDetails().getCountryName();
                            name_city=response.body().getDetails().getCityName();
                            name_state=response.body().getDetails().getStateName();
                            name_certification=response.body().getDetails().getCertification();
                            enterYourAddress.setText(response.body().getDetails().getAddress());

                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<getprofileModel> call, @NonNull Throwable t) {
                    //  progressDialog.dismiss();

                }
            });
        }
        else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }

    private void imageSelect() {

        dialogbox = new Dialog(getActivity());
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialog_photo);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.show();
        dialogbox.setCancelable(false);

        select_p = (TextView) dialogbox.findViewById(R.id.select_p);
        txt_gallery = (TextView) dialogbox.findViewById(R.id.txt_gallery);
        txt_take_photo = (TextView) dialogbox.findViewById(R.id.txt_take_photo);
        btn_cancel_dialog = (Button) dialogbox.findViewById(R.id.btn_cancel_dialog);

        btn_cancel_dialog.setOnClickListener(view -> dialogbox.dismiss());

        txt_gallery.setOnClickListener(view -> {

            dialogbox.dismiss();


        });

        txt_take_photo.setOnClickListener(view -> {

            dialogbox.dismiss();


        });

        languagechangecamara();

    }

    public  void delectProfile(){
        delete_dialog= new Dialog(getActivity());
        delete_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delete_dialog.setContentView(R.layout.dialog_profile_delete);
        delete_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        delete_dialog.show();
        delete_dialog.setCancelable(false);
        dc1 =  delete_dialog.findViewById(R.id.dc1);
        dc2 =  delete_dialog.findViewById(R.id.dc2);
        txt_cancel =  delete_dialog.findViewById(R.id.txt_cancel);
        userProfileDelete= delete_dialog.findViewById(R.id.delete_account);
        Account_delete_recreate=delete_dialog.findViewById(R.id.Account_delete_recreate) ;

        txt_cancel.setOnClickListener(view -> delete_dialog.dismiss());

        Account_delete_recreate.setSelected(true);

        userProfileDelete.setOnClickListener(view -> deleteUserProfile(USER_ID));

        Account_delete_recreate.setOnClickListener(view -> deleteUserProfile(USER_ID));

        languagechangedelecte();

    }

    private void deleteUserProfile(String user_id) {
        Log.e("user_id",user_id);

        if (GlobalMethods.isNetworkAvailable(context)) {
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            Api.getClient().getDeleteprofile(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<DeleteProfile>() {
                @Override
                public void onResponse(@NonNull Call<DeleteProfile> call, @NonNull Response<DeleteProfile> response) {
                    Log.e("success response", new Gson().toJson(response.body()));
                    progressDialog.dismiss();

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            delete_dialog.dismiss();
                            PrefConnect.writeString(context, PrefConnect.USER_ID, "");
                            PrefConnect.writeString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"");
                            Intent in = new Intent(context, ActivityLoginScreen.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            requireActivity().finish();

                        }else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<DeleteProfile> call, @NonNull Throwable t) {

                    progressDialog.dismiss();

                }
            });

        }
        else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }

    private void addState() {
        dialogbox= new Dialog(getActivity());
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_addstate);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogbox.setCancelable(false);
        ss= dialogbox.findViewById(R.id.ss);
        closeDialoglocation=(AppCompatButton) dialogbox.findViewById(R.id.state_dialogbox_close);
        state_dialogbox_done = (AppCompatButton) dialogbox.findViewById(R.id.state_dialogbox_done);
        recycler_state = (RecyclerView) dialogbox.findViewById(R.id.recycler_state);

        state_dialogbox_done.setOnClickListener(view -> {
            dialogbox.dismiss();
            tstate.setText(name_state);

            getlatandlag(name_state);

        });

        changeLanguageState();
        closeDialoglocation.setOnClickListener(view -> dialogbox.dismiss());

        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
        Api.getClient().getState(country_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<StateModel>() {
            @Override
            public void onResponse(@NonNull Call<StateModel> call, @NonNull Response<StateModel> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        stateModels = response.body().getStatesList();
                        adapterState = new AdapterState(context,itemClickListener_state,stateModels);
                        recycler_state.setHasFixedSize(true);
                        dialogbox.show();
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
                state_name = state_name;

                name_state=state_name;

            };
        }catch (Exception e){

            e.printStackTrace();

        }
    }

    private void addCountry() {

        countryModels = new ArrayList<>();
        dialogbox= new Dialog(getActivity());
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_addcountry);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogbox.setCancelable(false);
        sco= dialogbox.findViewById(R.id.sco);
        recycler_country = (RecyclerView) dialogbox.findViewById(R.id.recycler_country);
        closeDialoglocation=(AppCompatButton) dialogbox.findViewById(R.id.country_dialogbox_close);
        country_dialogbox_done = (AppCompatButton) dialogbox.findViewById(R.id.country_dialogbox_done);


        country_dialogbox_done.setOnClickListener(view -> {

            tcountry.setText(name_country);
            dialogbox.dismiss();
            getlatandlag(name_country);

        });

        closeDialoglocation.setOnClickListener(view -> {

            dialogbox.dismiss();


        });
        changeLanguageCountry();
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
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
                country_name = country_name;

                name_country=country_name;

            };

        }catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void addDistrict() {
        dialogbox= new Dialog(getActivity());
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_add_district);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogbox.setCancelable(false);
        sc= dialogbox.findViewById(R.id.sc);
        recycler_city = (RecyclerView) dialogbox.findViewById(R.id.recycler_city);
        distric_dialogbox_done = (AppCompatButton) dialogbox.findViewById(R.id.distric_dialogbox_done);
        closeDialoglocation=(AppCompatButton) dialogbox.findViewById(R.id.distric_dialogbox_close);
        TextView other_option=dialogbox.findViewById(R.id.other_option);
        LinearLayout city_layout=dialogbox.findViewById(R.id.city_layout);
        EditText othersCity=dialogbox.findViewById(R.id.others_txt_city);


        other_option.setOnClickListener(v -> city_layout.setVisibility(View.VISIBLE));

        closeDialoglocation.setOnClickListener(view -> {

            dialogbox.dismiss();

        });

        distric_dialogbox_done.setOnClickListener(view -> {

            dialogbox.dismiss();
            opnamecity=othersCity.getText().toString();

            if(opnamecity.equals("")){

                getlatandlag(name_city);
                tcity.setText(name_city);

            }else {

                name_city=opnamecity;
                getlatandlag(name_city);
                tcity.setText(name_city);

            }
        });

        changeLanguageCity();
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
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
                name_city=city_name;

            };
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public  void getlatandlag(String object_Name){

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {

            List addressList = geocoder.getFromLocationName(object_Name, 1);
            if (addressList != null && addressList.size() > 0) {

                Address address = (Address) addressList.get(0);
                lattitude = String.valueOf(address.getLatitude());
                longitude = String.valueOf(address.getLongitude());

            }
        } catch (IOException e) {

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
            Log.e("result", s + "::");

            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                AddresValue=jsonObject1.getString("formatted_address");

//                String[] str = AddresValue.split(", ");
//
//                for (int i = 0; i < str.length; i++) {
//
//                    enterYourAddress.setText(str[1]);
//
//                }
//
//                Log.e("lat",lattitude);
//                Log.e("long",longitude);


            } catch (JSONException e) {

                e.printStackTrace();


            }

        }
    }

    private void addCertificate(){
        dialogbox= new Dialog(getActivity());
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.dialogbox_addcertificate);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogbox.setCancelable(false);

        c1 = dialogbox.findViewById(R.id.c1);
        recycler_state =  dialogbox.findViewById(R.id.recycler_state);
        certificate_dialogbox_close =  dialogbox.findViewById(R.id.certificate_dialogbox_close);
        certificate_dialogbox_done= dialogbox.findViewById(R.id.certificate_dialogbox_done);

        certificate_dialogbox_close.setOnClickListener(view -> {
            dialogbox.dismiss();
            certifcate_id = "";
            tcertificate.setText("");
            name_certification = "";
        });

        certificate_dialogbox_done.setOnClickListener(view -> {
            dialogbox.dismiss();
            tcertificate.setText(name_certification);
        });

        changeLanguageCerti();
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
        Api.getClient().getCertificate(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CertificateList>() {
            @Override
            public void onResponse(@NonNull Call<CertificateList> call, @NonNull Response<CertificateList> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        certificateLists = response.body().getCertificateList();
                        dialogbox.show();
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
                progressDialog.dismiss();
                dialogbox.dismiss();
                GlobalMethods.Toast(context,t.getMessage());
                Log.e("failure res",t.getMessage());
            }
        });

        try {
            itemClickListenerCertificates = (id, certification_name) -> {
                certifcate_id = id;
                name_certification=certification_name;
                Log.e("certificate_id",certifcate_id+"test");


            };
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public Boolean validation(){
        fname=tfirstName.getText().toString().trim();
        lname=tlastName.getText().toString().trim();
        cname=tcompanyName.getText().toString().trim();
        emailid=temailId.getText().toString().trim();
        description=tdescription.getText().toString().trim();

        if(fname.isEmpty()){
            enterFirstName();
            Toast.makeText(context,GlobalMethods.getString(context,R.string.enter_your_first_name), Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(lname.isEmpty()){
            Toast.makeText(context, GlobalMethods.getString(context,R.string.enter_your_last_name), Toast.LENGTH_SHORT).show();
            return  false;
        }

        return  true;

    }

    private void enterFirstName() {
    }


    private void geteditprofile() {

        Log.e("EditGroupReq",
                "\nuser_id: " + USER_ID
                        + "\nfirst_name: " + fname
                        + "\nlast_name: " + lname
                        + "\nimage_name: " + file_name_profile
                        + "\ncompany_name: " + cname
                        + "\nemail: " + emailid
                        + "\ncountry: " + name_country
                        + "\nstate: " + name_state
                        + "\ncity: " + name_city
                        + "\nphone_no: " + phone_no
                        + "\ncertification: " + name_certification
                        + "\nskill_id: " + SKILS_ID
                        + "\navailable_from: " + SKILS_FROM
                        + "\navailable_to: " + SKILS_TO
                        + "\navailable_to: " + lattitude
                        + "\nskills_name: " + longitude
                        + "\ntaddress: " + enterYourAddress.getText().toString());


        if (GlobalMethods.isNetworkAvailable(context)) {
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            Api.getClient().geteditprofile(USER_ID, fname, lname, file_name_profile, cname, emailid,country_id,state_id,city_id, name_country, name_state, name_city, description, "1", longitude,lattitude, name_certification,
                    PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""),DEVICE_TOKEN, DEVICE_TYPE, DEVICE_VERSION, DEVICE_MODEL,enterYourAddress.getText().toString()).enqueue(new Callback<EditProfileModel>() {

                @Override
                public void onResponse(@NonNull Call<EditProfileModel> call, @NonNull Response<EditProfileModel> response) {
                    Log.e("success response", new Gson().toJson(response.body()));
                    progressDialog.dismiss();

                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus().equals("1")) {

                            PrefConnect.writeString(context,PrefConnect.USER_IMAGE_NAME,response.body().getUserDetails().getImageName());
                            GlobalMethods.Toast(context, response.body().getMessage());
                            Intent in = new Intent(context, ActivityBottom.class);
                            startActivity(in);
                            requireActivity().finish();

                        }
                        else {

                            GlobalMethods.Toast(context, response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EditProfileModel> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    GlobalMethods.Toast(context, t.getMessage());
                    Log.e("failure res", t.getMessage());
                }
            });

        } else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == 100){

                if(data != null){

                    Place place = Autocomplete.getPlaceFromIntent(data);

                    enterYourAddress.setText(place.getAddress());
                    getlatandlag(place.getAddress());
                    tcountry.setText(GlobalMethods.getString(context,R.string.country_optional));
                    tstate.setText(GlobalMethods.getString(context,R.string.state_optional));
                    tcity.setText(GlobalMethods.getString(context,R.string.city_optional));
                    country_id = "";
                    state_id = "";
                    city_id = "";
                    name_country = "";
                    name_state = "";
                    name_city = "";
                }
            }
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



    private void uploadFile(String filePath) {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(filePath);
//        Bitmap fullSizeBitMap = BitmapFactory.decodeFile(path);
//
//        Bitmap reduceBitMap =  ImageResizer.reduceBitmapSize(fullSizeBitMap,240000);
//
//        File reducefile = getMapFile(reduceBitMap);


        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file_name", file.getName(), requestBody);

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), "profile_image");
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
        Api.getClient().profileImageUpload(fileToUpload,filename).enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(@NonNull Call<ImageUpload> call, @NonNull Response<ImageUpload> response) {
                Log.e("success response", new Gson().toJson(response.body()));

                progressDialog.dismiss();
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
            }
        });

    }

    private File getMapFile(Bitmap reduceBitMap) {

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "reduted_file");


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        reduceBitMap.compress(Bitmap.CompressFormat.JPEG,0,bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            file.createNewFile();
            fos.flush();
            fos.close();
            return file;

        }
        catch (IOException e) {

            e.printStackTrace();

        }

        return file;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    private void languageChange() {

        if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==1) {

            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tdescription.setHint(resources.getText(R.string.description));
            tcity.setText(resources.getText(R.string.city_optional));
            tstate.setText(resources.getText(R.string.state_optional));
            tcountry.setText(resources.getText(R.string.country_optional));
            tcertificate.setText(resources.getText(R.string.certification));
            e_p_s_tv7.setText(resources.getText(R.string.UPDATE));
            enterYourAddress.setHint(resources.getText(R.string.address));



        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==2) {

            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tdescription.setHint(resources.getText(R.string.description));
            tcity.setText(resources.getText(R.string.city_optional));
            tstate.setText(resources.getText(R.string.state_optional));
            tcountry.setText(resources.getText(R.string.country_optional));
            tcertificate.setText(resources.getText(R.string.certification));
            e_p_s_tv7.setText(resources.getText(R.string.UPDATE));
            enterYourAddress.setHint(resources.getText(R.string.address));
        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==3) {

            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tdescription.setHint(resources.getText(R.string.description));
            tcity.setText(resources.getText(R.string.city_optional));
            tstate.setText(resources.getText(R.string.state_optional));
            tcountry.setText(resources.getText(R.string.country_optional));
            tcertificate.setText(resources.getText(R.string.certification));
            e_p_s_tv7.setText(resources.getText(R.string.UPDATE));
            enterYourAddress.setHint(resources.getText(R.string.address));


        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==4) {

            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tdescription.setHint(resources.getText(R.string.description));
            tcity.setText(resources.getText(R.string.city_optional));
            tstate.setText(resources.getText(R.string.state_optional));
            tcountry.setText(resources.getText(R.string.country_optional));
            tcertificate.setText(resources.getText(R.string.certification));
            e_p_s_tv7.setText(resources.getText(R.string.UPDATE));
            enterYourAddress.setHint(resources.getText(R.string.address));


        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==5) {

            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tdescription.setHint(resources.getText(R.string.description));
            tcity.setText(resources.getText(R.string.city_optional));
            tstate.setText(resources.getText(R.string.state_optional));
            tcountry.setText(resources.getText(R.string.country_optional));
            tcertificate.setText(resources.getText(R.string.certification));
            e_p_s_tv7.setText(resources.getText(R.string.UPDATE));
            enterYourAddress.setHint(resources.getText(R.string.address));


        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==6) {

            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tdescription.setHint(resources.getText(R.string.description));
            tcity.setText(resources.getText(R.string.city_optional));
            tstate.setText(resources.getText(R.string.state_optional));
            tcountry.setText(resources.getText(R.string.country_optional));
            tcertificate.setText(resources.getText(R.string.certification));
            e_p_s_tv7.setText(resources.getText(R.string.UPDATE));
            enterYourAddress.setHint(resources.getText(R.string.address));


        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==7) {

            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();
            tfirstName.setHint(resources.getText(R.string.enterFirstName));
            tlastName.setHint(resources.getText(R.string.enterLastName));
            tcompanyName.setHint(resources.getText(R.string.companyName));
            temailId.setHint(resources.getText(R.string.emailID));
            tdescription.setHint(resources.getText(R.string.description));
            tcity.setText(resources.getText(R.string.city_optional));
            tstate.setText(resources.getText(R.string.state_optional));
            tcountry.setText(resources.getText(R.string.country_optional));
            tcertificate.setText(resources.getText(R.string.certification));
            e_p_s_tv7.setText(resources.getText(R.string.UPDATE));
            enterYourAddress.setHint(resources.getText(R.string.address));


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
            distric_dialogbox_done.setText(resources.getText(R.string.done));
            closeDialoglocation.setText(resources.getText(R.string.close));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            distric_dialogbox_done.setText(resources.getText(R.string.done));
            closeDialoglocation.setText(resources.getText(R.string.close));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            distric_dialogbox_done.setText(resources.getText(R.string.done));
            closeDialoglocation.setText(resources.getText(R.string.close));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            distric_dialogbox_done.setText(resources.getText(R.string.done));
            closeDialoglocation.setText(resources.getText(R.string.close));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            distric_dialogbox_done.setText(resources.getText(R.string.done));
            closeDialoglocation.setText(resources.getText(R.string.close));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            distric_dialogbox_done.setText(resources.getText(R.string.done));
            closeDialoglocation.setText(resources.getText(R.string.close));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            sc.setText(resources.getText(R.string.select_city));
            distric_dialogbox_done.setText(resources.getText(R.string.done));
            closeDialoglocation.setText(resources.getText(R.string.close));


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
    private void languagechangedelecte() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();


            dc1.setText(resources.getText(R.string.deleteProfile));
            dc2.setText(resources.getText(R.string.deleteyourprofiler));
            txt_cancel.setText(resources.getText(R.string.cancel));
            userProfileDelete.setText(resources.getText(R.string.Delete));
            Account_delete_recreate.setText(resources.getText(R.string.deleteAndRecreate));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            dc1.setText(resources.getText(R.string.deleteProfile));
            dc2.setText(resources.getText(R.string.deleteyourprofiler));
            txt_cancel.setText(resources.getText(R.string.cancel));
            userProfileDelete.setText(resources.getText(R.string.Delete));
            Account_delete_recreate.setText(resources.getText(R.string.deleteAndRecreate));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            dc1.setText(resources.getText(R.string.deleteProfile));
            dc2.setText(resources.getText(R.string.deleteyourprofiler));
            txt_cancel.setText(resources.getText(R.string.cancel));
            userProfileDelete.setText(resources.getText(R.string.Delete));
            Account_delete_recreate.setText(resources.getText(R.string.deleteAndRecreate));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();
            dc1.setText(resources.getText(R.string.deleteProfile));
            dc2.setText(resources.getText(R.string.deleteyourprofiler));
            txt_cancel.setText(resources.getText(R.string.cancel));
            userProfileDelete.setText(resources.getText(R.string.Delete));
            Account_delete_recreate.setText(resources.getText(R.string.deleteAndRecreate));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            dc1.setText(resources.getText(R.string.deleteProfile));
            dc2.setText(resources.getText(R.string.deleteyourprofiler));
            txt_cancel.setText(resources.getText(R.string.cancel));
            userProfileDelete.setText(resources.getText(R.string.Delete));
            Account_delete_recreate.setText(resources.getText(R.string.deleteAndRecreate));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            dc1.setText(resources.getText(R.string.deleteProfile));
            dc2.setText(resources.getText(R.string.deleteyourprofiler));
            txt_cancel.setText(resources.getText(R.string.cancel));
            userProfileDelete.setText(resources.getText(R.string.Delete));
            Account_delete_recreate.setText(resources.getText(R.string.deleteAndRecreate));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            dc1.setText(resources.getText(R.string.deleteProfile));
            dc2.setText(resources.getText(R.string.deleteyourprofiler));
            txt_cancel.setText(resources.getText(R.string.cancel));
            userProfileDelete.setText(resources.getText(R.string.Delete));
            Account_delete_recreate.setText(resources.getText(R.string.deleteAndRecreate));
        }
    }


}