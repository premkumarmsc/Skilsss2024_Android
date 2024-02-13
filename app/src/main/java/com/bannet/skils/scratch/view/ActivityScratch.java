package com.bannet.skils.scratch.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.text.HtmlCompat;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.service.scratch.ScratchCard;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityScratch extends AppCompatActivity {

    public Context context;
    private ScratchCard mScratchCard;
    public Dialog dialogbox;
    public String status,qrimage,promocode,expireDate,title,discription,type,discount,flat,logo,promoID;

    public TextView tpromocode,texpireDate,tDiscount,tflat,tType,tTitle,dDetails,dtitle,ddate;
    public CircleImageView imageLogo;
    public ImageView imageQr,copyBtn,infoBtn,backBtn;
    public CardView promoLayout;
    public String finalDate,dLink;
    public AppCompatButton redeemBtn;

    public String userID;
    public CircleImageView dlogo;
    LinearLayout dipromo_layout;
    CoordinatorLayout appBarLayout;
    public RelativeLayout expiredStatus,expireLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);

        context = ActivityScratch.this;

        if(getSupportActionBar() != null){

            getSupportActionBar().hide();

        }

        init();

    }

    private void init(){
//

        mScratchCard = findViewById(R.id.scratchCard);



        userID = PrefConnect.readString(context,PrefConnect.USER_ID,"");

        tType = findViewById(R.id.type);
        tpromocode = findViewById(R.id.coupen_code);
        texpireDate = findViewById(R.id.expirdate);
        imageQr = findViewById(R.id.qr_imae);
        imageLogo = findViewById(R.id.logo);
        tDiscount = findViewById(R.id.rate);
        copyBtn = findViewById(R.id.copyBtn);
        promoLayout = findViewById(R.id.promo_layout);
        tTitle = findViewById(R.id.title);
        infoBtn = findViewById(R.id.info);
        dDetails = findViewById(R.id.details);
        dlogo = findViewById(R.id.dlogo);
        dtitle = findViewById(R.id.dtitle);
        ddate = findViewById(R.id.ddate);
        dipromo_layout = findViewById(R.id.dipromo_layout);
        expireLayout = findViewById(R.id.expired);
        expiredStatus = findViewById(R.id.expired_status);
        redeemBtn = findViewById(R.id.btn_redeem);
        backBtn = findViewById(R.id.bach_btn);

        promoID = getIntent().getStringExtra("promoid");
        status = getIntent().getStringExtra("status");
        qrimage = getIntent().getStringExtra("qr");
        logo = getIntent().getStringExtra("logo");
        expireDate = getIntent().getStringExtra("expire");
        promocode = getIntent().getStringExtra("promocode");
        type = getIntent().getStringExtra("type");
        discount = getIntent().getStringExtra("discount");
        flat = getIntent().getStringExtra("flat");
        title = getIntent().getStringExtra("title");
        discription = getIntent().getStringExtra("dis");
        String expirdstatus = getIntent().getStringExtra("e_status");
        dLink = getIntent().getStringExtra("link");

        Glide.with(context).load(EndPoint.ImageUrl + logo).into(imageLogo);
        Glide.with(context).load(EndPoint.ImageUrl + logo).into(dlogo);
        tTitle.setText(title);
        dtitle.setText(title);

        if(expirdstatus.equals("2")){

            expiredStatus.setVisibility(View.VISIBLE);
            expireLayout.setVisibility(View.VISIBLE);
        }
        else {

            expiredStatus.setVisibility(View.GONE);
            expireLayout.setVisibility(View.GONE);

        }

        dDetails.setText(HtmlCompat.fromHtml(discription, 0));

        String strDate = expireDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date objDate = null;
        try {
            objDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd, yyyy");

        finalDate = dateFormat2.format(objDate);

        texpireDate.setText(finalDate);
        ddate.setText(finalDate);

        if(status.equals("1")){

            dipromo_layout.setVisibility(View.VISIBLE);
            mScratchCard.setVisibility(View.GONE);

        }
        else {

            dipromo_layout.setVisibility(View.GONE);
            mScratchCard.setVisibility(View.VISIBLE);

        }

        if(type.equals("1")){

            tType.setText("Flat");
            tDiscount.setText("RS "+flat );

        }
        else if(type.equals("2")){

            tType.setText("Discount");
            tDiscount.setText(discount + " ");

        }
        else {

            tType.setText("Discount");
            tDiscount.setText(discount + " ");

        }

        if(qrimage.isEmpty()){

            promoLayout.setVisibility(View.VISIBLE);
            tpromocode.setVisibility(View.VISIBLE);
            tpromocode.setText(promocode);
            imageQr.setVisibility(View.GONE);

        }
        else {

            promoLayout.setVisibility(View.GONE);
            imageQr.setVisibility(View.VISIBLE);
            tpromocode.setVisibility(View.GONE);
            Glide.with(context).load(EndPoint.ImageUrl + qrimage).into(imageQr);

        }

        copyBtn.setOnClickListener(view -> {

            ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(promocode);
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

        });

        backBtn.setOnClickListener(view -> finish());

        //infoBtn.setOnClickListener(view -> Details());

       // Details();

        redeemBtn.setOnClickListener(v -> {

            String url = dLink;

            if(!url.equals("")){

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });

        handleListeners();

    }

    private void scratch() {

        mScratchCard.setVisibility(View.INVISIBLE);
        usedPromocode();

    }

    private void handleListeners() {
        mScratchCard.setOnScratchListener((scratchCard, visiblePercent) -> {

            if (visiblePercent > 0.8) {
                scratch();
            }

        });
    }

    private void Details() {

        dialogbox = new Dialog(context);
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.bottom_scratch_layout);
        dialogbox.show();
        dialogbox.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.getWindow().getAttributes().windowAnimations=R.style.DialoAnimasion;
        dialogbox.getWindow().setGravity(Gravity.BOTTOM);

        TextView dTitle = dialogbox.findViewById(R.id.title);
        CircleImageView dlogo = dialogbox.findViewById(R.id.logo);
        TextView dExpireDate = dialogbox.findViewById(R.id.date);
        TextView ddetails = dialogbox.findViewById(R.id.details);
        TextView dpromocode = dialogbox.findViewById(R.id.coupen_code);
        TextView dpromocodetitle = dialogbox.findViewById(R.id.code_title);
        CardView dcopyBtn = dialogbox.findViewById(R.id.copyBtn);
        CardView dpromoLayout = dialogbox.findViewById(R.id.promo_layout);
        AppCompatButton btn_redeem = dialogbox.findViewById(R.id.btn_redeem);

        dTitle.setText(title);
        dExpireDate.setText(finalDate);
        ddetails.setText(HtmlCompat.fromHtml(discription, 0));


        if(qrimage.isEmpty()){


            dpromoLayout.setVisibility(View.VISIBLE);
            dpromocodetitle.setVisibility(View.VISIBLE);
            dpromocode.setText(promocode);


        }
        else {

            dpromocodetitle.setVisibility(View.GONE);
            dpromoLayout.setVisibility(View.GONE);


        }

        dcopyBtn.setOnClickListener(view -> {

            ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(promocode);
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

        });
        String url = dLink;

        btn_redeem.setOnClickListener(v -> {

            if(!url.equals("")){

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });


        Glide.with(context).load(EndPoint.ImageUrl + logo).into(dlogo);



    }

    private void usedPromocode() {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().promocodeused(userID,promoID, PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                    Log.e("promocodeList",new Gson().toJson(response.body()));

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        GlobalMethods.Toast(context,response.body().getMessage());

                    }
                    else{

                        GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {

                    Log.e("failure professional res",t.getMessage());
                }
            });
        }

        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }


}