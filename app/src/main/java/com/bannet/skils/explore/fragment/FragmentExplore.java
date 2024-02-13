package com.bannet.skils.explore.fragment;

import static android.content.Context.LOCATION_SERVICE;
import static java.lang.Math.abs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bannet.skils.Activitys.ActivityPlanDetails;
import com.bannet.skils.Activitys.fragment.adapter.PaginationProfAdapter;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.ApiPage;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.service.localdatabase.SqliteDatabase;
import com.bannet.skils.addposting.activity.ActivityAddPosting;
import com.bannet.skils.chat.activity.ActivityChatList;
import com.bannet.skils.explore.adapter.AdapterHomeCategory;
import com.bannet.skils.explore.adapter.AdapterTopPostings;
import com.bannet.skils.explore.response.CategoryLocalREsponse;
import com.bannet.skils.explore.response.CategoryResponce;
import com.bannet.skils.home.responce.profileimagestatusModel;
import com.bannet.skils.login.actvity.ActivityLoginScreen;
import com.bannet.skils.notification.activity.ActivityNotification;
import com.bannet.skils.post.responce.PostinglistModel;
import com.bannet.skils.professinoldetails.activity.ActivityProffDetails;
import com.bannet.skils.professinoldetails.activity.ActivityWithoutimage;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bannet.skils.searchpage.activity.ActivitySearchActivity;
import com.bannet.skils.viewallpostings.activity.ActivityViewAllPostings;
import com.bannet.skils.viewallproffessionals.activity.ActivityViewAllProffessionals;
import com.bannet.skils.viewallproffessionals.adapter.AdapterViewAllProff;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentExplore extends Fragment {

    public Context context;
    public ProgressDialog progressDialog;
    public View view;
    public String select = "0";
    public RecyclerView catogeryList,topPostingList;
    public LinearLayout viewPosting,viewProffesional;
    public int TOTAL_PAGES = 0;
    public int currentPage = PAGE_START;
    public String status;
    public PaginationProfAdapter paginationProfAdapter;
    private static final int PAGE_START = 1;
    public boolean isLoading = false;
    public boolean isLastPage = false;
    public String longitude,lattitude,AddresValue;
    public GpsMyLocationProvider  provider;
    public RecyclerView recyclerProfessions;
    public Intent intent;
    public TextView currentAddress;
    public AdapterHomeCategory adapterHomeCategory;
    public LinearLayout locationChange;
    public String USER_ID,imagename,imageurl;
    public String userRegister;
    public CircleImageView profile_img;
    public ImageView notificationBtn,chatBtn;
    public AdapterTopPostings adapterTopPostings;
    public ImageView floating_plus,float_close;
    public RelativeLayout secondlayouthead;
    public LinearLayout shopBtn,postbtn;
    public TextView e1,e2,e3,e4,e5,e6;
    public EditText search;
    public RelativeLayout searchBtn,notWorking;
    public final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private ArrayList<ProfessionalList.Professional> userModalArrayList;
    private AdapterViewAllProff userRVAdapter;
    private AdapterViewAllProff.itemClikcLissner itemClikcLissner;
    public int newpage = 1;
    public ArrayList<CategoryResponce.Category>categoryList = new ArrayList<>();
    public List<CategoryResponce.Category> categories;
    private ProgressBar loadingPB;
    public NestedScrollView nestedSV;
    public AdapterHomeCategory.ItemClickLissener itemclickLissener;
    private SqliteDatabase mDatabase;
    //List<CategoryResponce> allContacts ;
    public int page_no=1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_explore, container, false);

        context = getActivity();
        init(view);
