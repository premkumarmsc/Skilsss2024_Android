package com.bannet.skils.Activitys.fragment.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.R;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.chat.activity.ActivityChatDetails;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.professinoldetails.activity.ActivityProffDetails;
import com.bannet.skils.professinoldetails.activity.ActivityWithoutimage;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;


public class PaginationProfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    String type;
    String USER_ID,simage_url,simageName;
    List<ProfessionalList.Professional> Filterpostlist;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;
    Resources resources;

    public PaginationProfAdapter(Context context,String type) {
        this.context = context;
        Filterpostlist = new LinkedList<>();
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
       /* View viewItem = inflater.inflate(R.layout.item_explore, parent, false);
        viewHolder = new MovieViewHolder(viewItem);*/
        switch (ITEM) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.activity_home_layout, parent, false);
                viewHolder = new MyViewholder(viewItem);
                break;
          case LOADING:
                View viewLoading = inflater.inflate(R.layout.layout_progress_bar, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder11, int position) {


         switch (ITEM) {
               case ITEM:

                   MyViewholder holder = (MyViewholder) holder11;
                   holder.skill.setText(GlobalMethods.getString(context,R.string.skills));
                   holder.sway.setText(GlobalMethods.getString(context,R.string.away));
                   holder.srating.setText(GlobalMethods.getString(context,R.string.ratings));

                   USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");

                   if(Filterpostlist.get(holder.getAdapterPosition()).getImageApprove()!=null){
                       simage_url = Filterpostlist.get(holder.getAdapterPosition()).getImageUrl();
                       simageName = Filterpostlist.get(holder.getAdapterPosition()).getImageName();

                       Glide.with(context).load(simage_url+simageName).error(R.drawable.same_profff).into(holder.img_profession);

                   }
                   else {

                       holder.img_profession.setImageResource(R.drawable.same_profff);
                   }
                   holder.txt_name.setText(Filterpostlist.get(holder.getAdapterPosition()).getFirstName()+" " +Filterpostlist.get(holder.getAdapterPosition()).getLastName());
                   holder.txt_skills.setText(Filterpostlist.get(holder.getAdapterPosition()).getSkillsName());

                   if(Filterpostlist.get(holder.getAdapterPosition()).getRatings()!=null) {
                       holder.ratingbar.setRating(Float.parseFloat(Filterpostlist.get(holder.getAdapterPosition()).getRatings()));
                   }

                   double blati = Double.parseDouble(Filterpostlist.get(holder.getAdapterPosition()).getLatitude());
                   double blong = Double.parseDouble(Filterpostlist.get(holder.getAdapterPosition()).getLongitude());
                   double mlati = Double.parseDouble(PrefConnect.readString(context,PrefConnect.LATITUDE,""));
                   double mlong = Double.parseDouble(PrefConnect.readString(context,PrefConnect.LOGITUDE,""));

                   double distance = distance(blati,blong,mlati,mlong);

                   double estimation = milesTokm(distance);

                   DecimalFormat tf = new DecimalFormat();
                   tf.setMaximumFractionDigits(1);
                   String eCalculate = tf.format(estimation);

                   holder.away.setText(eCalculate + "Km");

                   holder.rating.setText(Filterpostlist.get(holder.getAdapterPosition()).getRatings());

                   // holder.user_time.setText(Filterproffesional.get(holder.getAdapterPosition()).getAvailableFrom()+"-"+Filterproffesional.get(holder.getAdapterPosition()).getAvailableTo());
                   holder.linear_layout.setOnClickListener(v -> {

                           if(!USER_ID.equals(Filterpostlist.get(holder.getAdapterPosition()).getId())){

                               if(Filterpostlist.get(holder.getAdapterPosition()).getBannerImage().equals("") || Filterpostlist.get(holder.getAdapterPosition()).getBannerImage().isEmpty()){

                                   Intent in = new Intent(context, ActivityWithoutimage.class);
                                   in.putExtra("profe_id",Filterpostlist.get(holder.getAdapterPosition()).getId());
                                   in.putExtra("profileurl",Filterpostlist.get(holder.getAdapterPosition()).getImageUrl());
                                   in.putExtra("profile_name",Filterpostlist.get(holder.getAdapterPosition()).getImageName());
                                   in.putExtra("latitude",Filterpostlist.get(holder.getAdapterPosition()).getLatitude());
                                   in.putExtra("logitude",Filterpostlist.get(holder.getAdapterPosition()).getLongitude());
                                   in.putExtra("type","1");
                                   in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                   context.startActivity(in);

                               }
                               else {
                                   Intent in = new Intent(context, ActivityProffDetails.class);
                                   in.putExtra("profe_id",Filterpostlist.get(holder.getAdapterPosition()).getId());
                                   in.putExtra("profileurl",Filterpostlist.get(holder.getAdapterPosition()).getImageUrl());
                                   in.putExtra("profile_name",Filterpostlist.get(holder.getAdapterPosition()).getImageName());
                                   in.putExtra("latitude",Filterpostlist.get(holder.getAdapterPosition()).getLatitude());
                                   in.putExtra("logitude",Filterpostlist.get(holder.getAdapterPosition()).getLongitude());
                                   in.putExtra("type","1");
                                   in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                   context.startActivity(in);
                               }

                           }

                   });

                   holder.img_chat_detail.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                               if(!USER_ID.equals(Filterpostlist.get(holder.getAdapterPosition()).getId())){

                                   Intent in = new Intent(context, ActivityChatDetails.class);
                                   in.putExtra("other_user_id",Filterpostlist.get(holder.getAdapterPosition()).getId());
                                   in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                   context.startActivity(in);

                               }

                       }
                   });
                    break;
                    case LOADING:
                    LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder11;
                    loadingViewHolder.progressBar.setVisibility(View.GONE);
                    break;
                  }
    }

    @Override
    public int getItemCount() {

        if(type.equals("1")){

            int size = Filterpostlist.size();
            return (Math.min(size, 4));
        }
        else {
            return Filterpostlist == null ? 0 : Filterpostlist.size() - 1;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return (position == Filterpostlist.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ProfessionalList.Professional());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = Filterpostlist.size() - 1;
        ProfessionalList.Professional result = getItem(position);

        if (result != null) {
            Filterpostlist.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(ProfessionalList.Professional movie) {
        Filterpostlist.add(movie);
        notifyItemInserted(Filterpostlist.size() - 1);

    }

    public void addAll(List<ProfessionalList.Professional> moveResults) {
        for (ProfessionalList.Professional result : moveResults) {
            add(result);
        }
    }


    public void ClearAll() {
        Filterpostlist.clear();
        notifyDataSetChanged();
    }

    private void userLogin() {

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_not_logon_dialog);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialoAnimasion;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


        TextView title = dialog.findViewById(R.id.title);
        AppCompatButton login = dialog.findViewById(R.id.login_btn);

        title.setText(GlobalMethods.getString(context,R.string.please_login));
        login.setText(GlobalMethods.getString(context,R.string.go_to_login));

        login.setOnClickListener(view -> {

            Intent intent = new Intent(context, ActivityPhonenumberScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });
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

    private static double milesTokm(double distanceInMiles) {
        return distanceInMiles * 1.60934;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);

    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public ProfessionalList.Professional getItem(int position) {
        return Filterpostlist.get(position);
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        ImageView img_profession;
        TextView txt_name;
        TextView txt_skills;
        RelativeLayout linear_layout;
        ImageView img_chat_detail;
        RatingBar ratingbar;
        TextView user_time,skill,away,rating,sway,srating;

        public MyViewholder(View itemView) {
            super(itemView);
            img_profession = (ImageView) itemView.findViewById(R.id.img_profession);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_skills = (TextView) itemView.findViewById(R.id.txt_skills);
            linear_layout = (RelativeLayout) itemView.findViewById(R.id.linear_layout);
            img_chat_detail = (ImageView) itemView.findViewById(R.id.img_chat_detail);
            ratingbar = (RatingBar) itemView.findViewById(R.id.post_rating);
            skill=itemView.findViewById(R.id.skil);
            away = itemView.findViewById(R.id.txt_Km);
            rating=itemView.findViewById(R.id.txt_review);
            sway = itemView.findViewById(R.id.s_away);
            srating=itemView.findViewById(R.id.s_rating);


        }
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);

        }
    }


}