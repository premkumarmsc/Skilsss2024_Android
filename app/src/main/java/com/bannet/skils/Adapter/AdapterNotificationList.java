package com.bannet.skils.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.notification.responce.NotificationModel;
import com.bannet.skils.R;

import java.util.List;


public class AdapterNotificationList extends RecyclerView.Adapter<AdapterNotificationList.MyViewholder> {

    Context context;
    ItemClickListener itemClickListener;
    List<NotificationModel.Notification> nameModels;
    int row_index;

    public interface ItemClickListener{
        public void ItemClick(int position);
    }

    public AdapterNotificationList(Context context, ItemClickListener itemClickListener, List<NotificationModel.Notification> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationsinglecard,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        holder.txt_heading.setText(nameModels.get(position).getTitle());
        holder.txt_desc.setText(nameModels.get(position).getDescription());


    }


    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        TextView txt_heading;
        TextView txt_desc;
        LinearLayout linear_layout;


        public MyViewholder(View itemView) {
            super(itemView);
            txt_heading = (TextView) itemView.findViewById(R.id.txt_heading);
            txt_desc = (TextView) itemView.findViewById(R.id.txt_desc);
            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);

        }
    }
}
