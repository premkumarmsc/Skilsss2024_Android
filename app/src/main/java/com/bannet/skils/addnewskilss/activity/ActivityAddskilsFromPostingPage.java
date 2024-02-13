package com.bannet.skils.addnewskilss.activity;

import static java.lang.Math.abs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.addskilss.adapter.AdapterChooseCategery;
import com.bannet.skils.addskilss.adapter.AdapterSkillsPagenassion;
import com.bannet.skils.addskilss.adapter.UserRVAdapter;
import com.bannet.skils.explore.response.CategoryResponce;
import com.bannet.skils.service.NewApi;
import com.bannet.skils.subcategoryhome.responce.SubcategoryResponse;
import com.google.gson.Gson;
import com.bannet.skils.Activitys.ActivityAddNewSkill;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  ActivityAddskilsFromPostingPage extends AppCompatActivity {

    public RelativeLayout sumitebtn,selectCategory;
    public Context context;
    public LinearLayoutCompat timepickerFromDialogbox,timepickertoDialogbox;
    public ImageView backbtn;
    public CardView add_new_skils;
    private TimePicker timePicker;
    public Dialog timepickerdialog;
    public Button timePickerDialogClose;
    public TextView timeviewinFrom,timeviewinto;
    public Resources resources;
    public TextView a_s_s_tv1,a_s_s_tv2,a_s_s_tv3,a_s_s_tv4;
    public AdapterSkillsPagenassion adapterSkills;
    public AdapterSkillsPagenassion.ItemClickListener itemClickListener;
    public List<SubcategoryResponse.Skill> skillsList;
    public ProgressDialog progressDialog;
    public RecyclerView skil_recycycler;
    public String format;
    public String from_time,to_time,skils ="",skills_namee="";
    public String user_id;
    public String type;
    public TextView tp1,edicatName,suggest;
    private AdapterChooseCategery adapterChooseCategery;
    public AdapterChooseCategery.ItemClickListener itemClickListenercategory;
    public String catName="",catId="";
    public LinearLayout banner_post;
    public RelativeLayout search;
    public EditText edisearch;


    private ArrayList<SubcategoryResponse.Skill> userModalArrayList;
    public UserRVAdapter.ItemClickListener itemClickListenerskills;
    private UserRVAdapter userRVAdapter;
    private RecyclerView userRV;
    private String searchKey = "";
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    int page = 1, limit = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addskilsposting);
        getSupportActionBar().hide();
        context= ActivityAddskilsFromPostingPage.this;

        sumitebtn=(RelativeLayout) findViewById(R.id.skilpage_sumite_btn);
        backbtn=findViewById(R.id.img_back);
        add_new_skils = findViewById(R.id.add_new_skils);
        banner_post=findViewById(R.id.banner_post);
        selectCategory = findViewById(R.id.selectcat);
        edicatName = findViewById(R.id.edit_catogery);
        suggest = findViewById(R.id.suggest);
        search = findViewById(R.id.search_btn);
        edisearch = findViewById(R.id.edi_search);

        a_s_s_tv1=findViewById(R.id.a_s_s_tv1);
        a_s_s_tv2=findViewById(R.id.avt);
        a_s_s_tv3=findViewById(R.id.a_s_s_tv3);
        a_s_s_tv4=findViewById(R.id.a_s_s_tv4);

        type=getIntent().getStringExtra("type");
        banner_post.setVisibility(View.GONE);

        userModalArrayList = new ArrayList<>();

        // initializing our views.
        userRV = findViewById(R.id.skil_recycycler);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);

        // calling a method to load our api.
        calSkills(page,catId,searchKey);

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

        timepickerFromDialogbox=findViewById(R.id.timepickerfrom_dialogbox);
        timeviewinFrom=findViewById(R.id.timeview_in_from);
        timepickertoDialogbox=findViewById(R.id.timepickerto_dialogbox);
        timeviewinto=findViewById(R.id.timeview_in_to);

        user_id = PrefConnect.readString(context,PrefConnect.USER_ID,"");

        add_new_skils.setOnClickListener(view -> {

          Intent in = new Intent(context, ActivityAddNewSkill.class);
          startActivity(in);

        });

        suggest.setText(GlobalMethods.getString(context,R.string.addSkills));

        search.setOnClickListener(view -> {

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

        itemClickListenerskills = new UserRVAdapter.ItemClickListener() {
            @Override
            public void ItemClick(String id, String name) {
                skils = id;
                skills_namee = name;
            }
        };

        changeLanguage();

        sumitebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("image",skills_namee);
                returnIntent.putExtra("id",skils);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });

        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCategory();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("addpost")){
                    finish();
                }
                else{
                    finish();
                }

            }
        });

        timepickerFromDialogbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepickerFromDialog();
            }
        });
        timepickertoDialogbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepickerToDialog();
            }
        });



        itemClickListenercategory = new AdapterChooseCategery.ItemClickListener() {
            @Override
            public void ItemClick(String cat_id, String cat_name) {
                catName = cat_name;
                catId = cat_id;
            }
        };


    }

    private void chooseCategory() {

        timepickerdialog= new Dialog(ActivityAddskilsFromPostingPage.this);
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

            userRVAdapter.ClearAll();
            loadingPB.setVisibility(View.VISIBLE);
            timepickerdialog.dismiss();
            edicatName.setText(catName);
            page = 1;
            searchKey = edisearch.getText().toString();
            calSkills(page,catId,searchKey);

        });

        closeDialog.setOnClickListener(view -> timepickerdialog.dismiss());

        if(GlobalMethods.isNetworkAvailable(context)){
            progressDialog = ProgressDialog.show(ActivityAddskilsFromPostingPage.this, "", "Loading...", true);
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

    public void calSkills(int page,String catId,String Key){

        if (GlobalMethods.isNetworkAvailable(ActivityAddskilsFromPostingPage.this)) {

            skillsList = new ArrayList<>();
            String lan= PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"en");
            Log.e("sillslist",new Gson().toJson(lan));
            Log.e("page", String.valueOf(page));
            Api.getClient().subcategoryList(catId,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""), String.valueOf(page),Key).enqueue(new Callback<SubcategoryResponse>() {
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
                            userRVAdapter = new UserRVAdapter(userModalArrayList,itemClickListenerskills,context);

                            // setting layout manager to our recycler view.
                            userRV.setLayoutManager(new GridLayoutManager(context,2));

                            // setting adapter to our recycler view.
                            userRV.setAdapter(userRVAdapter);


                        }else {
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

        timepickerdialog= new Dialog(ActivityAddskilsFromPostingPage.this);
        timepickerdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timepickerdialog.setContentView(R.layout.dialogbox_timepicker);
        timepickerdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timepickerdialog.show();
        timepickerdialog.setCancelable(false);

        timePicker = (TimePicker) timepickerdialog.findViewById(R.id.simpleTimePicker);
        timePicker.setIs24HourView(false);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeviewinFrom.setText(hourOfDay + ":" + minute );
                from_time = timeviewinFrom.getText().toString();
            }
        });

      /*  calendar = Calendar.getInstance();
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);*/

    /*    timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
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
            }

        });*/
        timePickerDialogClose=(Button) timepickerdialog.findViewById(R.id.timpicker_dialogbox_close);
        timePickerDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepickerdialog.dismiss();
            }
        });
    }

    private void timepickerToDialog() {

        timepickerdialog = new Dialog(ActivityAddskilsFromPostingPage.this);
        timepickerdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timepickerdialog.setContentView(R.layout.dialogbox_timepicker);
        timepickerdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timepickerdialog.show();
        timepickerdialog.setCancelable(false);

        timePicker = (TimePicker) timepickerdialog.findViewById(R.id.simpleTimePicker);
        timePicker.setIs24HourView(false);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeviewinto.setText(hourOfDay + ":" + minute + " " + format);
                to_time = timeviewinto.getText().toString();
            }
        });

     /*   calendar = Calendar.getInstance();
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
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
            }

        });*/
        timePickerDialogClose = (Button) timepickerdialog.findViewById(R.id.timpicker_dialogbox_close);
        timePickerDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepickerdialog.dismiss();
            }
        });



    }

    private void changeLanguage() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));


            if(type.equals("addpost")){
                a_s_s_tv1.setText(resources.getText(R.string.addSkills));
                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{
                a_s_s_tv1.setText(resources.getText(R.string.edit_skills));
                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));
            }



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("addpost")){
                a_s_s_tv1.setText(resources.getText(R.string.addSkills));
                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{
                a_s_s_tv1.setText(resources.getText(R.string.edit_skills));
                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));
            }


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("addpost")){
                a_s_s_tv1.setText(resources.getText(R.string.addSkills));
                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{
                a_s_s_tv1.setText(resources.getText(R.string.edit_skills));
                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));
            }


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("addpost")){
                a_s_s_tv1.setText(resources.getText(R.string.addSkills));
                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{
                a_s_s_tv1.setText(resources.getText(R.string.edit_skills));
                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));
            }


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("addpost")){
                a_s_s_tv1.setText(resources.getText(R.string.addSkills));
                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{
                a_s_s_tv1.setText(resources.getText(R.string.edit_skills));
                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));
            }


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("addpost")){
                a_s_s_tv1.setText(resources.getText(R.string.addSkills));
                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{
                a_s_s_tv1.setText(resources.getText(R.string.edit_skills));
                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));
            }

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            a_s_s_tv2.setText(resources.getText(R.string.availabilityTiming));
            a_s_s_tv3.setText(resources.getText(R.string.selectSkills));
            timeviewinFrom.setText(resources.getText(R.string.from));
            timeviewinto.setText(resources.getText(R.string.to));

            if(type.equals("addpost")){
                a_s_s_tv1.setText(resources.getText(R.string.addSkills));
                a_s_s_tv4.setText(resources.getText(R.string.submit));
            }
            else{
                a_s_s_tv1.setText(resources.getText(R.string.edit_skills));
                a_s_s_tv4.setText(resources.getText(R.string.UPDATE));
            }


        }
    }

}