package com.bannet.skils.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bannet.skils.chat.activity.ActivityChatDetails;
import com.bannet.skils.chat.responce.ChatListModel;
import com.bannet.skils.R;
import com.bannet.skils.service.PrefConnect;

import java.util.List;


public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.MyViewholder> {

    Context context;
    ItemClickListener itemClickListener;
    List<ChatListModel.Chat> nameModels;
    int row_index;
    String USER_id;

    public interface ItemClickListener{
        public void ItemClick(int position);
    }

    public AdapterChatList(Context context, ItemClickListener itemClickListener, List<ChatListModel.Chat> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_list_layout,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        Glide.with(context).load(nameModels.get(position).getOppUserImage()).error(R.drawable.profile_image).into(holder.img_chat);
        holder.txt_name.setText(nameModels.get(position).getOppUserName());
        holder.txt_last_chat.setText(nameModels.get(position).getLastChat());

        USER_id= PrefConnect.readString(context,PrefConnect.USER_ID,"");

        holder.linear_layout.setOnClickListener(v -> {

            if(!USER_id.equals(nameModels.get(position).getOppUserId())){

                Intent in = new Intent(context, ActivityChatDetails.class);
                in.putExtra("other_user_id",nameModels.get(position).getOppUserId());
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);

            }

        });
    }

    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        ImageView img_chat;
        TextView txt_name;
        TextView txt_last_chat;
        LinearLayout linear_layout;


        public MyViewholder(View itemView) {
            super(itemView);
            img_chat =  itemView.findViewById(R.id.img_chat);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_last_chat = (TextView) itemView.findViewById(R.id.txt_last_chat);
            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);

        }
    }
}
