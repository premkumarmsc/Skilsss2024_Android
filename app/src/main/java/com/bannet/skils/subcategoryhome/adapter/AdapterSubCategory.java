package com.bannet.skils.subcategoryhome.adapter;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bannet.skils.R;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.subcategoryhome.responce.SubcategoryResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSubCategory extends RecyclerView.Adapter<AdapterSubCategory.MyviewHolder>{

    public Context context;
    public List<SubcategoryResponse.Skill>categoryList;
    public ArrayList<Integer> selectCheck = new ArrayList<>();
    AdapterSubCategory.itemClickLissener itemClickLissener;

    public AdapterSubCategory(Context context, List<SubcategoryResponse.Skill> categoryList, AdapterSubCategory.itemClickLissener itemClickLissener) {
        this.context = context;
        this.categoryList = categoryList;
        this.itemClickLissener = itemClickLissener;
    }

    public interface itemClickLissener{
        public void itemclick(String skilssId);
    }

    @NonNull
    @Override
    public AdapterSubCategory.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategory,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSubCategory.MyviewHolder holder, int position) {

        int sise = holder.getAdapterPosition() % 2;

        for (int i = 0; i < categoryList.size(); i++) {
            selectCheck.add(0);
        }

        holder.catName.setText(categoryList.get(holder.getAdapterPosition()).getSkillName());

        holder.itemView.setOnClickListener(v -> {

            for(int k=0; k<selectCheck.size(); k++) {

                if(k==holder.getAdapterPosition()) {

                    selectCheck.set(k,1);
                }
                else {

                    selectCheck.set(k,0);
                }
            }
            notifyDataSetChanged();
        });

        if(selectCheck.get(holder.getAdapterPosition()) == 1){

            itemClickLissener.itemclick(categoryList.get(holder.getAdapterPosition()).getId());
            holder.selectItem.setVisibility(View.VISIBLE);

        }
        else {

            holder.selectItem.setVisibility(View.GONE);

        }

        Glide.with(holder.itemView)
                .load(categoryList.get(holder.getAdapterPosition()).getSkillsImageUrl() + categoryList.get(holder.getAdapterPosition()).getSkillsImage())
                .fitCenter()
                .into(holder.catImage);

        if(sise == 0 ){
            holder.background.setBackgroundResource(R.drawable.category_bule_bg);
            holder.backGroundround.setBackgroundResource(R.drawable.bg_round_cat_blue);
        }
        else {

            holder.background.setBackgroundResource(R.drawable.category_green_bg);
            holder.backGroundround.setBackgroundResource(R.drawable.bg_round_cat_gree);

        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder {

        public CircleImageView catImage;
        public TextView catName;
        public LinearLayout background;
        public CardView selectItem;
        public RelativeLayout backGroundround;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            catImage = itemView.findViewById(R.id.cat_image);
            catName = itemView.findViewById(R.id.cat_name);
            background = itemView.findViewById(R.id.bgg);
            selectItem = itemView.findViewById(R.id.select_btn);
            backGroundround = itemView.findViewById(R.id.back_ground);

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
