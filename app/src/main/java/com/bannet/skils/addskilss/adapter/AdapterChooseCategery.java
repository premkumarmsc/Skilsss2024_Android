package com.bannet.skils.addskilss.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bannet.skils.R;
import com.bannet.skils.explore.response.CategoryResponce;
import java.util.List;

public class AdapterChooseCategery extends RecyclerView.Adapter<AdapterChooseCategery.MyViewholder>{

    Context context;
    ItemClickListener itemClickListener;
    List<CategoryResponce.Category> nameModels;
    int row_index;

    public AdapterChooseCategery(Context context, ItemClickListener itemClickListener, List<CategoryResponce.Category> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    public interface ItemClickListener{
        public void ItemClick(String id, String state_name);
    }


    @NonNull
    @Override
    public AdapterChooseCategery.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChooseCategery.MyViewholder holder, int position) {

        holder.txt_value.setText(nameModels.get(holder.getAdapterPosition()).getCategoryName());

        holder.txt_value.setOnClickListener(view -> {
            row_index = holder.getAdapterPosition();
            String state_id = nameModels.get(holder.getAdapterPosition()).getCategoryId();
            String state_name = nameModels.get(holder.getAdapterPosition()).getCategoryName();
            itemClickListener.ItemClick(state_id,state_name);
            holder.tick_selected.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        });

        if(row_index == holder.getAdapterPosition()){
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
