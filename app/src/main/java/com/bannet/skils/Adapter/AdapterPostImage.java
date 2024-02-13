package com.bannet.skils.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.bannet.skils.post.responce.PostImageModel;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdapterPostImage extends RecyclerView.Adapter<AdapterPostImage.MyViewholder> {

    Context context;
    ItemClickListener itemClickListener;
    List<PostImageModel> nameModels;
    int row_index;
    ArrayList<String> banner_image = new ArrayList<>();
    String USER_ID;

    public interface ItemClickListener{
        public void ItemClick(ImageView image);
    }

    public AdapterPostImage(Context context, ItemClickListener itemClickListener, List<PostImageModel> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");

        if(nameModels.size() == 0){
            holder.cancel_pic.setVisibility(View.GONE);
        }
        Glide.with(context).load(nameModels.get(position).getImagePath()).into(holder.img_main);

        getprofile(USER_ID,holder.img_main);
        holder.id_add.setVisibility(View.GONE);


        holder.cancel_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameModels.size() != 0){

                    itemClickListener.ItemClick(holder.img_main);
                    nameModels.remove(holder.getAdapterPosition());

                }
                else {

                    holder.itemView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        ImageView id_add;
        ImageView img_main;
        LinearLayout linear_layout;
        ImageView cancel_pic;


        public MyViewholder(View itemView) {
            super(itemView);
            id_add = (ImageView) itemView.findViewById(R.id.id_add);
            img_main = (ImageView) itemView.findViewById(R.id.img_main);
            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
            cancel_pic=itemView.findViewById(R.id.cancel_pic);

        }
    }
    private void getprofile(String user_id, ImageView bannerImage) {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().getprofile(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<getprofileModel>() {
                @Override
                public void onResponse(Call<getprofileModel> call, Response<getprofileModel> response) {

                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("1")){

                            String text = response.body().getDetails().getSkilId();


                            String[] arrSplit = text.split(", ");
                            for (int i=0; i < arrSplit.length; i++)
                            {
                                String imageName=arrSplit[i];

                                banner_image.add(imageName);

                            }

                            for(int j=0; j<banner_image.size();j++){

                            }

                        }else {

                        }
                    }
                }

                @Override
                public void onFailure(Call<getprofileModel> call, Throwable t) {
                    //  progressDialog.dismiss();

                }
            });
        }else {
            GlobalMethods.Toast(context, "No Internet Connection");
        }

    }
}
