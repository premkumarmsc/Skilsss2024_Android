package com.bannet.skils.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bannet.skils.Adapter.AdapterCertificates;
import com.bannet.skils.profile.responce.CertificateList;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddCertificatins extends AppCompatActivity {

    Context context;
    ImageView img_back;
    RelativeLayout submit;
    AdapterCertificates adapterCertificates;
    AdapterCertificates.ItemClickListener itemClickListener;
    List<CertificateList.Certificate> certificateLists;
    ProgressDialog progressDialog;
    RecyclerView certificate_recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_certificate);
        getSupportActionBar().hide();
        context= ActivityAddCertificatins.this;

        init();

    }
    public  void init(){

        submit=findViewById(R.id.certification_sumite_btn);
        certificate_recycler = findViewById(R.id.certificate_recycler);
        img_back=findViewById(com.bannet.skils.R.id.img_back);

        img_back.setOnClickListener(view -> finish());

        callCertificates();

        try{

            itemClickListener = id -> PrefConnect.writeString(context,PrefConnect.USER_SELECTED_CERTIFICATE,id);

        }
        catch (Exception e){

            e.printStackTrace();

        }


    }

    public void callCertificates(){
        if (GlobalMethods.isNetworkAvailable(context)) {

            certificateLists = new ArrayList<>();
            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            Api.getClient().getCertificate(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CertificateList>() {

                @Override
                public void onResponse(@NonNull Call<CertificateList> call, @NonNull Response<CertificateList> response) {

                     progressDialog.dismiss();
                     if(response.isSuccessful()){
                         assert response.body() != null;
                         if(response.body().getStatus().equals("1")){

                             certificateLists = response.body().getCertificateList();
                             adapterCertificates = new AdapterCertificates(context,itemClickListener,certificateLists);
                             certificate_recycler.setHasFixedSize(true);
                             certificate_recycler.setLayoutManager(new LinearLayoutManager(context));
                             certificate_recycler.setAdapter(adapterCertificates);


                         }else {

                             GlobalMethods.Toast(context,response.body().getMessage());

                         }
                     }
                     else {

                         GlobalMethods.Toast(context,GlobalMethods.getString(context,R.string.please_try_again_later));

                     }
                }

                @Override
                public void onFailure(@NonNull Call<CertificateList> call, @NonNull Throwable t) {

                    progressDialog.dismiss();

                }
            });

        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }
}