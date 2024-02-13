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

import com.bannet.skils.profile.responce.StateModel;
import com.bannet.skils.R;

import java.util.List;


public class AdapterState extends RecyclerView.Adapter<AdapterState.MyViewholder> {

    Context context;
    ItemClickListener itemClickListener;
    List<StateModel.States> nameModels;
    int row_index;

    public interface ItemClickListener{
        public void ItemClick(String id, String state_name);
    }

    public AdapterState(Context context, ItemClickListener itemClickListener, List<StateModel.States> nameModels) {
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

        holder.txt_value.setText(nameModels.get(position).getName());
        holder.txt_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                String state_id = nameModels.get(position).getId();
                String state_name = nameModels.get(position).getName();
                itemClickListener.ItemClick(state_id,state_name);
                holder.tick_selected.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
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
