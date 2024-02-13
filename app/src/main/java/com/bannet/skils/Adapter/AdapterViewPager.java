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

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bannet.skils.Model.WebModel.AdvertismentModel;
import com.bannet.skils.R;

import java.util.List;


public class AdapterViewPager extends PagerAdapter {

    private Context context;
    LayoutInflater layoutInflater;
    List<AdvertismentModel.Ads> image;

    public AdapterViewPager(Context context, List<AdvertismentModel.Ads> image) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.image = image;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = layoutInflater.inflate(R.layout.activity_home_layout_three, container, false);

        ImageView imageView_main = itemView.findViewById(R.id.img_banner);

        String random_image = image.get(position).getData().getImageUrl() + image.get(position).getData().getAdsImage();


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = image.get(position).getData().getExternalLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        Glide.with(context).load(random_image).into(imageView_main);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
