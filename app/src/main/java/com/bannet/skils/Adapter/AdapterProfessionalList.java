package com.bannet.skils.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bannet.skils.chat.activity.ActivityChatDetails;
import com.bannet.skils.professinoldetails.activity.ActivityViewProfessionalDetails;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import com.bannet.skils.R;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;

import java.util.ArrayList;
import java.util.List;


public class AdapterProfessionalList extends RecyclerView.Adapter<AdapterProfessionalList.MyViewholder>  implements Filterable {

    Context context;
    ItemClickListener itemClickListener;
    List<ProfessionalList.Professional> nameModels;
    List<ProfessionalList.Professional> Filterproffesional = new ArrayList<>();
    String images_list;
    String USER_ID,simage_url,simageName;

    Resources resources;

    public interface ItemClickListener{
        public void ItemClick(int position);
    }

    public AdapterProfessionalList(Context context, ItemClickListener itemClickListener, List<ProfessionalList.Professional> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
        this.Filterproffesional=nameModels;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_layout,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        changeLanguagedialog(holder.skill);


        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");

        if(Filterproffesional.get(holder.getAdapterPosition()).getImageApprove().equals("1")){
            simage_url = Filterproffesional.get(holder.getAdapterPosition()).getImageUrl();
            simageName = Filterproffesional.get(holder.getAdapterPosition()).getImageName();

            Glide.with(context).load(simage_url+simageName).error(R.drawable.profile_image).into(holder.img_profession);

        }
        else {

            holder.img_profession.setImageResource(R.drawable.profile_image);
        }


        holder.txt_name.setText(Filterproffesional.get(holder.getAdapterPosition()).getFirstName()+" " +Filterproffesional.get(holder.getAdapterPosition()).getLastName());
        holder.txt_skills.setText(Filterproffesional.get(holder.getAdapterPosition()).getSkillsName());
        holder.ratingbar.setRating(Float.parseFloat(Filterproffesional.get(holder.getAdapterPosition()).getRatings()));
       // holder.user_time.setText(Filterproffesional.get(holder.getAdapterPosition()).getAvailableFrom()+"-"+Filterproffesional.get(holder.getAdapterPosition()).getAvailableTo());


        holder.linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, ActivityViewProfessionalDetails.class);
                in.putExtra("profe_id",Filterproffesional.get(holder.getAdapterPosition()).getId());
                in.putExtra("profileurl",Filterproffesional.get(holder.getAdapterPosition()).getImageUrl());
                in.putExtra("profile_name",Filterproffesional.get(holder.getAdapterPosition()).getImageName());
                in.putExtra("type","1");
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }
        });

        holder.img_chat_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(USER_ID.equals(nameModels.get(holder.getAdapterPosition()).getId())){

                } else{
                    Intent in = new Intent(context, ActivityChatDetails.class);
                    in.putExtra("other_user_id",nameModels.get(holder.getAdapterPosition()).getId());
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return Filterproffesional.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    Filterproffesional = nameModels;
                } else {
                    List<ProfessionalList.Professional> filteredList = new ArrayList<>();
                    for (ProfessionalList.Professional row : nameModels) {

                        if (row.getSkillsName().toLowerCase().contains(charString.toLowerCase()) || row.getSkillsName().contains(charSequence) ||
                             row.getFirstName().toLowerCase().contains(charString.toLowerCase()) || row.getFirstName().contains(charSequence) ||
                             row.getLastName().toLowerCase().contains(charString.toLowerCase())  || row.getLastName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    Filterproffesional = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = Filterproffesional;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Filterproffesional = (ArrayList<ProfessionalList.Professional >) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        ImageView img_profession;
        TextView txt_name;
        TextView txt_skills;
        RelativeLayout linear_layout;
        ImageView img_chat_detail;
        RatingBar ratingbar;
        TextView user_time,skill;

        public MyViewholder(View itemView) {
            super(itemView);
            img_profession = (ImageView) itemView.findViewById(R.id.img_profession);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_skills = (TextView) itemView.findViewById(R.id.txt_skills);
            linear_layout = (RelativeLayout) itemView.findViewById(R.id.linear_layout);
            img_chat_detail = (ImageView) itemView.findViewById(R.id.img_chat_detail);
            ratingbar = (RatingBar) itemView.findViewById(R.id.post_rating);
            skill=itemView.findViewById(R.id.skil);


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
