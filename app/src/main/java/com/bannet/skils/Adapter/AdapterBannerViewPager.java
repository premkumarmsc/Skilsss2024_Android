package com.bannet.skils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bannet.skils.home.responce.BannerImage;
import com.bannet.skils.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class AdapterBannerViewPager  extends SliderViewAdapter<AdapterBannerViewPager.Holder> {

    private Context context;
    LayoutInflater layoutInflater;
    List<BannerImage> image;

    public AdapterBannerViewPager(Context context, List<BannerImage> image) {
        this.context = context;
        this.image = image;
    }

    @Override
    public AdapterBannerViewPager.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_home_layout_three,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(AdapterBannerViewPager.Holder viewHolder, int position) {


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
