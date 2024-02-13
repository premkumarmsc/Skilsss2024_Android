package com.bannet.skils.language.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.profile.activity.ActivityAddprofileScreen;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLanguageScreen extends AppCompatActivity {

    private Context context;
    public Dialog dialog;
    private ProgressDialog progressDialog;
    public LinearLayout submitbtn;
    private int languageId=0;
    public String user_id;
    public Resources resources;
    public TextView l_s_tv2,l_s_tv3,l_s_tv1,l_s_5;
    private String type;
    public TextView title;
    private AppCompatButton ok,cancel;
    public CheckBox english,hindi,malayalam,tamil,french,spanish,arabic;
    public ImageView languagescreen_back_btn;

    public String LANGUAGE;
    public int LANGUAGEID;
    public String languageParm,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_screen);

        if(getSupportActionBar() != null){

            getSupportActionBar().hide();

        }
        context=ActivityLanguageScreen.this;

        init();

    }

    private void init(){

        english = findViewById(R.id.language_english);
        hindi = findViewById(R.id.language_hindi);
        malayalam = findViewById(R.id.language_malayalam);
        tamil = findViewById(R.id.language_tamil);
        french = findViewById(R.id.language_french);
        spanish = findViewById(R.id.language_spanish);
        arabic = findViewById(R.id.language_arabic);
        languagescreen_back_btn = findViewById(R.id.back_btn);
        submitbtn= findViewById(R.id.update_btn);
        l_s_tv2 = findViewById(R.id.l_s_tv1);
        l_s_tv1 = findViewById(R.id.l_s_tv2);
        l_s_tv3 = findViewById(R.id.l_s_tv3);
        l_s_5 = findViewById(R.id.l_s_5);


        type=getIntent().getStringExtra("select");
        code=getIntent().getStringExtra("code");
        user_id = getIntent().getStringExtra("user_id");

        Log.e("user_id",user_id);


        LANGUAGEID=PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1);

        if(LANGUAGEID == 0){

            languageId = 0;

        }else{

            languageId=LANGUAGEID;

        }

        english.setText(GlobalMethods.getString(context,R.string.english));
        hindi.setText(GlobalMethods.getString(context,R.string.hindi));
        malayalam.setText(GlobalMethods.getString(context,R.string.malayalam));
        tamil.setText(GlobalMethods.getString(context,R.string.tamil));
        french.setText(GlobalMethods.getString(context,R.string.french));
        spanish.setText(GlobalMethods.getString(context,R.string.spanish));
        arabic.setText(GlobalMethods.getString(context,R.string.arabic));
        l_s_tv1.setText(GlobalMethods.getString(context,R.string.changeLanguage));
        l_s_tv2.setText(GlobalMethods.getString(context,R.string.regional_language));
        l_s_tv3.setText(GlobalMethods.getString(context,R.string.international_language));

        if(type.equals("settings")){
            l_s_5.setText(GlobalMethods.getString(context,R.string.UPDATE));

        }
        else {
            l_s_5.setText(GlobalMethods.getString(context,R.string.next));
        }

        english.setOnClickListener(view -> {

            languageId = 1;
            hindi.setChecked(false);
            malayalam.setChecked(false);
            tamil.setChecked(false);
            french.setChecked(false);
            spanish.setChecked(false);
            arabic.setChecked(false);

        });

        hindi.setOnClickListener(view -> {

            languageId = 2;
            english.setChecked(false);
            malayalam.setChecked(false);
            tamil.setChecked(false);
            french.setChecked(false);
            spanish.setChecked(false);
            arabic.setChecked(false);

        });

        malayalam.setOnClickListener(view -> {

            languageId = 3;
            hindi.setChecked(false);
            english.setChecked(false);
            tamil.setChecked(false);
            french.setChecked(false);
            spanish.setChecked(false);
            arabic.setChecked(false);

        });

        tamil.setOnClickListener(view -> {

            languageId = 4;
            hindi.setChecked(false);
            malayalam.setChecked(false);
            english.setChecked(false);
            french.setChecked(false);
            spanish.setChecked(false);
            arabic.setChecked(false);

        });

        french.setOnClickListener(view -> {

            languageId = 5;
            hindi.setChecked(false);
            malayalam.setChecked(false);
            tamil.setChecked(false);
            english.setChecked(false);
            spanish.setChecked(false);
            arabic.setChecked(false);

        });

        spanish.setOnClickListener(view -> {

            languageId = 6;
            hindi.setChecked(false);
            malayalam.setChecked(false);
            tamil.setChecked(false);
            french.setChecked(false);
            english.setChecked(false);
            arabic.setChecked(false);

        });

        arabic.setOnClickListener(view -> {

            languageId = 7;
            hindi.setChecked(false);
            malayalam.setChecked(false);
            tamil.setChecked(false);
            french.setChecked(false);
            spanish.setChecked(false);
            english.setChecked(false);

        });

        languageParm = PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"");

        if(languageParm.equals("") || languageParm.isEmpty()){

            languageParm = "en";

        }
        else {
            languageParm = PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"");
        }

        languagescreen_back_btn.setOnClickListener(v -> {

            if(type.equals("select")){

                startActivity(new Intent(context, ActivityPhonenumberScreen.class));
                finish();

            }
            else{

                finish();

            }

        });

       // user_id = PrefConnect.readString(context,PrefConnect.USER_ID,"");
        LANGUAGE=PrefConnect.readString(context,PrefConnect.LANGUAGE_NAME,"");

        if(languageId !=0){

            switch (languageId) {

                case 1:

                    english.setChecked(true);
                    PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"en");
                    languageParm = "en";
                    break;

                case 2:
                    PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"hi");
                    hindi.setChecked(true);
                    languageParm = "hi";
                    break;

                case 3:
                    PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"ml");
                    malayalam.setChecked(true);
                    languageParm = "ml";
                    break;

                case 4:
                    PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"ta");
                    tamil.setChecked(true);
                    languageParm = "ta";
                    break;

                case 5:
                    PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"fr");
                    french.setChecked(true);
                    languageParm = "fr";
                    break;

                case 6:
                    PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"es");
                    spanish.setChecked(true);
                    languageParm = "es";
                    break;

                case 7:
                    PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"ar");
                    arabic.setChecked(true);
                    languageParm = "ar";
                    break;

            }

        }

        submitbtn.setOnClickListener(view -> {

            if(Validation()){

                updateLanguage();

            }
        });

    }

    public Boolean Validation(){

        if(languageId==0){

            GlobalMethods.Toast(context,GlobalMethods.getString(context,R.string.Please_Select_Your_Language));

            return false;

        }
        return true;
    }

    private void updateLanguage() {

        Log.e("languageParm",languageParm);
        Log.e("user_id",user_id);
        if (GlobalMethods.isNetworkAvailable(context)) {

            progressDialog = ProgressDialog.show(ActivityLanguageScreen.this, "", "Loading...", true);
            Api.getClient().updateLanguage(user_id,languageParm).enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    Log.e("suess", new Gson().toJson(response.body()));

                    progressDialog.dismiss();

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            if(languageId == 1){

                                PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"en");

                            }else if(languageId == 2){

                                PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"hi");

                            }else if(languageId == 3){

                                PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"ml");

                            }else if(languageId == 4){

                                PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"ta");

                            }else if(languageId == 5){

                                PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"fr");

                            }else if(languageId == 6){

                                PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"es");

                            }else if(languageId == 7){

                                PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"ar");

                            }

                            if(type.equals("select")){

                                PrefConnect.writeInteger(context,PrefConnect.LANGUAGE,languageId);
                                Intent intent = new Intent(context,ActivityAddprofileScreen.class);
                                intent.putExtra("code",code);
                                intent.putExtra("user_id",user_id);
                                startActivity(intent);
                                finish();

                            }else{

                                PrefConnect.writeInteger(context,PrefConnect.LANGUAGE,languageId);
                                startActivity(new Intent(context, ActivityBottom.class));
                                finish();

                            }

                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {

                    progressDialog.dismiss();

                }
            });

        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }

    @Override
    public void onBackPressed() {

        if(type.equals("select")){

            dialog = new Dialog(ActivityLanguageScreen.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_otp_back_screen);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            title=dialog.findViewById(R.id.title);
            cancel =  dialog.findViewById(R.id.cancel);
            ok =  dialog.findViewById(R.id.ok);

            title.setText(GlobalMethods.getString(context,R.string.are_you_sure_do_you_want_to_back));
            cancel.setText(GlobalMethods.getString(context,R.string.cancel));
            ok.setText(GlobalMethods.getString(context,R.string.ok));

            cancel.setOnClickListener(v -> dialog.dismiss());

            ok.setOnClickListener(v -> {
                dialog.dismiss();
                if(type.equals("select")){

                    startActivity(new Intent(context,ActivityPhonenumberScreen.class));
                    finish();

                }
                else{

                    finish();

                }

            });


        }
        else{

            finish();

        }



    }

//    private void callLanguage() {
//
//        if (GlobalMethods.isNetworkAvailable(context)) {
//
//            Api.getClient().LanguageList().enqueue(new Callback<SelectLanguageModel>() {
//
//                @Override
//                public void onResponse(@NonNull Call<SelectLanguageModel> call, @NonNull Response<SelectLanguageModel> response) {
//                    Log.e("success response", new Gson().toJson(response.body()));
//
//                    if(response.isSuccessful()){
//
//                        assert response.body() != null;
//                        if(response.body().getStatus().equals("1")){
//
//                            adapterLanguageList = new AdapterLanguageList(context,itemClickListener,response.body().getLanguageList());
//                            languageList.setHasFixedSize(true);
//                            languageList.setLayoutManager(new LinearLayoutManager(context));
//                            languageList.setAdapter(adapterLanguageList);
//
//                        }
//                        else {
//
//                            GlobalMethods.Toast(context,response.body().getMessage());
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<SelectLanguageModel> call, @NonNull Throwable t) {
//
//                    Log.e("failure res",t.getMessage());
//                }
//            });
//
//        }else {
//            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
//        }
//    }


}