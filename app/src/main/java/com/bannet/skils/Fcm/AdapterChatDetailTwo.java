package com.bannet.skils.Fcm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bannet.skils.Model.UserDetailsOther;
import com.bannet.skils.R;
import com.bannet.skils.service.PrefConnect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uniflyn on 8/11/17.
 */

public class AdapterChatDetailTwo extends RecyclerView.Adapter<AdapterChatDetailTwo.ViewHolder> {
    Context contextx;
    String my_id = "";
    private int SELF = 100, OTHRES = 200;
    private int SELFTWO;
    Dialog dialogImageview;
    List<UserDetailsOther> userDetailsOthers = new ArrayList<>();
    ItemClickListener itemClickListener;
    ImageView PhotoView,close_Img;
    String user_id;

    public AdapterChatDetailTwo(Context contextx, List<UserDetailsOther> userDetailsOthers, ItemClickListener itemClickListener) {
        this.contextx = contextx;
        this.userDetailsOthers = userDetailsOthers;
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        public void ItemClick(String imageurl);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;


        if (viewType == SELF) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_from, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {

        user_id = PrefConnect.readString(contextx, PrefConnect.USER_ID, "");
        my_id = user_id;


        Log.e("SELFTWO", String.valueOf(SELFTWO));
        Log.e("user_id", String.valueOf(my_id));

        UserDetailsOther userDetailsOther = userDetailsOthers.get(position);
        Log.e("SELFTWO",userDetailsOther.getUserid());
        if (userDetailsOther.getUserid().equals(my_id)) {
            SELFTWO = 300;
            return SELF;
        } else {
            SELFTWO = 400;
        }
        return OTHRES;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        if (SELFTWO == 300) {


            Glide.with(contextx).load(userDetailsOthers.get(position).getProfile_url()).into(holder.img_profile);
            holder.txt_time.setText(userDetailsOthers.get(position).getTime());
            if (userDetailsOthers.get(position).getImage().equals("")){
                holder.txtMsg.setText(userDetailsOthers.get(position).getUserMessage());
                holder.txtMsg.setVisibility(View.VISIBLE);
                holder.image_view.setVisibility(View.GONE);
            }else {
                Glide.with(contextx).load(userDetailsOthers.get(position).getImage()).into(holder.image_view);
                holder.txtMsg.setVisibility(View.GONE);
                holder.image_view.setVisibility(View.VISIBLE);

            }

        } else if (SELFTWO == 400) {

            Glide.with(contextx).load(userDetailsOthers.get(position).getProfile_url()).into(holder.img_profile);
            holder.txt_time.setText(userDetailsOthers.get(position).getTime());
            if (userDetailsOthers.get(position).getImage().equals("")){
                holder.txtMsg.setText(userDetailsOthers.get(position).getUserMessage());
                holder.txtMsg.setVisibility(View.VISIBLE);
                holder.image_view.setVisibility(View.GONE);
            }else {
                Glide.with(contextx).load(userDetailsOthers.get(position).getImage()).into(holder.image_view);
                holder.txtMsg.setVisibility(View.GONE);
                holder.image_view.setVisibility(View.VISIBLE);

            }
        }

        holder.image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemClickListener.ItemClick(userDetailsOthers.get(position).getImage());


            }
        });

    }

    @Override
    public int getItemCount() {
        return userDetailsOthers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMsg, txt_time;
        ImageView img_profile,image_view;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMsg = (TextView) itemView.findViewById(R.id.txtMsg);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            img_profile = (ImageView) itemView.findViewById(R.id.img_profile);
            image_view = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }



}
