package com.bannet.skils.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bannet.skils.chat.activity.ActivityChatDetails;
import com.bannet.skils.postingdetails.activity.ActivityPostingsDetailsScreen;
import com.bannet.skils.favposting.responce.FavpostingsListModel;
import com.bannet.skils.R;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;

import java.util.List;


public class AdapterMySavedPostingList extends RecyclerView.Adapter<AdapterMySavedPostingList.MyViewholder> {

    Context context;
    ItemClickListener itemClickListener;
    List<FavpostingsListModel.Posting> nameModels;
    String  USER_ID,post_id;
    String images_list;
    Resources resources;

    public interface ItemClickListener{
         void ItemClick(FavpostingsListModel.Posting post_id, ImageView img_fav, View itemview);
    }

    public AdapterMySavedPostingList(Context context, ItemClickListener itemClickListener, List<FavpostingsListModel.Posting> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_saved_postings_list_layout,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

//        Glide.with(context).load(nameModels.get(position).getImage()).into(holder.img_profession);
        holder.txt_name.setText(nameModels.get(position).getTitle());
        holder.txt_skills.setText(nameModels.get(position).getSkillsName());
        holder.txt_desctiption.setText(nameModels.get(position).getDescription());

        images_list = nameModels.get(position).getPostImages();
        if(images_list.isEmpty())
        {
            holder.img_profession.setImageResource(R.drawable.select_skil_image);
        }
        else{
            String[] str = images_list.split(",");
            // Iterating over the string
            for (int i = 0; i < str.length; i++) {
                // Printing the elements of String array
                Glide.with(context).load(nameModels.get(position).getPostImageUrl() + str[0]).error(R.drawable.select_skil_image).into(holder.img_profession);

            }

        }
        changeLanguagedialog(holder.skils);
        USER_ID = PrefConnect.readString(context, PrefConnect.USER_ID, "");
        post_id = nameModels.get(position).getPostId();

        holder.itemView.setOnClickListener(v -> {

            Intent in = new Intent(context, ActivityPostingsDetailsScreen.class);
            in.putExtra("post_id", nameModels.get(holder.getAdapterPosition()).getPostId());
            in.putExtra("post_user_id", nameModels.get(holder.getAdapterPosition()).getUserId());
            in.putExtra("notification_id", "");
            in.putExtra("type", "2");
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);

//            Log.e("opp_jde",nameModels.get(position).getUserId());
//            Intent in = new Intent(context, ActivityPostingsDetailsScreen.class);
//            in.putExtra("post_id", nameModels.get(position).getPostId());
//            in.putExtra("notification_id","");
//            in.putExtra("type","2");
//            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(in);

        });

        holder.img_chat_postings_new.setOnClickListener(view -> {

            if(!USER_ID.equals(nameModels.get(holder.getAdapterPosition()).getUserId())){
                Intent in = new Intent(context, ActivityChatDetails.class);
                in.putExtra("other_user_id", nameModels.get(position).getUserId());
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);

            }

        });

        holder.img_fav.setOnClickListener(view -> itemClickListener.ItemClick(nameModels.get(holder.getAdapterPosition()),holder.img_fav,holder.itemView));

    }

    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {

        ImageView img_profession;
        TextView txt_name;
        TextView txt_skills,skils;
        ImageView img_chat_postings_new;
        TextView txt_desctiption;
        ImageView img_fav;

        public MyViewholder(View itemView) {
            super(itemView);

            skils=itemView.findViewById(R.id.skils);
            txt_desctiption=itemView.findViewById(R.id.txt_desctiption);
            img_profession = (ImageView) itemView.findViewById(R.id.img_profession);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_skills = (TextView) itemView.findViewById(R.id.txt_skills);
            img_chat_postings_new = (ImageView) itemView.findViewById(R.id.img_chat_postings_new);
            img_fav = (ImageView) itemView.findViewById(R.id.img_fav);
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