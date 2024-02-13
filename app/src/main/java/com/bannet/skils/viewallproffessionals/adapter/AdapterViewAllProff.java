package com.bannet.skils.viewallproffessionals.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.R;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.chat.activity.ActivityChatDetails;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterViewAllProff extends RecyclerView.Adapter<AdapterViewAllProff.MyViewHolder> {

    public Context context;
    public String type;
    public String USER_ID,simage_url,simageName;
    private String userRegister;
    public AdapterViewAllProff.itemClikcLissner itemClikcLissner;
    public List<ProfessionalList.Professional> Filterpostlist;

    public AdapterViewAllProff(Context context, List<ProfessionalList.Professional> filterpostlist,String type,itemClikcLissner itemClikcLissner) {
        this.context = context;
        this.type = type;
        this.itemClikcLissner = itemClikcLissner;
        Filterpostlist = filterpostlist;
    }

    public interface itemClikcLissner{

        void itemclick(ProfessionalList.Professional professional);

    }

    @NonNull
    @Override
    public AdapterViewAllProff.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewAllProff.MyViewHolder holder, int position) {

        userRegister = PrefConnect.readString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"");
        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");

        holder.skill.setText(GlobalMethods.getString(context,R.string.skills));
        holder.sway.setText(GlobalMethods.getString(context,R.string.away));
        holder.srating.setText(GlobalMethods.getString(context,R.string.ratings));

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
        // holder.user_time.setText(Filterproffesional.get(holder.getAdapterPosition()).getAvailableFrom()+"-"+Filterproffesional.get(holder.getAdapterPosition()).getAvailableTo());
        holder.linear_layout.setOnClickListener(v -> {

            if(userRegister.equals("customer")){

                userLogin();

            }
            else {
                if(!USER_ID.equals(Filterpostlist.get(holder.getAdapterPosition()).getId())){

                    itemClikcLissner.itemclick(Filterpostlist.get(holder.getAdapterPosition()));


                }
            }


        });

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

        holder.img_chat_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userRegister.equals("customer")){

                    userLogin();

                }
                else {
                    if(!USER_ID.equals(Filterpostlist.get(holder.getAdapterPosition()).getId())){

                        Intent in = new Intent(context, ActivityChatDetails.class);
                        in.putExtra("other_user_id",Filterpostlist.get(holder.getAdapterPosition()).getId());
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(in);

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(type.equals("1")){

            int size = Filterpostlist.size();
            return (Math.min(size, 6));
        }
        else {
            return Filterpostlist.size();
        }
    }

    public void ClearAll() {

        if(Filterpostlist !=  null){

            Filterpostlist.clear();
            notifyDataSetChanged();

        }

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_profession;
        TextView txt_name;
        TextView txt_skills;
        RelativeLayout linear_layout;
        ImageView img_chat_detail;
        RatingBar ratingbar;
        TextView skill,away,rating,sway,srating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_profession =  itemView.findViewById(R.id.img_profession);
            txt_name =  itemView.findViewById(R.id.txt_name);
            txt_skills =  itemView.findViewById(R.id.txt_skills);
            linear_layout =  itemView.findViewById(R.id.linear_layout);
            img_chat_detail =  itemView.findViewById(R.id.img_chat_detail);
            ratingbar =  itemView.findViewById(R.id.post_rating);
            skill=itemView.findViewById(R.id.skil);
            away = itemView.findViewById(R.id.txt_Km);
            rating=itemView.findViewById(R.id.txt_review);
            sway = itemView.findViewById(R.id.s_away);
            srating=itemView.findViewById(R.id.s_rating);
        }
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
}
