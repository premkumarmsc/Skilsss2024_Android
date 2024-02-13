package com.bannet.skils.coupon.adapter;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bannet.skils.R;
import com.bannet.skils.service.EndPoint;
import com.bannet.skils.coupon.response.modelReward;
import com.bannet.skils.scratch.view.ActivityScratch;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRewardScratchCard extends RecyclerView.Adapter<AdapterRewardScratchCard.MyViewHolder> {

    public View view;
    public Dialog dialogbox;
    public Context context;
    public List<modelReward.Used> list;
    public AdapterRewardScratchCard.ItemClickLissener itemClickLissener;
    public String finalDate;
    public int position;

    public AdapterRewardScratchCard(Context context, List<modelReward.Used> list, ItemClickLissener itemClickLissener) {
        this.context = context;
        this.list = list;
        this.itemClickLissener = itemClickLissener;
    }

    public interface ItemClickLissener{

        void itemclick(String promoId, RelativeLayout scratch,RelativeLayout notScratch,int position,String type);

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reward,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        itemClickLissener.itemclick(list.get(holder.getAdapterPosition()).getId(),holder.scratch,holder.notScratch,holder.getAdapterPosition(),"1");

        Glide.with(context).load(EndPoint.ImageUrl + list.get(holder.getAdapterPosition()).getData().getLogo()).into(holder.logo);

        if(list.get(holder.getAdapterPosition()).getData().getStatus().equals("2")){

            holder.expiedLayout.setVisibility(View.VISIBLE);
            holder.expiredStatus.setVisibility(View.VISIBLE);

        } else if (list.get(holder.getAdapterPosition()).getData().getStatus().equals("3")) {

            holder.offersCompletedStatusLayout.setVisibility(View.VISIBLE);
            holder.offersCompletedStatus.setVisibility(View.VISIBLE);
            
        } else {

            holder.expiedLayout.setVisibility(View.GONE);
            holder.expiredStatus.setVisibility(View.GONE);
            holder.offersCompletedStatusLayout.setVisibility(View.GONE);
            holder.offersCompletedStatus.setVisibility(View.GONE);

        }

        if(list.get(holder.getAdapterPosition()).getData().getType().equals("1")){

            holder.type.setText("Flat");
            holder.discount.setText(list.get(holder.getAdapterPosition()).getData().getAmountFlat() + " ");

        }
        else if(list.get(holder.getAdapterPosition()).getData().getType().equals("2")){

            holder.type.setText("Discount");
            holder.discount.setText(list.get(holder.getAdapterPosition()).getData().getAmountDiscount() + " ");

        }
        else {

            holder.type.setText("Discount");
            holder.discount.setText(list.get(holder.getAdapterPosition()).getData().getAmountDiscount() + " ");

        }

        if(list.get(holder.getAdapterPosition()).getData().getQrImage().isEmpty()){



            holder.coupenCode.setVisibility(View.VISIBLE);
            holder.coupenCode.setText(list.get(holder.getAdapterPosition()).getData().getPromoCode());
            holder.qrImage.setVisibility(View.GONE);

        }
        else {

            holder.qrImage.setVisibility(View.VISIBLE);
            holder.coupenCode.setVisibility(View.GONE);
            Glide.with(context).load(EndPoint.ImageUrl + list.get(holder.getAdapterPosition()).getData().getQrImage()).into(holder.qrImage);

        }

        String strDate = list.get(holder.getAdapterPosition()).getData().getEndDate();
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

        finalDate = dateFormat2.format(objDate);

        holder.exprireDate.setText(finalDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // itemClickLissener.itemclick(list.get(holder.getAdapterPosition()).getId(),holder.scratch,holder.notScratch,holder.getAdapterPosition(),"2");
                Intent intent = new Intent(context, ActivityScratch.class);
                intent.putExtra("promoid",list.get(holder.getAdapterPosition()).getId());
                intent.putExtra("status",list.get(holder.getAdapterPosition()).getData().getScratchStatus());
                intent.putExtra("qr",list.get(holder.getAdapterPosition()).getData().getQrImage());
                intent.putExtra("logo",list.get(holder.getAdapterPosition()).getData().getLogo());
                intent.putExtra("expire",list.get(holder.getAdapterPosition()).getData().getEndDate());
                intent.putExtra("promocode",list.get(holder.getAdapterPosition()).getData().getPromoCode());
                intent.putExtra("type",list.get(holder.getAdapterPosition()).getData().getType());
                intent.putExtra("discount",list.get(holder.getAdapterPosition()).getData().getAmountDiscount());
                intent.putExtra("flat",list.get(holder.getAdapterPosition()).getData().getAmountFlat());
                intent.putExtra("title",list.get(holder.getAdapterPosition()).getData().getTitle());
                intent.putExtra("dis",list.get(holder.getAdapterPosition()).getData().getOffDetails());
                intent.putExtra("e_status",list.get(holder.getAdapterPosition()).getData().getStatus());
                intent.putExtra("link",list.get(holder.getAdapterPosition()).getData().getLink());
                context.startActivity(intent);
            }
        });

        holder.infoBtn.setOnClickListener(view -> Details(list.get(holder.getAdapterPosition())));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView qrImage,infoBtn;
        public CircleImageView logo;
        public RelativeLayout notScratch;
        public RelativeLayout scratch,expiredStatus,expiedLayout,offersCompletedStatusLayout,offersCompletedStatus;
        public TextView type,exprireDate,discount,coupenCode;
        public AppCompatButton btn_redeem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            notScratch = itemView.findViewById(R.id.notscratch);
            scratch = itemView.findViewById(R.id.scratch);
            type = itemView.findViewById(R.id.type);
            exprireDate = itemView.findViewById(R.id.expirdate);
            discount = itemView.findViewById(R.id.rate);
            qrImage = itemView.findViewById(R.id.qr_imae);
            coupenCode = itemView.findViewById(R.id.coupen_code);
            logo = itemView.findViewById(R.id.logo);
            infoBtn = itemView.findViewById(R.id.info);
            expiredStatus = itemView.findViewById(R.id.expired_status);
            expiedLayout = itemView.findViewById(R.id.expired);
            offersCompletedStatus = itemView.findViewById(R.id.offersCompletedStatus);
            offersCompletedStatusLayout = itemView.findViewById(R.id.completed);

        }
    }

    private void Details(modelReward.Used used) {

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

        dTitle.setText(used.getData().getTitle());
        dExpireDate.setText(finalDate);
        ddetails.setText(HtmlCompat.fromHtml(used.getData().getOffDetails(), 0));


        if(used.getData().getQrImage().isEmpty()){


            dpromoLayout.setVisibility(View.VISIBLE);
            dpromocodetitle.setVisibility(View.VISIBLE);
            dpromocode.setText(used.getData().getPromoCode());


        }
        else {

            dpromocodetitle.setVisibility(View.GONE);
            dpromoLayout.setVisibility(View.GONE);


        }

        dcopyBtn.setOnClickListener(view -> {

            ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(used.getData().getPromoCode());
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

        });
        String url = list.get(position).getData().getLink();

        btn_redeem.setOnClickListener(v -> {

            if(!url.equals("")){

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });


        Glide.with(context).load(EndPoint.ImageUrl + used.getData().getLogo()).into(dlogo);

    }

}
