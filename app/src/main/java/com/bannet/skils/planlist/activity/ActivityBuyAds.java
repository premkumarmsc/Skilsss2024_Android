package com.bannet.skils.planlist.activity;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.bannet.skils.Activitys.ActivityAdminPageUrl;
import com.bannet.skils.Activitys.ActivityPlanDetails;
import com.bannet.skils.Activitys.ActivityUpgradePlanList;
import com.bannet.skils.Adapter.AdapterAdsCoverage;
import com.bannet.skils.service.IAPHelper;
import com.bannet.skils.Model.AdsCoverageList;
import com.bannet.skils.Model.WebModel.AdvertismentModel;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityBuyAds extends AppCompatActivity implements IAPHelper.IAPHelperListener  {

    ImageView img_back;
    Context context;
    AdapterAdsCoverage adapterAdsCoverage;
    AdapterAdsCoverage.ItemClickListener itemClickListener;
    List<AdsCoverageList.Plan> adsCoverageLists;
    RecyclerView recycler_ads;
    ProgressDialog progressDialog;
    LinearLayout linkshow;
    String USER_ID;
    String longitude,lattitude;
    ImageView bottom_add_image;
    Button user_plan_list;
    TextView add_purchase_link;

    //language
    Resources resources;
    TextView a1,a2,a3,a4;
    Dialog dialogbox;
    Button dialogClose,dialogDone;
    TextView cm1,cm2;
    public String notificationId;

    IAPHelper iapHelper;
    HashMap<String, SkuDetails> skuDetailsHashMap = new HashMap<>();

    String plan_local="skilsss_local_plan_29.33";
    String plan_silver="skilsss_silver_plan_59.99";
    String plan_gold="skilsss_gold_plan_149.99";

    //skilsss_gold_plan_149.99
    //skilsss_local_plan_29.33
    //skilsss_silver_plan_59.99
    private List<String> skuList = Arrays.asList(plan_local,plan_silver,plan_gold);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adds);
        Objects.requireNonNull(getSupportActionBar()).hide();
        context = ActivityBuyAds.this;
       // iapHelper = new IAPHelper(this, this, skuList);
        init();
        planList();
