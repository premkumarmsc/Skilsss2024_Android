package com.bannet.skils.addposting.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.bannet.skils.Adapter.AdapterCity;
import com.bannet.skils.Adapter.AdapterCountry;
import com.bannet.skils.Adapter.AdapterPostImage;
import com.bannet.skils.Adapter.AdapterState;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.addnewskilss.activity.ActivityAddskilsFromPostingPage;
import com.bannet.skils.crop.fragment.BannerSelectorFragment;
import com.bannet.skils.post.responce.AddPostModel;
import com.bannet.skils.post.responce.EditPostModel;
import com.bannet.skils.post.responce.PostImageModel;
import com.bannet.skils.postingdetails.responce.postingDetailsModel;
import com.bannet.skils.profile.responce.CityModel;
import com.bannet.skils.profile.responce.CountryModel;
import com.bannet.skils.profile.responce.ImageUpload;
import com.bannet.skils.profile.responce.StateModel;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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
import java.util.Arrays;
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

public class FragmentCropAddPosting extends BannerSelectorFragment {

    public Context context;
    public View view;
    public LinearLayout mPictureIv;
    public RelativeLayout img_select_skill;
    public RelativeLayout addDistrict,addCountry,addState;
    public EditText edit_post_title,edit_post_descr;
    public TextView enterYourAddress;
    public Dialog dialogbox;
    public AppCompatButton closeDialoglocation,state_dialogbox_done,country_dialogbox_done,distric_dialogbox_done;
    public RecyclerView recycler_country;
    public RecyclerView recycler_city;
    public RecyclerView recycler_state;
    public AdapterCountry adapterCountry;
    public AdapterCountry.ItemClickListener itemClickListener_country;
    public List<CountryModel.Country> countryModels;
    public AdapterCity adapterCity;
    public AdapterCity.ItemClickListener itemClickListener_city;
    public List<CityModel.City> cityModels;
    public AdapterState adapterState;
    public AdapterState.ItemClickListener itemClickListener_state;
    public List<StateModel.States> stateModels;
    public String state_id="",city_id="",country_id="";
    public String state_name="",city_name="",country_name="",opnamecity="";
    public ProgressDialog progressDialog;
    public TextView edit_country,edit_state,edit_city;
    public String user_id,skill_id="",skill_name="",post_images="",lattitude,longitude;
    public LinearLayout relative_submit;
    public RecyclerView recycler_images;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public Dialog camera_dialog;
    public TextView txt_gallery, txt_take_photo,select_p;
    public Button btn_cancel_dialog;
    public String file_name = "",path;
    public File finalFile;
    public ImageView liveAddress;
    public ArrayList<String> list = new ArrayList<>();
    public List<PostImageModel> testModels = new ArrayList<>();
    public AdapterPostImage testAdapter;
    public AdapterPostImage.ItemClickListener itemClickListener;
    public ArrayList<String> images_name = new ArrayList<>();
    public TextView relative_slect_skill;
    public String type,post_id="";
    public TextView title,btn,or;
    public String USER_ID,AddresValue;
    public TextView sco,ss,sc,ap1,ap2,ap3,ap4,ap7;
    public Resources resources;
    public RelativeLayout plaseSerach;

    public static FragmentCropAddPosting newInstance() {
        return new FragmentCropAddPosting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_crop_add_posting, container, false);

        context = getActivity();
        init(view);

