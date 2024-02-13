package com.bannet.skils.viewallpostings.adapter;

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
import com.bannet.skils.post.responce.PostinglistModel;
import com.bannet.skils.postingdetails.activity.ActivityPostingsDetailsScreen;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterViewAllPosting extends RecyclerView.Adapter<AdapterViewAllPosting.MyViewHolder> {

    public Context context;
    public String images_list,USER_ID;
    private List<PostinglistModel.Posting> Filterpostlist;

    public AdapterViewAllPosting(Context context, List<PostinglistModel.Posting> filterpostlist) {
        this.context = context;
        Filterpostlist = filterpostlist;
    }

    @NonNull
    @Override
    public AdapterViewAllPosting.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_layout_two_new, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewAllPosting.MyViewHolder holder, int position) {


        images_list = Filterpostlist.get(holder.getAdapterPosition()).getPostImages();
        USER_ID = PrefConnect.readString(context, PrefConnect.USER_ID, "");
        if (images_list == null) {
            holder.img_profession.setImageResource(R.drawable.select_skil_image);

        } else {
            String[] str = images_list.split(",");
            // Iterating over the string
            for (int i = 0; i < str.length; i++) {
                // Printing the elements of String array;
                Glide.with(context).load(Filterpostlist.get(holder.getAdapterPosition()).getPostImageUrl() + str[0]).error(R.drawable.app_name).into(holder.img_profession);
            }
        }
        holder.txt_name.setText(Filterpostlist.get(holder.getAdapterPosition()).getTitle());
        holder.txt_skills.setText(Filterpostlist.get(holder.getAdapterPosition()).getSkillsName());


        String strDate = Filterpostlist.get(position).getCreatedAt();
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
        holder.txt_created_at.setText(finalDate);

        holder.linear_layout.setOnClickListener(v -> {

            if(USER_ID.equals("customer")){

                userLogin();

            }
            else {
                Intent in = new Intent(context, ActivityPostingsDetailsScreen.class);
                in.putExtra("post_id", Filterpostlist.get(holder.getAdapterPosition()).getId());
                in.putExtra("post_user_id", Filterpostlist.get(holder.getAdapterPosition()).getUserId());
                in.putExtra("notification_id", "");
                in.putExtra("type", "1");
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }

        });
        holder.img_chat_postings_new.setOnClickListener(view -> {

            if(USER_ID.equals("customer")){

                userLogin();

            }
            else {
                if (!USER_ID.equalsIgnoreCase(Filterpostlist.get(holder.getAdapterPosition()).getUserId())) {

                    Intent in = new Intent(context, ActivityChatDetails.class);
                    in.putExtra("other_user_id", Filterpostlist.get(holder.getAdapterPosition()).getUserId());
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return Filterpostlist.size();
    }

    public void ClearAll() {
        Filterpostlist.clear();
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_profession;
        TextView txt_name;
        TextView txt_skills;
        RelativeLayout linear_layout;
        ImageView img_chat_postings_new;
        TextView txt_created_at;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_profession =  itemView.findViewById(R.id.img_profession);
            txt_name =  itemView.findViewById(R.id.txt_name);
            txt_skills =  itemView.findViewById(R.id.txt_skills);
            linear_layout =  itemView.findViewById(R.id.linear_layout);
            img_chat_postings_new =  itemView.findViewById(R.id.img_chat_postings_new);
            txt_created_at =  itemView.findViewById(R.id.txt_created_at);

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
}
