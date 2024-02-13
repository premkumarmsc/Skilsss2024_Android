package com.bannet.skils.explore.adapter;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.R;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.post.responce.PostinglistModel;
import com.bannet.skils.postingdetails.activity.ActivityPostingsDetailsScreen;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterTopPostings extends RecyclerView.Adapter<AdapterTopPostings.Myviewholder> {

    public Context context;
    public List<PostinglistModel.Posting>postingList;
    public String images_list,USER_ID;
    public String userRegister;

    public AdapterTopPostings(Context context, List<PostinglistModel.Posting> postingList) {
        this.context = context;
        this.postingList = postingList;
    }

    @NonNull
    @Override
    public AdapterTopPostings.Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_rate_posting,parent,false);
        return new Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTopPostings.Myviewholder holder, int position) {

        userRegister = PrefConnect.readString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"");
        USER_ID = PrefConnect.readString(context, PrefConnect.USER_ID, "");
        images_list = postingList.get(holder.getAdapterPosition()).getPostImages();
        holder.title.setText(postingList.get(holder.getAdapterPosition()).getTitle());
        holder.skilsss.setText(postingList.get(holder.getAdapterPosition()).getDescription());

        if (images_list == null) {
            holder.postImage.setImageResource(R.drawable.select_skil_image);

        } else {
            String[] str = images_list.split(",");
            // Iterating over the string
            for (int i = 0; i < str.length; i++) {
                // Printing the elements of String array;
                Glide.with(context).load(postingList.get(holder.getAdapterPosition()).getPostImageUrl() + str[0]).into(holder.postImage);
            }
        }

        holder.itemView.setOnClickListener(v -> {

            if(userRegister.equals("customer")){

                userLogin();

            }
            else {
                Intent in = new Intent(context, ActivityPostingsDetailsScreen.class);
                in.putExtra("post_id", postingList.get(holder.getAdapterPosition()).getId());
                in.putExtra("post_user_id", postingList.get(holder.getAdapterPosition()).getUserId());
                in.putExtra("notification_id", "");
                in.putExtra("type", "1");
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }

        });

    }

    @Override
    public int getItemCount() {
        int size = postingList.size();
        return (Math.min(size, 4));
    }

    public static class Myviewholder extends RecyclerView.ViewHolder {

        public ImageView postImage;
        public TextView title,skilsss;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.post_image);
            title = itemView.findViewById(R.id.title);
            skilsss = itemView.findViewById(R.id.skils);
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
