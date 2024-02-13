package com.bannet.skils.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.professinoldetails.activity.ActivityProffDetails;
import com.bannet.skils.professinoldetails.activity.ActivityWithoutimage;
import com.bumptech.glide.Glide;
import com.bannet.skils.favorite.responce.FavouriteListModel;
import com.bannet.skils.R;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;

import java.util.List;


public class AdapterFavouriteList extends RecyclerView.Adapter<AdapterFavouriteList.MyViewholder> {

    Context context;
    ItemClickListener itemClickListener;
    List<FavouriteListModel.Posting> nameModels;
    String USER_ID;
    Resources resources;

    public interface ItemClickListener{
        public void ItemClick(String position,View layout);
    }

    public AdapterFavouriteList(Context context, ItemClickListener itemClickListener, List<FavouriteListModel.Posting> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_fav_layout,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        changeLanguagedialog(holder.skills);

        Glide.with(context).load(nameModels.get(holder.getAdapterPosition()).getImageUrl()+nameModels.get(holder.getAdapterPosition()).getImageName()).error(R.drawable.profile_image).into(holder.img_profession);
        holder.txt_name.setText(nameModels.get(holder.getAdapterPosition()).getFirstName()+" "+nameModels.get(holder.getAdapterPosition()).getLastName());
        holder.txt_skills.setText(nameModels.get(holder.getAdapterPosition()).getSkillsName());
        holder.ratingBar.setRating(Float.parseFloat(nameModels.get(holder.getAdapterPosition()).getRatings()));

        USER_ID = PrefConnect.readString(context,PrefConnect.USER_ID,"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameModels.get(holder.getAdapterPosition()).getBannerImage().equals("") || nameModels.get(holder.getAdapterPosition()).getBannerImage().isEmpty()){

                    Intent in = new Intent(context, ActivityWithoutimage.class);
                    in.putExtra("profe_id",nameModels.get(holder.getAdapterPosition()).getProfId());
                    in.putExtra("profileurl",nameModels.get(holder.getAdapterPosition()).getImageUrl());
                    in.putExtra("profile_name",nameModels.get(holder.getAdapterPosition()).getImageName());
                    in.putExtra("profileurl",nameModels.get(holder.getAdapterPosition()).getImageUrl());
                    in.putExtra("profile_name",nameModels.get(holder.getAdapterPosition()).getImageName());
                    in.putExtra("latitude",nameModels.get(holder.getAdapterPosition()).getLatitude());
                    in.putExtra("logitude",nameModels.get(holder.getAdapterPosition()).getLongitude());
                    in.putExtra("type","2");
                    in.putExtra("typehire","add");
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }
                else {

                    Intent in = new Intent(context, ActivityProffDetails.class);
                    in.putExtra("profe_id",nameModels.get(holder.getAdapterPosition()).getProfId());
                    in.putExtra("profileurl",nameModels.get(holder.getAdapterPosition()).getImageUrl());
                    in.putExtra("profile_name",nameModels.get(holder.getAdapterPosition()).getImageName());
                    in.putExtra("latitude",nameModels.get(holder.getAdapterPosition()).getLatitude());
                    in.putExtra("logitude",nameModels.get(holder.getAdapterPosition()).getLongitude());
                    in.putExtra("type","2");
                    in.putExtra("typehire","add");
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }

            }
        });

//        holder.img_chat_detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(USER_ID.equals(nameModels.get(holder.getAdapterPosition()).getProfId())){
//
//                } else{
//                    Intent in = new Intent(context, ActivityChatDetails.class);
//                    in.putExtra("other_user_id",nameModels.get(position).getProfId());
//                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(in);
//                }
//
//            }
//        });

        holder.img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.ItemClick(nameModels.get(position).getProfId(),holder.itemView);
            }
        });
    }


    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        ImageView img_profession;
        TextView txt_name,skills;
        TextView txt_skills;
        ImageView img_fav;
        RatingBar ratingBar;

        public MyViewholder(View itemView) {
            super(itemView);
            img_profession = (ImageView) itemView.findViewById(R.id.img_profession);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            skills = itemView.findViewById(R.id.skills);
            txt_skills = (TextView) itemView.findViewById(R.id.txt_skills);
            img_fav = (ImageView) itemView.findViewById(R.id.img_fav);
            ratingBar = itemView.findViewById(R.id.post_rating);
        }
    }
    private void changeLanguagedialog(TextView skill) {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();


            skill.setText(resources.getText(R.string.skills));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));

        }
    }
}