//        getAdvertisment();

    }

    public void init(){

        a1=findViewById(R.id.ba1);
        a2=findViewById(R.id.ba2);
        a3=findViewById(R.id.ba3);
        a4=findViewById(R.id.ba4);

        changeLanguage();

        img_back =  findViewById(R.id.img_back);
        recycler_ads =  findViewById(R.id.recycler_ads);
        linkshow=findViewById(R.id.website_show_link);
        bottom_add_image=findViewById(R.id.bottom_add_image);
        add_purchase_link=findViewById(R.id.txt_add_purchase_link);
        user_plan_list=findViewById(R.id.user_plan_list);

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");
        longitude=PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude=PrefConnect.readString(context,PrefConnect.LATITUDE,"");

        userCheck(USER_ID);

        img_back.setOnClickListener(view -> finish());

        add_purchase_link.setOnClickListener(view -> {

            Intent intent=new Intent(context, ActivityAdminPageUrl.class);
            startActivity(intent);

        });

        user_plan_list.setOnClickListener(v -> {

            Intent intent=new Intent(context, ActivityUpgradePlanList.class);
            startActivity(intent);
        });

        user_plan_list.setText(GlobalMethods.getString(context,R.string.my_ads));

        itemClickListener= (id, validity, planName, coverage, cost) -> {

            dialogbox = new Dialog(ActivityBuyAds.this);
            dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogbox.setContentView(R.layout.dialog_confirmplan_active);
            dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogbox.show();
            dialogbox.setCancelable(false);

            cm1 = dialogbox.findViewById(R.id.cm1);
            cm2 = dialogbox.findViewById(R.id.cm2);
            dialogClose =  dialogbox.findViewById(R.id.btn_cancel_dialog);
            dialogDone =  dialogbox.findViewById(R.id.btn_ok_dialog);
            changeLanguagecon();

            dialogDone.setOnClickListener(v -> {

                dialogbox.dismiss();

             //   launch(plan_local);
                Intent intent = new Intent(context, ActivityPlanDetails.class);
                intent.putExtra("plan_id", id);
                intent.putExtra("plan_validity", validity);
                intent.putExtra("plan_name", planName);
                intent.putExtra("plan_coverage", coverage);
                intent.putExtra("plan_cost", cost);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            });
            dialogClose.setOnClickListener(view -> dialogbox.dismiss());
        };

        try {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {

                notificationId = extras.getString("notification_id");

                Log.e("card_id",notificationId);

                singlenotificationDelete(notificationId);

            }
        }
        catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void singlenotificationDelete(String id){

        if(GlobalMethods.isNetworkAvailable(ActivityBuyAds.this)){
            Api.getClient().singleNotificationDelete(id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    Log.e("sucss",new Gson().toJson(response.body()));


                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {


                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    public void userCheck(String id){

        if(GlobalMethods.isNetworkAvailable(ActivityBuyAds.this)){

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
                            finish();

                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {


                }
            });
        }
        else {

            GlobalMethods.Toast(context, "No Internet Connection");

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //userCheck(USER_ID);
    }

    public void planList(){

        adsCoverageLists = new ArrayList<>();
        if(GlobalMethods.isNetworkAvailable(ActivityBuyAds.this)){
            progressDialog = ProgressDialog.show(this, "", "Loading...", true);
            Api.getClient().showPlan(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<AdsCoverageList>() {
                @Override
                public void onResponse(@NonNull Call<AdsCoverageList> call, @NonNull Response<AdsCoverageList> response) {
                    progressDialog.dismiss();
                    assert response.body() != null;

                    if(response.body().getStatus().equals("1")){

                        adsCoverageLists = response.body().getPlanList();
                        adapterAdsCoverage = new AdapterAdsCoverage(context,itemClickListener,adsCoverageLists);
                        recycler_ads.setHasFixedSize(true);
                        recycler_ads.setLayoutManager(new LinearLayoutManager(context));
                        recycler_ads.setAdapter(adapterAdsCoverage);

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

    public void getAdvertisment(){

        if(GlobalMethods.isNetworkAvailable(ActivityBuyAds.this)){

            Api.getClient().getAdsList(lattitude,longitude,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<AdvertismentModel>() {
                @Override
                public void onResponse(@NonNull Call<AdvertismentModel> call, @NonNull Response<AdvertismentModel> response) {

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){
                            Log.e("res",new Gson().toJson(response.body()));

//                            String random_image = response.body().getAdsList().getData().getImageUrl()+response.body().getAdsList().getData().getAdsImage();
//                            String web=response.body().getAdsList().getData().getExternalLink();
//                            Glide.with(context).load(random_image).into(bottom_add_image);
//
//                            bottom_add_image.setOnClickListener(view -> {
//
//                                Intent in=new Intent(context,ActivityAdsWebview.class);
//                                in.putExtra("link",web);
//                                startActivity(in);
//
//                            });

                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AdvertismentModel> call, @NonNull Throwable t) {
                    //  progressDialog.dismiss();


                }
            });

        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }
    private void changeLanguage() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            a1.setText(resources.getText(R.string.buy_ads));
            a2.setText(resources.getText(R.string.ads_management_panel));
            a3.setText(resources.getText(R.string.username_and_password_will_be_your_app));
            a4.setText(resources.getText(R.string.username_and_password));





        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();
            a1.setText(resources.getText(R.string.buy_ads));
            a2.setText(resources.getText(R.string.ads_management_panel));
            a3.setText(resources.getText(R.string.username_and_password_will_be_your_app));
            a4.setText(resources.getText(R.string.username_and_password));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            a1.setText(resources.getText(R.string.buy_ads));
            a2.setText(resources.getText(R.string.ads_management_panel));
            a3.setText(resources.getText(R.string.username_and_password_will_be_your_app));
            a4.setText(resources.getText(R.string.username_and_password));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            a1.setText(resources.getText(R.string.buy_ads));
            a2.setText(resources.getText(R.string.ads_management_panel));
            a3.setText(resources.getText(R.string.username_and_password_will_be_your_app));
            a4.setText(resources.getText(R.string.username_and_password));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            a1.setText(resources.getText(R.string.buy_ads));
            a2.setText(resources.getText(R.string.ads_management_panel));
            a3.setText(resources.getText(R.string.username_and_password_will_be_your_app));
            a4.setText(resources.getText(R.string.username_and_password));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            a1.setText(resources.getText(R.string.buy_ads));
            a2.setText(resources.getText(R.string.ads_management_panel));
            a3.setText(resources.getText(R.string.username_and_password_will_be_your_app));
            a4.setText(resources.getText(R.string.username_and_password));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            a1.setText(resources.getText(R.string.buy_ads));
            a2.setText(resources.getText(R.string.ads_management_panel));
            a3.setText(resources.getText(R.string.username_and_password_will_be_your_app));
            a4.setText(resources.getText(R.string.username_and_password));


        }
    }
    private void changeLanguagecon() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            cm1.setText(resources.getText(R.string.conformation));
            cm2.setText(resources.getText(R.string.one_select_the_plan_not_edting));
            dialogDone.setText(resources.getText(R.string.done));
            dialogClose.setText(resources.getText(R.string.close));





        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            cm1.setText(resources.getText(R.string.conformation));
            cm2.setText(resources.getText(R.string.one_select_the_plan_not_edting));
            dialogDone.setText(resources.getText(R.string.done));
            dialogClose.setText(resources.getText(R.string.close));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            cm1.setText(resources.getText(R.string.conformation));
            cm2.setText(resources.getText(R.string.one_select_the_plan_not_edting));
            dialogDone.setText(resources.getText(R.string.done));
            dialogClose.setText(resources.getText(R.string.close));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            cm1.setText(resources.getText(R.string.conformation));
            cm2.setText(resources.getText(R.string.one_select_the_plan_not_edting));
            dialogDone.setText(resources.getText(R.string.done));
            dialogClose.setText(resources.getText(R.string.close));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            cm1.setText(resources.getText(R.string.conformation));
            cm2.setText(resources.getText(R.string.one_select_the_plan_not_edting));
            dialogDone.setText(resources.getText(R.string.done));
            dialogClose.setText(resources.getText(R.string.close));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            cm1.setText(resources.getText(R.string.conformation));
            cm2.setText(resources.getText(R.string.one_select_the_plan_not_edting));
            dialogDone.setText(resources.getText(R.string.done));
            dialogClose.setText(resources.getText(R.string.close));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            cm1.setText(resources.getText(R.string.conformation));
            cm2.setText(resources.getText(R.string.one_select_the_plan_not_edting));
            dialogDone.setText(resources.getText(R.string.done));
            dialogClose.setText(resources.getText(R.string.close));


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
        Log.d("paymentstatus","status >>" +status);
      //  setAdsLocationOnOffApi("0", "1");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iapHelper != null)
            iapHelper.endConnection();
    }

}
