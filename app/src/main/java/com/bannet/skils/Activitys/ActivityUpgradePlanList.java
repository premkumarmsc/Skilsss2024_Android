package com.bannet.skils.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.billingclient.api.SkuDetails;
import com.bannet.skils.R;
import com.bannet.skils.service.IAPHelper;
import com.google.gson.Gson;
import com.bannet.skils.Adapter.AdapterUpgradePlanStatus;
import com.bannet.skils.Model.PlanStatusModel;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUpgradePlanList extends AppCompatActivity {


    Context context;
    ImageView back_btn;

    RecyclerView planStatus;
    ProgressDialog progressDialog;
    String  USER_ID;

    AdapterUpgradePlanStatus.ItemClickListener itemClickListener;
    AdapterUpgradePlanStatus adapterUpgradePlanStatus;

    TextView mp1;
    Resources resources;

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
        setContentView(R.layout.activity_upgrade_plan_list);
        getSupportActionBar().hide();
        context = ActivityUpgradePlanList.this;
        //iapHelper = new IAPHelper(this, this, skuList);

        back_btn=findViewById(R.id.img_back);
        planStatus=findViewById(R.id.plan_status);
        mp1=findViewById(R.id.mp1);

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Userplanlist(USER_ID);

        changeLanguage();

    }

    private void Userplanlist(String user_id) {

        if(GlobalMethods.isNetworkAvailable(ActivityUpgradePlanList.this)){
            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            Api.getClient().planStatusList(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<PlanStatusModel>() {
                @Override
                public void onResponse(@NonNull Call<PlanStatusModel> call, @NonNull Response<PlanStatusModel> response) {
                    progressDialog.dismiss();

                    if(response.body().getStatus().equals("1")){

                        adapterUpgradePlanStatus = new AdapterUpgradePlanStatus(context,itemClickListener,response.body().getChatList());
                        planStatus.setHasFixedSize(true);
                        planStatus.setLayoutManager(new LinearLayoutManager(context));
                        planStatus.setAdapter(adapterUpgradePlanStatus);


                    }else {

                        GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlanStatusModel> call, @NonNull Throwable t) {

                    progressDialog.dismiss();

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

            mp1.setText(resources.getText(R.string.plan_status));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.plan_status));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.plan_status));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.plan_status));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.plan_status));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.plan_status));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            mp1.setText(resources.getText(R.string.plan_status));

        }
    }


   /* private void launch(String sku){
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
    }*/

}