package com.bannet.skils.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.Model.AdsCoverageList;
import com.bannet.skils.R;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;

import java.util.List;


public class AdapterAdsCoverage extends RecyclerView.Adapter<AdapterAdsCoverage.MyViewholder> {

    Context context;
    ItemClickListener itemClickListener;
    List<AdsCoverageList.Plan> nameModels;
    int row_index;
    Dialog dialogbox;
    Button dialogClose,dialogDone;


    public interface ItemClickListener{
        public void ItemClick(String id,String validity,String planName,String coverage,String cost);
    }

    public AdapterAdsCoverage(Context context, ItemClickListener itemClickListener, List<AdsCoverageList.Plan> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_add_list_layout,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        if(nameModels.get(position).getId().equals("1")){
            PrefConnect.writeString(context,PrefConnect.PLAN_ONE,nameModels.get(position).getPlanName()+" ("+nameModels.get(position).getCoverage()+")");
        }else if(nameModels.get(position).getId().equals("2")){
            PrefConnect.writeString(context,PrefConnect.PLAN_TWO,nameModels.get(position).getPlanName()+" ("+nameModels.get(position).getCoverage()+")");
        }else if(nameModels.get(position).getId().equals("3")){
            PrefConnect.writeString(context,PrefConnect.PLAN_THREE,nameModels.get(position).getPlanName()+" ("+nameModels.get(position).getCoverage()+")");
        }else if(nameModels.get(position).getId().equals("4")){
            PrefConnect.writeString(context,PrefConnect.PLAN_FOUR,nameModels.get(position).getPlanName()+" ("+nameModels.get(position).getCoverage()+")");
        }

        String validity = GlobalMethods.getString(context,R.string.validity);
        String days = GlobalMethods.getString(context,R.string.days);
        holder.txt_name.setText(validity+" "+nameModels.get(position).getValidityMonths() +" "+days);
        holder.txt_plan_cost.setText(nameModels.get(position).getCost());
        holder.txt_range.setText(nameModels.get(position).getPlanName()+" ("+nameModels.get(position).getCoverage()+")");

        holder.buyplan.setText(GlobalMethods.getString(context,R.string.buy_now));
        holder.buyplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemClickListener.ItemClick(nameModels.get(position).getId(),nameModels.get(position).getValidityMonths(),
                        nameModels.get(position).getPlanName(),nameModels.get(position).getCoverage(),
                        nameModels.get(position).getCost());
            }
        });

    }

    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {

        TextView txt_name;
        TextView txt_range;
        LinearLayout linear_layout;
        AppCompatButton buyplan;
        TextView txt_plan_cost;


        public MyViewholder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_range = (TextView) itemView.findViewById(R.id.txt_range);
            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
            buyplan=itemView.findViewById(R.id.buy_plan);
            txt_plan_cost=itemView.findViewById(R.id.txt_plan_cost);


        }
    }


    private void showBottomnavigation() {

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_planselect_places);

        RatingBar ratingBar1=dialog.findViewById(R.id.updateratingBar);
        AppCompatButton ratindAddBtn=dialog.findViewById(R.id.txt_rating_update);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialoAnimasion;
        dialog.getWindow().setGravity(Gravity.BOTTOM);




    }

}
