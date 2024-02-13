package com.bannet.skils.addskilss.cropview;

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
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bannet.skils.Activitys.ActivityAddNewSkill;
import com.bannet.skils.Adapter.AdapterPostImage;
import com.bannet.skils.Model.EditskillsModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.NewApi;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.addskilss.adapter.AdapterChooseCategery;
import com.bannet.skils.addskilss.adapter.AdapterSkillsPagenassion;
import com.bannet.skils.addskilss.responce.ProfileUpload;
import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.crop.fragment.BannerSelectorFragment;
import com.bannet.skils.explore.response.CategoryResponce;
import com.bannet.skils.home.responce.profileimagestatusModel;
import com.bannet.skils.post.responce.PostImageModel;
import com.bannet.skils.profile.activity.ActivityAddprofileScreen;
import com.bannet.skils.profile.responce.ImageUpload;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bannet.skils.subcategoryhome.responce.SubcategoryResponse;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentCropAddskilsss extends BannerSelectorFragment {

    private LinearLayout mPictureIv;
    private View view;
    public RelativeLayout sumitebtn,selectCategory;
    public Dialog dialogbox;
    public LinearLayout bannerLayout;
    public Context context;
    public LinearLayoutCompat timepickerFromDialogbox,timepickertoDialogbox;
    public CardView add_new_skils;
    public String referCode;
    private AdapterChooseCategery adapterChooseCategery;
    public AdapterChooseCategery.ItemClickListener itemClickListenercategory;
    private TimePicker timePicker;
    public Dialog timepickerdialog;
    public Button timePickerDialogClose;
    public TextView timeviewinFrom,timeviewinto,select_p;
    public String searchKey = "";

    public Resources resources;
    public TextView a_s_s_tv2,a_s_s_tv3,a_s_s_tv4;
    public AdapterSkillsPagenassion adapterSkills;
    public AdapterSkillsPagenassion.ItemClickListener itemClickListener;
    public List<SubcategoryResponse.Skill> skillsList;
    public ProgressDialog progressDialog;
    public RecyclerView skil_recycycler;
    public String format;
    public Calendar calendar;
    public String  from_time,to_time,skils ="",skillss_name="";
    int lang;
    public String first_name,last_name,image_name,company_name,about,email,password,device_type,device_token,
            device_model,device_version,gender,certificate = "",lattitude,longitude,liveAddress;
    public String USER_ID;
    public String country_id,state_id,city_id,country_name,state_name,city_name;
    public String type;
    public Dialog camera_dialog;
    public TextView txt_gallery, txt_take_photo,abimage;
    public Button btn_cancel_dialog;
    public String file_name = "",path;
    public File finalFile;
    public ArrayList<String> list = new ArrayList<>();
    public List<PostImageModel> testModels = new ArrayList<>();
    public ArrayList<String> images_name = new ArrayList<>();
    public String banner_images="",code;
    public AdapterPostImage testAdapter;
    public AdapterPostImage.ItemClickListener banneritemClickListener;
    public RecyclerView recycler_images;
    public TextView tp1,edicatName;
    public TextView suggest;
    public String catName="",catId="";
    public RelativeLayout search;
    public EditText edisearch;

    private ArrayList<SubcategoryResponse.Skill> userModalArrayList;
    public AdapterSkillsPagenassion.ItemClickListener itemClickListenerskills;
    private AdapterSkillsPagenassion userRVAdapter;
    private RecyclerView userRV;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    int page = 1, limit = 2;

    public static FragmentCropAddskilsss newInstance() {
        return new FragmentCropAddskilsss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_crop_addskilsss, container, false);

        context = getActivity();
        init(view);

        return view;
    }

    private void init(View view){

        onbackword();

        sumitebtn=(RelativeLayout) view.findViewById(R.id.skilpage_sumite_btn);
        add_new_skils = view.findViewById(R.id.add_new_skils);
        suggest = view.findViewById(R.id.suggest);
        bannerLayout = view.findViewById(R.id.banner_post);
        a_s_s_tv2=view.findViewById(R.id.avt);
        a_s_s_tv3=view.findViewById(R.id.a_s_s_tv3);
        a_s_s_tv4=view.findViewById(R.id.a_s_s_tv4);
        abimage=view.findViewById(R.id.abimage);
        selectCategory = view.findViewById(R.id.selectcat);
        edicatName = view.findViewById(R.id.edit_catogery);
        edisearch = view.findViewById(R.id.edi_search);

        recycler_images =  view.findViewById(R.id.recycler_images);

        timepickerFromDialogbox=view.findViewById(R.id.timepickerfrom_dialogbox);
        timeviewinFrom=view.findViewById(R.id.timeview_in_from);
        timepickertoDialogbox=view.findViewById(R.id.timepickerto_dialogbox);
        timeviewinto=view.findViewById(R.id.timeview_in_to);
        search = view.findViewById(R.id.search_btn);

        userModalArrayList = new ArrayList<>();

        // initializing our views.
        userRV = view.findViewById(R.id.skil_recycycler);
        loadingPB = view.findViewById(R.id.idPBLoading);
        nestedSV = view.findViewById(R.id.idNestedSV);

        USER_ID = PrefConnect.readString(context,PrefConnect.USER_ID,"");

        if(getArguments() != null){

            type = getArguments().getString("type");
            first_name = getArguments().getString("first_name");
            last_name = getArguments().getString("last_name");
            image_name = getArguments().getString("image_name");
            company_name = getArguments().getString("company_name");
            country_id = getArguments().getString("country");
            state_id = getArguments().getString("state");
            city_id = getArguments().getString("city");
            country_name = getArguments().getString("country_name");
            state_name = getArguments().getString("state_name");
            city_name = getArguments().getString("city_name");
            about = getArguments().getString("about");
            email = getArguments().getString("email");
            password = getArguments().getString("password");
            device_type = getArguments().getString("device_type");
            device_token = getArguments().getString("device_token");
            device_model = getArguments().getString("device_model");
            device_version = getArguments().getString("device_version");
            gender = getArguments().getString("gender");
            certificate = getArguments().getString("certificate_id");
            longitude = getArguments().getString("longitude");
            lattitude = getArguments().getString("lattitude");
            referCode = getArguments().getString("refercode");
            liveAddress = getArguments().getString("address");
            code = getArguments().getString("code");

        }

        suggest.setText(GlobalMethods.getString(context,R.string.addSkills));
        edicatName.setText(GlobalMethods.getString(context,R.string.select_category));
        edisearch.setHint(GlobalMethods.getString(context,R.string.search_with_skills_name));

        lang = PrefConnect.readInteger(context,PrefConnect.LANGUAGE,0);

        calSkills(page,"",searchKey);

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
                    calSkills(page,catId,searchKey);
                }
            }
        });

        if(!type.equals("profile_complected")){

            Profileget(USER_ID);

        }

        add_new_skils.setOnClickListener(view1 -> {

            Intent in1 = new Intent(context, ActivityAddNewSkill.class);
            in1.putExtra("user_id",USER_ID);
            startActivity(in1);

        });

        search.setOnClickListener(view2 -> {

            catId = "";
            if(!edisearch.getText().toString().isEmpty()){

                String quesy = edisearch.getText().toString();
                searchKey = quesy;
                page = 1;
                userRVAdapter.ClearAll();
                calSkills(page,"",edisearch.getText().toString());

            }
            else {
                page = 1;
                calSkills(page,catId,"");

            }
        });

        banneritemClickListener = image -> {

            try {

                image.setImageDrawable(null);
                Toast.makeText(context,GlobalMethods.getString(context,R.string.Image_removed), Toast.LENGTH_SHORT).show();

            }
            catch (Exception e){

                e.printStackTrace();
                Toast.makeText(context,GlobalMethods.getString(context,R.string.Some_error_occurred), Toast.LENGTH_SHORT).show();

            }
        };

        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCategory();
            }
        });

        sumitebtn.setOnClickListener(view3 -> {

            Log.e("type",type);
            if(type.equals("edit_skils")){

                Updateskils(USER_ID);

            }

            else{

                calRegister();

            }

        });

        timepickerFromDialogbox.setOnClickListener(view4-> timepickerFromDialog());

        timepickertoDialogbox.setOnClickListener(view5 -> timepickerToDialog());

        itemClickListenerskills = new AdapterSkillsPagenassion.ItemClickListener() {
            @Override
            public void ItemClick(String id, String name) {
                skils = id;
                skillss_name = name;
            }
        };

        itemClickListenercategory = (cat_id, cat_name) -> {

            catName = cat_name;
            catId = cat_id;

        };

        changeLanguage();

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

    private void chooseCategory() {
        timepickerdialog= new Dialog(getActivity());
        timepickerdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timepickerdialog.setContentView(R.layout.dialogbox_addstate);
        timepickerdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timepickerdialog.setCancelable(false);
        TextView ss = timepickerdialog.findViewById(R.id.ss);
        AppCompatButton closeDialog=(AppCompatButton) timepickerdialog.findViewById(R.id.state_dialogbox_close);
        AppCompatButton dialogbox_done = (AppCompatButton) timepickerdialog.findViewById(R.id.state_dialogbox_done);
        RecyclerView recycler_cat = (RecyclerView) timepickerdialog.findViewById(R.id.recycler_state);

        ss.setText(GlobalMethods.getString(context,R.string.select_category));
        dialogbox_done.setText(GlobalMethods.getString(context,R.string.done));
        closeDialog.setText(GlobalMethods.getString(context,R.string.close));

        dialogbox_done.setOnClickListener(view -> {

            timepickerdialog.dismiss();
            edicatName.setText(catName);
            page = 1;

            userRVAdapter.ClearAll();
            loadingPB.setVisibility(View.VISIBLE);
            Log.e("page",page+"");
            Log.e("catId",catId+"");
            calSkills(page,catId,"");

        });

        closeDialog.setOnClickListener(view -> timepickerdialog.dismiss());

        if(GlobalMethods.isNetworkAvailable(context)){
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            Api.getClient().categoryList(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CategoryResponce>() {
                @Override
                public void onResponse(@NonNull Call<CategoryResponce> call, @NonNull Response<CategoryResponce> response) {
                    progressDialog.dismiss();
                    Log.e("mycatery",new Gson().toJson(response.body()));

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){
                        timepickerdialog.show();
                        //  GridLayoutManager linearLayoutManager =new GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,false);
                        adapterChooseCategery = new AdapterChooseCategery(context,itemClickListenercategory,response.body().getCategoryList());
                        recycler_cat.setHasFixedSize(true);
                        recycler_cat.setLayoutManager(new LinearLayoutManager(context));
                        recycler_cat.setAdapter(adapterChooseCategery);


                    }else {

                        GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CategoryResponce> call, @NonNull Throwable t) {
                    progressDialog.dismiss();

                    Log.e("failure professional res",t.getMessage());
                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }

    private Boolean validation(){
        if(banner_images.equals("")){
            GlobalMethods.Toast(context,GlobalMethods.getString(context,R.string.Please_Select_banner));
            return false;
        }
        return true;
    }

    private void Updateskils(String user_id) {

        StringBuilder temp = new StringBuilder();
//        StringBuilder temp1 = new StringBuilder();
//        StringBuilder temp2 = new StringBuilder();
        for(int i=0;i<testModels.size();i++ ){

            temp.append(testModels.get(i).getImagename()).append(",");

        }
//
//        for(int i=0;i<userRVAdapter.subCategoryResponse.size();i++ ){
//
//            temp1.append(userRVAdapter.subCategoryResponse.get(i).getId()).append(", ");
//            temp2.append(userRVAdapter.subCategoryResponse.get(i).getSkillName()).append(", ");
//
//        }
//
//        StringBuffer sb1= new StringBuffer(String.valueOf(temp1));
//        sb1.deleteCharAt(sb1.length()-2);
//        Log.e("temp", String.valueOf(sb1));
//        skils = String.valueOf(sb1);
//
//        StringBuffer sb2= new StringBuffer(String.valueOf(temp2));
//        sb2.deleteCharAt(sb1.length()-2);
//        Log.e("temp", String.valueOf(sb2));
//        skillss_name = String.valueOf(sb2);

        if(String.valueOf(temp).equals(",")){
            banner_images = "";
        }
        else {

            StringBuffer sb= new StringBuffer(String.valueOf(temp));
            sb.deleteCharAt(sb.length()-1);
            Log.e("temp", String.valueOf(sb));
            banner_images = String.valueOf(sb);

        }

        if(GlobalMethods.isNetworkAvailable(getActivity())){
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            Log.e("user_id",user_id);

            Api.getClient().editProfileSkils(user_id,skils,skillss_name,from_time,to_time,banner_images,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<EditskillsModel>() {
                @Override
                public void onResponse(@NonNull Call<EditskillsModel> call, @NonNull Response<EditskillsModel> response) {
                    progressDialog.dismiss();
                    Log.e("skilssuodate",new Gson().toJson(response.body()));
                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            requireActivity().finish();

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EditskillsModel> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    GlobalMethods.Toast(context,t.getMessage());

                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }

    private void onbackword(){

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(type.equals("profile_complected")){

                    Intent intent = new Intent(context,ActivityAddprofileScreen.class);
                    intent.putExtra("code",code);
                    startActivity(intent);
                    requireActivity().finish();

                }else {
                    requireActivity().finish();
                }

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback((LifecycleOwner) context, callback);

    }

    private void Profileget(String  user_id) {
        if(GlobalMethods.isNetworkAvailable(getActivity())){

            Api.getClient().getprofile(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<getprofileModel>() {
                @Override
                public void onResponse(@NonNull Call<getprofileModel> call, @NonNull Response<getprofileModel> response) {
                    Log.e("get_sk_profile", new Gson().toJson(response.body()));

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            from_time=response.body().getDetails().getAvailableFrom();
                            to_time=response.body().getDetails().getAvailableTo();
                            skils=response.body().getDetails().getSkilId();
                            skillss_name=response.body().getDetails().getSkillsName();
                            timeviewinFrom.setText(response.body().getDetails().getAvailableFrom());
                            timeviewinto.setText(response.body().getDetails().getAvailableTo());

                            String image = response.body().getDetails().getBannerImage();
                            String imageurl = response.body().getDetails().getImageUrl();
                            String[] arrSplit = image.split(",");

                            for (int i=0; i < arrSplit.length; i++){

                                String imgurl=imageurl + arrSplit[i];

                                testModels.add(new PostImageModel(imgurl,arrSplit[i]));

                                testAdapter = new AdapterPostImage(context,banneritemClickListener,testModels);
                                recycler_images.setHasFixedSize(true);
                                recycler_images.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                                recycler_images.setAdapter(testAdapter);

                            }

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<getprofileModel> call, @NonNull Throwable t) {

                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }


    }

    private void profile_image_status() {
        if(GlobalMethods.isNetworkAvailable(context)){
            Api.getClient().profileimagestatus(USER_ID,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<profileimagestatusModel>() {
                @Override
                public void onResponse(@NonNull Call<profileimagestatusModel> call, @NonNull Response<profileimagestatusModel> response) {

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){


                        }else {


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

    public void calRegister(){

        StringBuilder temp = new StringBuilder();
//        StringBuilder temp1 = new StringBuilder();
//        StringBuilder temp2 = new StringBuilder();
        for(int i=0;i<testModels.size();i++ ){

            temp.append(testModels.get(i).getImagename()).append(",");

        }

//        for(int i=0;i<userRVAdapter.subCategoryResponse.size();i++ ){
//
//            temp1.append(userRVAdapter.subCategoryResponse.get(i).getId()).append(", ");
//            temp2.append(userRVAdapter.subCategoryResponse.get(i).getSkillName()).append(", ");
//
//        }
//
//        StringBuffer sb1= new StringBuffer(String.valueOf(temp1));
//        sb1.deleteCharAt(sb1.length()-2);
//        Log.e("temp", String.valueOf(sb1));
//        skils = String.valueOf(sb1);
//
//        StringBuffer sb2= new StringBuffer(String.valueOf(temp2));
//        sb2.deleteCharAt(sb1.length()-2);
//        Log.e("temp", String.valueOf(sb2));
//        skillss_name = String.valueOf(sb2);

        if(testModels.size() == 0){

            banner_images = "";

        }
        else {

            StringBuffer sb= new StringBuffer(String.valueOf(temp));
            sb.deleteCharAt(sb.length()-1);
            banner_images = String.valueOf(sb);

        }
        
        Log.e("user_id",USER_ID+"test");
        Log.e("first_name",first_name+"test");
        Log.e("last_name",last_name+"test");
        Log.e("image_name",image_name+"test");
        Log.e("company_name",company_name+"test");
        Log.e("country_id",country_id+"test");
        Log.e("state_id",state_id+"test");
        Log.e("city_id",city_id+"test");
        Log.e("country_name",country_name+"test");
        Log.e("state_name",state_name+"test");
        Log.e("city_name",city_name+"test");
        Log.e("about",about+"test");
        Log.e("email",email+"test");
        Log.e("password",password+"test");
        Log.e("certificate",certificate+"test");
        Log.e("device_type",device_type+"test");
        Log.e("device_token",device_token+"test");
        Log.e("device_model",device_model+"test");
        Log.e("device_version",device_version+"test");
        Log.e("gender",gender+"test");
        Log.e("skils",skils+"test");
        Log.e("skils name",skillss_name+"test");
        Log.e("from_time",from_time+"test");
        Log.e("to_time",to_time+"test");
        Log.e("lattitude",lattitude+"test");
        Log.e("longitude",longitude+"test");


        if (GlobalMethods.isNetworkAvailable(getActivity())) {
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);

            Api.getClient().userRegister(USER_ID,first_name,last_name,image_name,company_name,country_id,state_id,city_id,country_name,
                    state_name,city_name,about,email,password,
                    certificate,device_type,device_token,device_model,device_version,gender,skils,from_time,to_time,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""),lattitude,longitude,liveAddress,skillss_name,banner_images,
                    referCode).enqueue(new Callback<ProfileUpload>() {
                @Override
                public void onResponse(@NonNull Call<ProfileUpload> call, @NonNull Response<ProfileUpload> response) {
                    Log.e("susss",new Gson().toJson(response.body()));
                    progressDialog.dismiss();
                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            PrefConnect.writeString(context,PrefConnect.USER_NAME,response.body().getProfileDetails().getFirstName()+" "+response.body().getProfileDetails().getLastName());
                            PrefConnect.writeString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"yes");
                            PrefConnect.writeString(context,PrefConnect.USER_IMAGE_URL,response.body().getProfileDetails().getImageUrl());
                            PrefConnect.writeString(context,PrefConnect.USER_PROFILE_VERIFIED_STATUS,response.body().getProfileDetails().getImageApprove());
                            PrefConnect.writeString(context,PrefConnect.USER_IMAGE_NAME,response.body().getProfileDetails().getImageName());
                            PrefConnect.writeString(context,PrefConnect.DEVICE_TOKEN,response.body().getProfileDetails().getDeviceToken());
                            PrefConnect.writeString(context,PrefConnect.DEVICE_VERSION,response.body().getProfileDetails().getDeviceVersion());
                            PrefConnect.writeString(context,PrefConnect.DEVICE_MODEL,response.body().getProfileDetails().getDeviceModel());
                            PrefConnect.writeString(context,PrefConnect.DEVICE_TYPE,response.body().getProfileDetails().getDeviceType());
                            PrefConnect.writeString(context,PrefConnect.USER_ID,USER_ID);
                            Intent intent=new Intent(context, ActivityBottom.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            requireActivity().finish();
                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                        else{

                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProfileUpload> call, @NonNull Throwable t) {
                    progressDialog.dismiss();

                    Log.e("failure res",t.getMessage());
                }
            });

        }
        else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }

    public void calSkills(int page,String catId,String key){

        if (GlobalMethods.isNetworkAvailable(getActivity())) {

            skillsList = new ArrayList<>();
            String lan= PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"en");
            Log.e("skillsList",new Gson().toJson(lan));
            Log.e("page", String.valueOf(page));
            Log.e("catId", String.valueOf(catId));
           NewApi.getClient().newsubcategoryList(USER_ID,catId,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""), String.valueOf(page),key).enqueue(new Callback<SubcategoryResponse>() {
                @Override
                public void onResponse(@NonNull Call<SubcategoryResponse> call, @NonNull Response<SubcategoryResponse> response) {
                    Log.e("sillslist",new Gson().toJson(response.body()));
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            userModalArrayList.addAll(response.body().getSkillList());

                            int totalCount = response.body().getTotalCount();
                            loadingPB.setVisibility(View.GONE);

                            // passing array list to our adapter class.
                            userRVAdapter = new AdapterSkillsPagenassion(userModalArrayList,itemClickListenerskills,context);

                            // setting layout manager to our recycler view.
                            userRV.setLayoutManager(new GridLayoutManager(context,2));

                            // setting adapter to our recycler view.
                            userRV.setAdapter(userRVAdapter);

                        }else {

                            loadingPB.setVisibility(View.GONE);
                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SubcategoryResponse> call, @NonNull Throwable t) {
                    GlobalMethods.Toast(context,t.getMessage());
                    Log.e("failure res",t.getMessage());
                }
            });

        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }

    private void timepickerFromDialog() {

        timepickerdialog= new Dialog(getActivity());
        timepickerdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timepickerdialog.setContentView(R.layout.dialogbox_timepicker);
        timepickerdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timepickerdialog.show();
        timepickerdialog.setCancelable(false);

        tp1=timepickerdialog.findViewById(R.id.tp1);

        timePicker = (TimePicker) timepickerdialog.findViewById(R.id.simpleTimePicker);

        calendar = Calendar.getInstance();


        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {

            if (hourOfDay == 0) {

                hourOfDay += 12;
                format = "AM";

            }
            else if (hourOfDay == 12) {

                format = "PM";
            }
            else if (hourOfDay > 12) {

                hourOfDay -= 12;
                format = "PM";

            }
            else {

                format = "AM";

            }

            timeviewinFrom.setText(hourOfDay + ":" + minute + " " +format);
            from_time = timeviewinFrom.getText().toString();

        });

        timePickerDialogClose=(Button) timepickerdialog.findViewById(R.id.timpicker_dialogbox_close);
        timePickerDialogClose.setOnClickListener(view -> timepickerdialog.dismiss());

        languagechangetime();

    }

    private void timepickerToDialog() {

        timepickerdialog = new Dialog(getActivity());
        timepickerdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timepickerdialog.setContentView(R.layout.dialogbox_timepicker);
        timepickerdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timepickerdialog.show();
        timepickerdialog.setCancelable(false);

        timePicker = (TimePicker) timepickerdialog.findViewById(R.id.simpleTimePicker);
        tp1=timepickerdialog.findViewById(R.id.tp1);

        calendar = Calendar.getInstance();

        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {

            if (hourOfDay == 0) {

                hourOfDay += 12;
                format = "AM";

            }
            else if (hourOfDay == 12) {

                format = "PM";

            }
            else if (hourOfDay > 12) {

                hourOfDay -= 12;
                format = "PM";

            }
            else {

                format = "AM";

            }

            timeviewinto.setText(hourOfDay + ":" + minute + " " + format);
            to_time = timeviewinto.getText().toString();

        });

        timePickerDialogClose = (Button) timepickerdialog.findViewById(R.id.timpicker_dialogbox_close);

        timePickerDialogClose.setOnClickListener(view -> timepickerdialog.dismiss());

        languagechangetime();

    }

    private void setVerifiedDialog() {
        camera_dialog = new Dialog(getActivity());
        camera_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        camera_dialog.setContentView(R.layout.dialog_photo);
        camera_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        camera_dialog.show();
        camera_dialog.setCancelable(false);
        select_p=camera_dialog.findViewById(R.id.select_p);
        txt_gallery =  camera_dialog.findViewById(R.id.txt_gallery);
        txt_take_photo =  camera_dialog.findViewById(R.id.txt_take_photo);
        btn_cancel_dialog =  camera_dialog.findViewById(R.id.btn_cancel_dialog);

        btn_cancel_dialog.setOnClickListener(view -> camera_dialog.dismiss());

        txt_gallery.setOnClickListener(view -> {

            camera_dialog.dismiss();


        });

        txt_take_photo.setOnClickListener(view -> {

            camera_dialog.dismiss();


        });

        languagechangecamara();

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

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), "profile_image");
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
        Api.getClient().profileImageUpload(fileToUpload,filename).enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(@NonNull Call<ImageUpload> call, @NonNull Response<ImageUpload> response) {
                Log.e("success response", new Gson().toJson(response.body()));
                progressDialog.dismiss();

                assert response.body() != null;
                if(response.body().getStatus().equals("1")){
                    String img = response.body().getFileName();
                    testModels.add(new PostImageModel(response.body().getFileUrl(),img));
                    images_name.add(img);
                    String formattedString = images_name.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")
                            .replace(" ","")//remove the left bracket
                            .trim();

                    testAdapter = new AdapterPostImage(context,banneritemClickListener,testModels);
                    recycler_images.setHasFixedSize(true);
                    recycler_images.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    recycler_images.setAdapter(testAdapter);

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



    private void changeLanguage() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            abimage.setText(resources.getText(R.string.add_banner_images));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));



            if(type.equals("profile_complected")){

                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{

                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));

            }



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            abimage.setText(resources.getText(R.string.add_banner_images));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("profile_complected")){

                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{

                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));

            }


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            abimage.setText(resources.getText(R.string.add_banner_images));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("profile_complected")){

                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{

                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));

            }


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();
            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            abimage.setText(resources.getText(R.string.add_banner_images));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("profile_complected")){

                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{


                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));

            }


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            abimage.setText(resources.getText(R.string.add_banner_images));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("profile_complected")){

                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{


                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));

            }


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            abimage.setText(resources.getText(R.string.add_banner_images));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("profile_complected")){

                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{


                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));

            }

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            abimage.setText(resources.getText(R.string.add_banner_images));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("profile_complected")){

                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{

                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));

            }
        }
    }
    private void languagechangetime() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            tp1.setText(resources.getText(R.string.select_time));
            timePickerDialogClose.setText(resources.getText(R.string.ok));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            tp1.setText(resources.getText(R.string.select_time));
            timePickerDialogClose.setText(resources.getText(R.string.ok));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            tp1.setText(resources.getText(R.string.select_time));
            timePickerDialogClose.setText(resources.getText(R.string.ok));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            tp1.setText(resources.getText(R.string.select_time));
            timePickerDialogClose.setText(resources.getText(R.string.ok));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            tp1.setText(resources.getText(R.string.select_time));
            timePickerDialogClose.setText(resources.getText(R.string.ok));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            tp1.setText(resources.getText(R.string.select_time));
            timePickerDialogClose.setText(resources.getText(R.string.ok));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            tp1.setText(resources.getText(R.string.select_time));
            timePickerDialogClose.setText(resources.getText(R.string.ok));
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