package com.bannet.skils.Adapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bannet.skils.Model.WebModel.AdvertismentModel;
import com.bumptech.glide.Glide;
import com.bannet.skils.Model.PostViewPageImage;
import com.bannet.skils.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class AdapterPostingViewPager extends SliderViewAdapter<AdapterPostingViewPager.Holder> {

    private Context context;
    List<PostViewPageImage> image;

    public AdapterPostingViewPager(Context context, List<PostViewPageImage> image) {
        this.context = context;
        this.image = image;
    }


    @Override
    public AdapterPostingViewPager.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_home_layout_three,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPostingViewPager.Holder viewHolder, int position) {


        String random_image = image.get(position).getImageUrl() + image.get(position).getImage();

        Glide.with(context).load(random_image).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return image.size();
    }

    public class Holder extends  SliderViewAdapter.ViewHolder{

        ImageView imageView;

        public Holder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.img_banner);

        }
    }
}