//        onbackword();

        return view;

    }

    private void init(View view){

        catgoryloadFirstPage(page_no);


        itemclickLissener = (type,id) -> {

            switch (type) {

                case "lastindex":
                    page_no++;
                    Log.e("pageeee",page_no+"");

                    catgoryloadFirstPage1(page_no);
                    break;

            }
        };


        USER_ID = PrefConnect.readString(context,PrefConnect.USER_ID,"");
        userRegister = PrefConnect.readString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"");

        topPostingList = view.findViewById(R.id.top_postlist);
        catogeryList = view.findViewById(R.id.catagery_list);
        viewPosting = view.findViewById(R.id.viewposting);
        viewProffesional = view.findViewById(R.id.viewproff);
        recyclerProfessions = view.findViewById(R.id.proff_list);
        currentAddress = view.findViewById(R.id.current_address);
        locationChange = view.findViewById(R.id.location_change);
        profile_img = view.findViewById(R.id.profile_image);
        notificationBtn = view.findViewById(R.id.notification_btn);
        searchBtn = view.findViewById(R.id.search_btn);
        notWorking = view.findViewById(R.id.not_working);
        chatBtn = view.findViewById(R.id.chat_btn);

        mDatabase = new SqliteDatabase(getActivity());
        //allContacts = mDatabase.listContacts();

        e1 = view.findViewById(R.id.location);
        search = view.findViewById(R.id.edi_search);
        e2 = view.findViewById(R.id.e2);
        e3 = view.findViewById(R.id.e3);
        e4 = view.findViewById(R.id.e4);
        e5 = view.findViewById(R.id.e5);
        e6 = view.findViewById(R.id.e6);
        loadingPB = view.findViewById(R.id.idPBLoading);
        nestedSV = view.findViewById(R.id.idNestedSV);

        e1.setText(GlobalMethods.getString(context,R.string.location));
        e2.setText(GlobalMethods.getString(context,R.string.top_postings_near_you));
        e3.setText(GlobalMethods.getString(context,R.string.view_all));
        e4.setText(GlobalMethods.getString(context,R.string.what_is_on_your_mind));
        e5.setText(GlobalMethods.getString(context,R.string.peoples_around_you));
        e6.setText(GlobalMethods.getString(context,R.string.view_all));

        search.setHint(GlobalMethods.getString(context,R.string.search_with_skills_and_name));

        floating_plus =  view.findViewById(R.id.open_float);
        // relative_right =  view.findViewById(R.id.relative_right);
        float_close =  view.findViewById(R.id.float_close);

        secondlayouthead = view.findViewById(R.id.realtive_head);
        shopBtn = view.findViewById(R.id.contact_new_ads);
        postbtn = view.findViewById(R.id.post_job);

        userModalArrayList = new ArrayList<>();
        getprofile(USER_ID);

        floating_plus.setVisibility(View.GONE);



        Log.e("USER_ID",USER_ID);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(context, 2);
        paginationProfAdapter = new PaginationProfAdapter(context,"1");
        recyclerProfessions.setHasFixedSize(true);
        recyclerProfessions.setLayoutManager(linearLayoutManager);
        // recyclerProfessions.addOnScrollListener(paginationScrollListener);
        recyclerProfessions.setAdapter(paginationProfAdapter);

        Places.initialize(requireActivity(), EndPoint.apiKey);

        locationChange.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(context);

            startActivityForResult(intent,100);
        });

        notWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        //catgoryloadFirstPage(newpage);

        longitude = PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude = PrefConnect.readString(context,PrefConnect.LATITUDE,"");


        Log.e("lati0",lattitude);
        Log.e("longgg",longitude);


        if(!longitude.isEmpty()){

            Log.e("if_cgchgc", "jkjhjkh");
            getProfessionalList(lattitude,longitude,"");
            topPostingList();
            getCurrentLocation1();



        }
        else {

            Log.e("elsffgf", "jkjhjkh");
            //getProfessionalList(lattitude,longitude,"");
            getCurrentLocation();



        }

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

        viewPosting.setOnClickListener(view12 -> {

            intent = new Intent(context, ActivityViewAllPostings.class);
            intent.putExtra("key"," ");
            startActivity(intent);

        });

        searchBtn.setOnClickListener(view12 -> {

            if(!search.getText().toString().isEmpty()){

                intent = new Intent(context, ActivitySearchActivity.class);
                intent.putExtra("key",search.getText().toString());
                startActivity(intent);
                search.setText("");

            }

        });

        viewProffesional.setOnClickListener(view1 -> {

            intent = new Intent(context, ActivityViewAllProffessionals.class);
            startActivity(intent);

        });

        floating_plus.setOnClickListener(view16 -> {

            select = "1";
            notWorking.setVisibility(View.VISIBLE);
            floating_plus.setVisibility(View.GONE);
            secondlayouthead.setVisibility(View.VISIBLE);

        });

        float_close.setOnClickListener(view15 -> {

            notWorking.setVisibility(View.GONE);
            floating_plus.setVisibility(View.VISIBLE);
            secondlayouthead.setVisibility(View.GONE);
            select = "0";

        });

        shopBtn.setOnClickListener(view14 -> {

            if(userRegister.equals("customer")) {
                userLogin();
            }
            else {

                if(!isLocationEnabled(context))

                    showMessageEnabledGPS();

                else{

                    select = "0";
                    notWorking.setVisibility(View.GONE);
                    secondlayouthead.setVisibility(View.GONE);
                    Intent in = new Intent(context, ActivityPlanDetails.class);
                    startActivity(in);
                }

            }

        });

        postbtn.setOnClickListener(view13 -> {

            if(userRegister.equals("customer")) {

                userLogin();

            }
            else {
                select = "0";
                notWorking.setVisibility(View.GONE);
                secondlayouthead.setVisibility(View.GONE);
                Intent in = new Intent(context, ActivityAddPosting.class);
                in.putExtra("type","add_new_post");
                startActivity(in);
            }

        });

        notificationBtn.setOnClickListener(view3 -> {

            if (userRegister.equals("customer")){

                userLogin();

            }
            else {

                intent = new Intent(context, ActivityNotification.class);
                startActivity(intent);

            }

        });

        chatBtn.setOnClickListener(view17 -> {

            if (userRegister.equals("customer")){

                userLogin();

            }
            else {

                Intent intent = new Intent(context, ActivityChatList.class);
                startActivity(intent);

            }


        });

        //catgoryloadFirstPage(page_no);

