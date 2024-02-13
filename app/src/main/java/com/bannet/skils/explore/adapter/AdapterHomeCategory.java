package com.bannet.skils.explore.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.R;
import com.bannet.skils.explore.response.CategoryResponce;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.explore.response.CategoryLocalREsponse;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.subcategoryhome.activity.ActivitySubcategoryHomePage;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterHomeCategory extends RecyclerView.Adapter<AdapterHomeCategory.MyviewHolder> {

    public Context context;
    public List<CategoryResponce.Category>categoryResponceList;
    public int j;
    public int k;
    public int a = 2;
    public String USER_ID,userRegister;
    public AdapterHomeCategory.ItemClickLissener itemclickLissener;

    public AdapterHomeCategory(Context context, List<CategoryResponce.Category> categoryResponceList, ItemClickLissener itemclickLissener) {
        this.context = context;
        this.categoryResponceList = categoryResponceList;
        this.itemclickLissener = itemclickLissener;
    }

    public interface ItemClickLissener{

        void itemclick(String type,String id);


    }

    @NonNull
    @Override
    public AdapterHomeCategory.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_category,parent,false);
        return new MyviewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHomeCategory.MyviewHolder holder, int position) {


        int sise = holder.getAdapterPosition() % 2;
        userRegister = PrefConnect.readString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"");
        USER_ID = PrefConnect.readString(context, PrefConnect.USER_ID, "");
        holder.catName.setText(categoryResponceList.get(holder.getAdapterPosition()).getCategoryName());

        Glide.with(holder.itemView)
                .load(categoryResponceList.get(holder.getAdapterPosition()).getImageUrl()+categoryResponceList.get(holder.getAdapterPosition()).getCategoryImage())
                .fitCenter()
                .into(holder.catImage);

        if(sise == 0 ){

            holder.background.setBackgroundResource(R.drawable.category_green_bg);

        }
        else {

            holder.background.setBackgroundResource(R.drawable.category_bule_bg);

        }


        holder.itemView.setOnClickListener(view -> {

            if(userRegister.equals("customer")){

                userLogin();

            }
            else {
                Intent intent = new Intent(context, ActivitySubcategoryHomePage.class);
                intent.putExtra("cat_id",categoryResponceList.get(holder.getAdapterPosition()).getCategoryId());
                intent.putExtra("type","add");
                context.startActivity(intent);
            }

        });

        if(holder.getAdapterPosition() +1 == categoryResponceList.size()){

            itemclickLissener.itemclick("lastindex",categoryResponceList.get(holder.getAdapterPosition()).getCategoryId());
            Log.e("last","lastindex");

        }

    }

    @Override
    public int getItemCount() {

        return categoryResponceList.size();

    }

    public static class MyviewHolder extends RecyclerView.ViewHolder {

        public ImageView catImage;
        public TextView catName;
        public LinearLayout background;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            catImage = itemView.findViewById(R.id.cat_image);
            catName = itemView.findViewById(R.id.cat_name);
            background = itemView.findViewById(R.id.bgg);

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