        return view;
    }

    private void init(View view){

        //callMultiplePermissions();

        if(getArguments() != null){

            type = getArguments().getString("type");
            post_id = getArguments().getString("post_id");

        }

        ap1 = view.findViewById(R.id.ap1);
        ap2 = view.findViewById(R.id.ap2);
        ap3 = view.findViewById(R.id.ap3);
        ap4 = view.findViewById(R.id.ap4);
        ap7 = view.findViewById(R.id.h7);
        or = view.findViewById(R.id.or);

        btn = view.findViewById(R.id.post_page_btn);
        addDistrict = view.findViewById(R.id.profilepage_add_district);
        addCountry = view.findViewById(R.id.profilepage_add_country);
        addState = view.findViewById(R.id.profilepage_add_state);
        edit_post_title = view.findViewById(R.id.edit_post_title);
        edit_post_descr = view.findViewById(R.id.edit_post_descr);
        edit_country = view.findViewById(R.id.edit_country);
        edit_state = view.findViewById(R.id.edit_state);
        edit_city = view.findViewById(R.id.edit_city);
        relative_submit = view.findViewById(R.id.relative_submit);
        img_select_skill =  view.findViewById(R.id.img_select_skill);
        recycler_images =  view.findViewById(R.id.recycler_images);
        relative_slect_skill =  view.findViewById(R.id.relative_slect_skill);
        enterYourAddress = view.findViewById(R.id.edit_address);
        plaseSerach = view.findViewById(R.id.place_serach);
        liveAddress = view.findViewById(R.id.liveAddress);

        user_id = PrefConnect.readString(context,PrefConnect.USER_ID,"");
        longitude=PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude=PrefConnect.readString(context,PrefConnect.LATITUDE,"");

        or.setText(GlobalMethods.getString(context,R.string.or));
        ap7.setText(GlobalMethods.getString(context,R.string.address));
        relative_slect_skill.setText(GlobalMethods.getString(context,R.string.selectRequiredSkills));
        Places.initialize(requireActivity(), EndPoint.apiKey);

        liveAddress.setOnClickListener(v -> {

            enterYourAddress.setText(AddresValue);
            edit_country.setText(GlobalMethods.getString(context,R.string.country_optional));
            edit_state.setText(GlobalMethods.getString(context,R.string.state_optional));
            edit_city.setText(GlobalMethods.getString(context,R.string.city_optional));
            country_name = "";
            state_name = "";
            city_name = "";
            country_id = "";
            state_id = "";
            city_id = "";
        });

        enterYourAddress.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(context);

            startActivityForResult(intent,100);
        });



        changeLanguage();

        if(!type.equals("add_new_post")) {

            getpostDetails(USER_ID,post_id);
            Log.e("post_id",post_id);

        }

        img_select_skill.setOnClickListener(view1 -> {

            Intent i = new Intent(getActivity(), ActivityAddskilsFromPostingPage.class);
            i.putExtra("type","addpost");
            startActivityForResult(i, 50);

        });

        relative_submit.setOnClickListener(view2 -> {

            if(type.equals("add_new_post")){

                if(validation()){
                    callAddPost();
                }

            }
            else{
                updatepost(post_id);
            }
        });


        addDistrict.setOnClickListener(view3 -> {

            enterYourAddress.setText("");
            if(!state_name.equals("")){

                addDistrict();

            }else{

                GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_state));

            }

        });

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

        addCountry.setOnClickListener(view4 -> {
            enterYourAddress.setText("");
            addCountry();
        });

        addState.setOnClickListener(view5 -> {
            enterYourAddress.setText("");
            if(!country_name.equals("")){

                addState();

            }else{

                GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.select_country));

            }
        });

        itemClickListener = new AdapterPostImage.ItemClickListener() {
            @Override
            public void ItemClick(ImageView image) {

                try {

                    image.setImageDrawable(null);
                    GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Image_removed));

                }

                catch (Exception e){

                    e.printStackTrace();
                    GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Some_error_occurred));

                }
            }
        };

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPictureIv = view.findViewById(R.id.main_frag_picture_iv);
        initEvents();
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
                // mPictureIv.setImageBitmap(bitmap);

                String filePath = fileUri.getEncodedPath();
                String imagePath = Uri.decode(filePath);
                uploadFile(imagePath);

            }
        });
    }

    private  boolean validation(){

        if(edit_post_title.getText().toString().isEmpty()){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.title));
            return false;

        }
        return true;
    }


    private void getpostDetails(String user_id, String post_id ) {

        if (GlobalMethods.isNetworkAvailable(context)) {

            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);

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
                                recycler_images.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
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
        if(testModels.size() == 0){

            post_images = "";

        }
        else {

            StringBuffer sb= new StringBuffer(String.valueOf(temp));
            sb.deleteCharAt(sb.length()-1);
            Log.e("temp", String.valueOf(sb));
            post_images = String.valueOf(sb);

        }
        if (GlobalMethods.isNetworkAvailable(getActivity())) {

            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
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
                            requireActivity().finish();
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


    private void addCountry() {
        countryModels = new ArrayList<>();
        dialogbox= new Dialog(getActivity());
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

        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
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
        dialogbox= new Dialog(getActivity());
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

        dialogbox= new Dialog(getActivity());
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

        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
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

                if(type.equals("add_new_post")) {

                    enterYourAddress.setText(AddresValue);

                }

            } catch (JSONException e) {

                e.printStackTrace();


            }

        }
    }


    public void callAddPost(){

        if (GlobalMethods.isNetworkAvailable(getActivity())){
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

            if(testModels.size() == 0){

                post_images = "";

            }
            else {

                StringBuffer sb= new StringBuffer(String.valueOf(temp));
                sb.deleteCharAt(sb.length()-1);
                Log.e("temp", String.valueOf(sb));
                post_images = String.valueOf(sb);

            }

            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
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
                            requireActivity().finish();

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
        camera_dialog = new Dialog(getActivity());
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
        startActivityForResult(cameraIntent, 0);

    }

    private void galleryIntent() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto , 1);

//        Intent pickPhoto = new Intent(Intent.ACTION_PICK);
//        pickPhoto.setType("image/*"); // Filter to only show image files
//        startActivityForResult(pickPhoto, 1);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == 100){

                if(data != null){

                    Log.e("place","pla");

                    Place place = Autocomplete.getPlaceFromIntent(data);

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
            }
            else if(requestCode == 50){

                 skill_name = data.getStringExtra("image");
                 skill_id = data.getStringExtra("id");
                 relative_slect_skill.setText(skill_name);


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


    private void uploadFile(String imagePath) {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(imagePath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file_name", file.getName(), requestBody);

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), "post_images");
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
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
                    recycler_images.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
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
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void callMultiplePermissions() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();

        if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("ACCESS_FINE_LOCATION");
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("ACCESS_COARSE_LOCATION");
        if (!addPermission(permissionsList, android.Manifest.permission.READ_MEDIA_IMAGES))
            permissionsNeeded.add("READ_MEDIA_IMAGES");
        if (!addPermission(permissionsList, android.Manifest.permission.READ_MEDIA_VIDEO))
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

    public  void getlatandlag(String object_Name){


        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

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

                btn.setText(resources.getText(R.string.submit));

            }else {

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

                btn.setText(resources.getText(R.string.submit));

            }else {

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
                btn.setText(resources.getText(R.string.submit));

            }else {
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
                btn.setText(resources.getText(R.string.submit));

            }else {

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

                btn.setText(resources.getText(R.string.submit));

            }else {

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

                btn.setText(resources.getText(R.string.submit));

            }else {

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