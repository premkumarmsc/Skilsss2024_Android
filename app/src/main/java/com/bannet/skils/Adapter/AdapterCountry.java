package com.bannet.skils.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.profile.responce.CountryModel;
import com.bannet.skils.R;

import java.util.List;


public class AdapterCountry extends RecyclerView.Adapter<AdapterCountry.MyViewholder> {

    Context context;
    ItemClickListener itemClickListener;
    List<CountryModel.Country> nameModels;
    int row_index = -1;

    public interface ItemClickListener{
        public void ItemClick(String country_id,String country_name);
    }

    public AdapterCountry(Context context, ItemClickListener itemClickListener, List<CountryModel.Country> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        holder.txt_value.setText(nameModels.get(position).getCountry());

        holder.txt_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                String country_id = nameModels.get(position).getId();
                String country_name = nameModels.get(position).getCountry();
                itemClickListener.ItemClick(country_id,country_name);
                notifyDataSetChanged();
                holder.tick_selected.setVisibility(View.VISIBLE);
            }
        });

        if(row_index == position){
            holder.tick_selected.setVisibility(View.VISIBLE);
        }else {
            holder.tick_selected.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        TextView txt_value;
        LinearLayout linear_layout;
        ImageView tick_selected;

        public MyViewholder(View itemView) {
            super(itemView);
            txt_value = (TextView) itemView.findViewById(R.id.txt_value);
            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
            tick_selected = (ImageView) itemView.findViewById(R.id.tick_selected);

        }
    }
}