//        if (allContacts.size() > 0) {
//
////            Log.e("name",allContacts.get(1).getId());
////            Log.e("name...",allContacts.size()+"");
//
//            GridLayoutManager linearLayoutManager1 =new GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,false);
//            adapterHomeCategory = new AdapterHomeCategory(context,allContacts);
//            catogeryList.setHasFixedSize(true);
//            catogeryList.setLayoutManager(linearLayoutManager1);
//            catogeryList.setAdapter(adapterHomeCategory);
//
//        }
//        else {
//
//            //catgorylList();
//            page_no++;
//            catgoryloadFirstPage1(page_no);
//
//        }

        //catgorylListadd();





        nestedSV.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                newpage++;
                Log.e("pageeee",newpage+"");
                loadingPB.setVisibility(View.VISIBLE);
               // catgorylList(newpage);
            }
        });

    }

    private void userLogin() {

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_not_logon_dialog);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialoAnimasion;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


        TextView title = dialog.findViewById(R.id.title);
        AppCompatButton login = dialog.findViewById(R.id.login_btn);

        title.setText(GlobalMethods.getString(context,R.string.please_login));
        login.setText(GlobalMethods.getString(context,R.string.go_to_login));

        login.setOnClickListener(view -> {

            Intent intent = new Intent(context, ActivityLoginScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });

    }

    private boolean isLocationEnabled(Context context){
//        String locationProviders;
        boolean enabled = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            enabled = (mode != Settings.Secure.LOCATION_MODE_OFF);
        }else{
            LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            enabled =  service.isProviderEnabled(LocationManager.GPS_PROVIDER)||service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return enabled;
    }

    public void showMessageEnabledGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.this_service_requires_the_activation_of_the_gps))
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
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

            Log.e("long0",longitude);
            Log.e("latttttttttttt",lattitude);

            PrefConnect.writeString(context,PrefConnect.LATITUDE,lattitude);
            PrefConnect.writeString(context,PrefConnect.LOGITUDE,longitude);

            getProfessionalList(lattitude,longitude,"");
            topPostingList();
            GetAddressSOURCE getAddressSOURCE = new GetAddressSOURCE();
            getAddressSOURCE.execute();

        });

    }

    public  void getCurrentLocation1( ) {

        provider = new GpsMyLocationProvider(context);
        provider.setLocationUpdateMinDistance(0);
        provider.setLocationUpdateMinTime(0);
        provider.startLocationProvider((location, source) -> {

            provider.stopLocationProvider();
            //Log.e("Latitude : ", location.getLatitude() + "\nLongitude : " + location.getLongitude() + "\nAccuracy : " + location.getAccuracy());

            lattitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";



            PrefConnect.writeString(context,PrefConnect.LATITUDE,lattitude);
            PrefConnect.writeString(context,PrefConnect.LOGITUDE,longitude);

            GetAddressSOURCE getAddressSOURCE = new GetAddressSOURCE();
            getAddressSOURCE.execute();

        });

    }

    public boolean isPermissionGrantedFirst() {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
        }
        else {

            return true;
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
                return true;
            } else {

                //Toast.makeText(Accueil.this, "Permission is revoked", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_READ_EXTERNAL_STORAGE);
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

//            if(data != null){
                Place place = Autocomplete.getPlaceFromIntent(data);
                currentAddress.setText(place.getAddress());

                String[] str = Objects.requireNonNull(place.getAddress()).split(",");

                for (int i = 0; i < str.length; i++) {

                    currentAddress.setText(str[0]);


                }
                getlatandlag(place.getAddress());
            //}

        }
    }

    public  void getlatandlag(String object_Name){

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {

            List addressList = geocoder.getFromLocationName(object_Name, 1);
            if (addressList != null && addressList.size() > 0) {

                Address address = (Address) addressList.get(0);
               // currentPage = 1;
                lattitude = String.valueOf(address.getLatitude());
                longitude = String.valueOf(address.getLongitude());

                getProfessionalList(lattitude,longitude,"");


            }
        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void getProfessionalList(String lati,String logi,String key){

        Log.e("latitude",lati);
        Log.e("logitude",logi);
        Log.e("keyyyyy",key);

        String lang= PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"");

        Log.e("language",lang);




        if(GlobalMethods.isNetworkAvailable(context)){
            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            String currentPaged= String.valueOf(currentPage);
            ApiPage.getClient().professionallist(lati,logi,"",key, PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""), String.valueOf(1)).enqueue(new Callback<ProfessionalList>() {
                @Override
                public void onResponse(@NonNull Call<ProfessionalList> call, @NonNull Response<ProfessionalList> response) {
                    Log.e("p_responec",new Gson().toJson(response.body()));
                    if(response.isSuccessful()){

                        progressDialog.dismiss();
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            userModalArrayList.addAll(response.body().getProfessionalList());

                            // passing array list to our adapter class.
                            userRVAdapter = new AdapterViewAllProff(context,userModalArrayList,"1",itemClikcLissner);

                            // setting layout manager to our recycler view.
                            recyclerProfessions.setLayoutManager(new GridLayoutManager(context,2));

                            // setting adapter to our recycler view.
                            recyclerProfessions.setAdapter(userRVAdapter);

                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ProfessionalList> call, @NonNull Throwable t) {

                    Log.e("errrr",t.getMessage());
                    progressDialog.dismiss();
                }
            });

        }else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }


    private void getprofile(String  user_id) {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().getprofile(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<getprofileModel>() {
                @Override
                public void onResponse(@NonNull Call<getprofileModel> call, @NonNull Response<getprofileModel> response) {

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            imagename=response.body().getDetails().getImageName();
                            imageurl=response.body().getDetails().getImageUrl();
                            profile_image_status(imageurl,imagename);
                            PrefConnect.writeString(context,PrefConnect.SKILSS_ID,response.body().getDetails().getSkilId());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<getprofileModel> call, @NonNull Throwable t) {


                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }

    private void topPostingList() {

        if(GlobalMethods.isNetworkAvailable(context)){


            Api.getClient().Toppostingslist(lattitude,longitude,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""),"").enqueue(new Callback<PostinglistModel>() {
                @Override
                public void onResponse(@NonNull Call<PostinglistModel> call, @NonNull Response<PostinglistModel> response) {

                    Log.e("top_post",new Gson().toJson(response.body()));

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        adapterTopPostings = new AdapterTopPostings(context,response.body().getPostingList());
                        topPostingList.setHasFixedSize(true);
                        topPostingList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
                        topPostingList.setAdapter(adapterTopPostings);


                    }else {


                    }
                }

                @Override
                public void onFailure(@NonNull Call<PostinglistModel> call, @NonNull Throwable t) {

                    Log.e("failure professional res",t.getMessage());
                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }

    private void profile_image_status(String imageurl,String imagename) {
        if(GlobalMethods.isNetworkAvailable(context)){
            Api.getClient().profileimagestatus(USER_ID,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<profileimagestatusModel>() {
                @Override
                public void onResponse(@NonNull Call<profileimagestatusModel> call, @NonNull Response<profileimagestatusModel> response) {

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            Glide.with(context).load(imageurl+imagename).error(R.drawable.profile_image).into(profile_img);


                        }else {
                            profile_img.setBackgroundResource(R.drawable.profile_image);

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<profileimagestatusModel> call, @NonNull Throwable t) {


                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }


    public static int roundUp(long num, long divisor) {

        int sign = (num > 0 ? 1 : -1) * (divisor > 0 ? 1 : -1);
        return (int) (sign * (abs(num) + abs(divisor) - 1) / abs(divisor));

    }

//    private void catgorylList() {
//
//        if(GlobalMethods.isNetworkAvailable(context)){
//
//            Api.getClient().categoryList(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CategoryResponce>() {
//                @Override
//                public void onResponse(@NonNull Call<CategoryResponce> call, @NonNull Response<CategoryResponce> response) {
//
//                    Log.e("mycatery",new Gson().toJson(response.body()));
//
//                    assert response.body() != null;
//                    if(response.body().getStatus().equals("1")){
//
//                        mDatabase.deletaAll();
//
//                        for (int i = 0;i < response.body().getCategoryList().size();i++){
//
//
//                            CategoryLocalREsponse newContact = new CategoryLocalREsponse(response.body().getCategoryList().get(i).getCategoryId(),
//                                    response.body().getCategoryList().get(i).getCategoryName(),response.body().getCategoryList().get(i).getImageUrl() +
//                                    response.body().getCategoryList().get(i).getCategoryImage());
//
//
//                            mDatabase.addContacts(newContact);
//                            //allContacts = mDatabase.listContacts();
//
//                            GridLayoutManager linearLayoutManager =new GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,false);
//                            adapterHomeCategory = new AdapterHomeCategory(context,allContacts);
//                            catogeryList.setHasFixedSize(true);
//                            catogeryList.setLayoutManager(linearLayoutManager);
//                            catogeryList.setAdapter(adapterHomeCategory);
//                            adapterHomeCategory.notifyDataSetChanged();
//
//                        }
//
//                    }else {
//
//                        GlobalMethods.Toast(context,response.body().getMessage());
//
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<CategoryResponce> call, @NonNull Throwable t) {
//
//                    Log.e("failure professional res",t.getMessage());
//
//                }
//            });
//        }
//        else {
//
//            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
//
//        }
//    }
//
//
//    private void catgorylListadd() {
//
//        if(GlobalMethods.isNetworkAvailable(context)){
//
//            Api.getClient().categoryList(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CategoryResponce>() {
//                @Override
//                public void onResponse(@NonNull Call<CategoryResponce> call, @NonNull Response<CategoryResponce> response) {
//
//                    assert response.body() != null;
//                    if(response.body().getStatus().equals("1")){
////                        mDatabase.deletaAll();
////                        for (int i = 0;i < response.body().getCategoryList().size();i++){
////
////
////                            CategoryLocalREsponse newContact = new CategoryLocalREsponse(response.body().getCategoryList().get(i).getCategoryId(),
////                                    response.body().getCategoryList().get(i).getCategoryName(),response.body().getCategoryList().get(i).getImageUrl() +
////                                    response.body().getCategoryList().get(i).getCategoryImage());
////
////                            mDatabase.addContacts(newContact);
////
////
////
////                        }
//
//                                    GridLayoutManager linearLayoutManager1 =new GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,false);
//                                    adapterHomeCategory = new AdapterHomeCategory(context,allContacts);
//                                    catogeryList.setHasFixedSize(true);
//                                    catogeryList.setLayoutManager(linearLayoutManager1);
//                                    catogeryList.setAdapter(adapterHomeCategory);
//
//
//
//
//                    }else {
//
//                        // GlobalMethods.Toast(context,response.body().getMessage());
//
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<CategoryResponce> call, @NonNull Throwable t) {
//
//                    Log.e("failure professional res",t.getMessage());
//
//                }
//            });
//        }
//        else {
//
//            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
//
//        }
//    }

    private void catgoryloadFirstPage(int newpage) {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().categoryListPage(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""), String.valueOf(newpage)).enqueue(new Callback<CategoryResponce>() {
                @Override
                public void onResponse(@NonNull Call<CategoryResponce> call, @NonNull Response<CategoryResponce> response) {

                    Log.e("pagination",new Gson().toJson(response.body()));

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){




                        //categoryList.addAll(response.body().getCategoryList());
                        GridLayoutManager linearLayoutManager =new GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,false);
                        adapterHomeCategory = new AdapterHomeCategory(context,response.body().getCategoryList(),itemclickLissener);
                        catogeryList.setHasFixedSize(true);
                        catogeryList.setLayoutManager(linearLayoutManager);
                        catogeryList.setAdapter(adapterHomeCategory);

                    }else {

                        // GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CategoryResponce> call, @NonNull Throwable t) {

                    Log.e("failure professional res",t.getMessage());

                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    private void catgoryloadFirstPage1(int newpage) {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().categoryListPage(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""), String.valueOf(newpage)).enqueue(new Callback<CategoryResponce>() {
                @Override
                public void onResponse(@NonNull Call<CategoryResponce> call, @NonNull Response<CategoryResponce> response) {

                    Log.e("pagination",new Gson().toJson(response.body()));

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){


                        categoryList.addAll(response.body().getCategoryList());
                        adapterHomeCategory.notifyDataSetChanged();

//                        GridLayoutManager linearLayoutManager =new GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,false);
//                        adapterHomeCategory = new AdapterHomeCategory(context,categories);
//                        catogeryList.setHasFixedSize(true);
//                        catogeryList.setLayoutManager(linearLayoutManager);
//                        catogeryList.setAdapter(adapterHomeCategory);

                    }else {

                        // GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CategoryResponce> call, @NonNull Throwable t) {

                    Log.e("failure professional res",t.getMessage());

                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

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
            Log.e("ressssss",s);
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                AddresValue=jsonObject1.getString("formatted_address");
                Log.e("AddresValue",AddresValue);

                String[] str = AddresValue.split(", ");

                for (int i = 0; i < str.length; i++) {

                      currentAddress.setText(str[1]);

                }

                Log.e("lat",lattitude);
                Log.e("long",longitude);




            } catch (JSONException e) {

                e.printStackTrace();


            }

        }
    }

}