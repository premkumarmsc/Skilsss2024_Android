package com.bannet.skils.coupon.view;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
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
import com.bannet.skils.coupon.adapter.AdapterRewardScratchCard;
import com.bannet.skils.coupon.response.CheckPrmocodeResponse;
import com.bannet.skils.coupon.response.modelReward;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentCoupon extends Fragment {

    public View view;
    public Dialog dialogbox;
    public Context context;
    public RecyclerView list;
    public AdapterRewardScratchCard adapterRewardScratchCard;
    public AdapterRewardScratchCard.ItemClickLissener itemClickLissener;
    public String userID,cardScratch = "0";
    public TextView totalScratch;

    public RelativeLayout selectBg;
    public ImageView backBtn,infoBtn;
    public CoordinatorLayout coordinatorLayout;

    public ScratchCard scratchCard;
    public TextView dtitle,dtype,rate,expirdate,coupen_code;
    public CircleImageView logo;
    public CardView promo_layout;
    public ImageView qr_imae,copyBtn;

    public String promoID;
    int POSITION;

    String select = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_coupon, container, false);

        context = getActivity();

        onbackword();
        init(view);

        return view;

    }

    private void init(View view){

        userID = PrefConnect.readString(context,PrefConnect.USER_ID,"");
        Log.e("userID",userID);

        getprofile(userID);



        list = view.findViewById(R.id.rewards);
        totalScratch = view.findViewById(R.id.total_scratch);
        infoBtn = view.findViewById(R.id.info);
        coordinatorLayout = view.findViewById(R.id.cordlayout);
        selectBg = view.findViewById(R.id.select_bg);
        scratchCard = view.findViewById(R.id.scratchCard);
        backBtn = view.findViewById(R.id.back_btn);

        dtitle = view.findViewById(R.id.title);
        dtype = view.findViewById(R.id.type);
        rate = view.findViewById(R.id.rate);
        expirdate = view.findViewById(R.id.expirdate);
        logo = view.findViewById(R.id.logo);

        qr_imae = view.findViewById(R.id.qr_imae);
        promo_layout = view.findViewById(R.id.promo_layout);
        coupen_code = view.findViewById(R.id.coupen_code);
        copyBtn = view.findViewById(R.id.copyBtn);

        PromocodeList();

        itemClickLissener = (promoId, scratch, notScratch, position, type) -> {

            POSITION = position;
            promoID = promoId;
            if(type.equals("1")){

                checkPromocode(promoId,scratch,notScratch,position);

            }
            else {

                select = "1";
                coordinatorLayout.setVisibility(View.VISIBLE);
                selectBg.setVisibility(View.VISIBLE);

                dtitle.setText(adapterRewardScratchCard.list.get(position).getData().getTitle());
                String strDate = adapterRewardScratchCard.list.get(position).getData().getEndDate();
                //current date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date objDate = null;
                try {
                    objDate = dateFormat.parse(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Expected date format
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd, yyyy");

                String finalDate = dateFormat2.format(objDate);
                expirdate.setText(finalDate);

                if(adapterRewardScratchCard.list.get(position).getData().getType().equals("1")){

                    dtype.setText("Flat");
                    rate.setText(adapterRewardScratchCard.list.get(position).getData().getAmountFlat() + " RS");

                }
                else if(adapterRewardScratchCard.list.get(position).getData().getType().equals("2")){

                    dtype.setText("Discount");
                    rate.setText(adapterRewardScratchCard.list.get(position).getData().getAmountDiscount() + " RS");

                }
                else {

                    dtype.setText("Discount");
                    rate.setText(adapterRewardScratchCard.list.get(position).getData().getAmountDiscount() + " RS");

                }

                if(adapterRewardScratchCard.list.get(position).getData().getQrImage().isEmpty()){

                    qr_imae.setVisibility(View.GONE);
                    promo_layout.setVisibility(View.VISIBLE);
                    coupen_code.setText(adapterRewardScratchCard.list.get(position).getData().getPromoCode());

                }
                else {

                    qr_imae.setVisibility(View.VISIBLE);
                    promo_layout.setVisibility(View.GONE);
                    Glide.with(context).load(EndPoint.ImageUrl + adapterRewardScratchCard.list.get(position).getData().getQrImage()).into(qr_imae);

                }

                copyBtn.setOnClickListener(view1 -> {

                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(adapterRewardScratchCard.list.get(position).getData().getPromoCode());
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                });

                Glide.with(context).load(EndPoint.ImageUrl + adapterRewardScratchCard.list.get(position).getData().getLogo()).into(logo);

                if(adapterRewardScratchCard.list.get(position).getData().getScratchStatus().equals("0")){

                    scratchCard.setVisibility(View.VISIBLE);

                }
                else {

                    scratchCard.setVisibility(View.GONE);

                }

            }

        };

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coordinatorLayout.setVisibility(View.GONE);
                selectBg.setVisibility(View.GONE);
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Details(POSITION);
            }
        });

        selectBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coordinatorLayout.setVisibility(View.GONE);
                selectBg.setVisibility(View.GONE);
            }
        });
        handleListeners();

    }

    private void scratch() {

        scratchCard.setVisibility(View.INVISIBLE);
        usedPromocode();

    }

    private void handleListeners() {
        scratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {
            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                if (visiblePercent > 0.8) {
                    scratch();
                }
            }
        });
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
                        PromocodeList();
                        getprofile(userID);


                    }else{

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

    private void PromocodeList() {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().promocodeList(
                    PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""),
                    PrefConnect.readString(context,PrefConnect.LATITUDE,""),
                    PrefConnect.readString(context,PrefConnect.LOGITUDE,"")).enqueue(new Callback<modelReward>() {
                @Override
                public void onResponse(@NonNull Call<modelReward> call, @NonNull Response<modelReward> response) {

                    Log.e("promocodeList",new Gson().toJson(response.body()));
                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        adapterRewardScratchCard=new AdapterRewardScratchCard(context,response.body().getUsedlist(),itemClickLissener);
                        GridLayoutManager layoutManager=new GridLayoutManager(context,2);
                        list.setLayoutManager(layoutManager);
                        list.setAdapter(adapterRewardScratchCard);

                    }else {

                        GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<modelReward> call, @NonNull Throwable t) {

                    Log.e("failure professional res",t.getMessage());
                }
            });
        }

        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    private void Details(int position) {

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
        TextView dLink = dialogbox.findViewById(R.id.dLink);
        TextView dpromocode = dialogbox.findViewById(R.id.coupen_code);
        TextView dpromocodetitle = dialogbox.findViewById(R.id.code_title);
        CardView dcopyBtn = dialogbox.findViewById(R.id.copyBtn);
        CardView dpromoLayout = dialogbox.findViewById(R.id.promo_layout);
        AppCompatButton btn_redeem = dialogbox.findViewById(R.id.btn_redeem);

        dTitle.setText(adapterRewardScratchCard.list.get(position).getData().getTitle());
        String strDate = adapterRewardScratchCard.list.get(position).getData().getEndDate();
        //current date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date objDate = null;
        try {
            objDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Expected date format
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd, yyyy");

        String finalDate = dateFormat2.format(objDate);
        dExpireDate.setText(finalDate);
        ddetails.setText(HtmlCompat.fromHtml(adapterRewardScratchCard.list.get(position).getData().getOffDetails(), 0));
        dLink.setText(HtmlCompat.fromHtml(adapterRewardScratchCard.list.get(position).getData().getLink(), 0));


        if(adapterRewardScratchCard.list.get(position).getData().getQrImage().isEmpty()){


            dpromoLayout.setVisibility(View.VISIBLE);
            dpromocodetitle.setVisibility(View.VISIBLE);
            dpromocode.setText(adapterRewardScratchCard.list.get(position).getData().getPromoCode());


        }
        else {

            dpromocodetitle.setVisibility(View.GONE);
            dpromoLayout.setVisibility(View.GONE);


        }

        dcopyBtn.setOnClickListener(view -> {

            ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(adapterRewardScratchCard.list.get(position).getData().getPromoCode());
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

        });

        String url = adapterRewardScratchCard.list.get(position).getData().getLink();

        btn_redeem.setOnClickListener(v -> {

            if(!url.equals("")){

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });

        Glide.with(context).load(EndPoint.ImageUrl + adapterRewardScratchCard.list.get(position).getData().getLogo()).into(dlogo);





    }

    private void checkPromocode(String promoId, RelativeLayout scratch, RelativeLayout notScratch,int position) {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().checkpromocode(userID,promoId,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CheckPrmocodeResponse>() {
                @Override
                public void onResponse(@NonNull Call<CheckPrmocodeResponse> call, @NonNull Response<CheckPrmocodeResponse> response) {

                    Log.e("promocodeList",new Gson().toJson(response.body()));

                    assert response.body() != null;
                    if(response.body().getStatus() == 1){

                        Log.e("scratch","scratch");
                        scratch.setVisibility(View.VISIBLE);
                        notScratch.setVisibility(View.GONE);
                        adapterRewardScratchCard.list.get(position).getData().setScratchStatus("1");


                    }else{

                        adapterRewardScratchCard.list.get(position).getData().setScratchStatus("0");
                        notScratch.setVisibility(View.VISIBLE);
                        scratch.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CheckPrmocodeResponse> call, @NonNull Throwable t) {

                    Log.e("failure professional res",t.getMessage());
                }
            });
        }

        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    private void getprofile(String user_id) {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().getprofile(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<getprofileModel>() {
                @Override
                public void onResponse(@NonNull Call<getprofileModel> call, @NonNull Response<getprofileModel> response) {

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            totalScratch.setText(response.body().getDetails().getUsedpromocodes());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<getprofileModel> call, @NonNull Throwable t) {
                    //  progressDialog.dismiss();

                    Log.e("failure responce",t.getMessage());
                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }

    private void onbackword(){

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                if(select.equals("1")){

                    coordinatorLayout.setVisibility(View.GONE);
                    selectBg.setVisibility(View.GONE);

                }


            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback((LifecycleOwner) context, callback);

    }

}