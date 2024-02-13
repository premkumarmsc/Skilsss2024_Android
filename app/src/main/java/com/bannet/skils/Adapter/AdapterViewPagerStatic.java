package com.bannet.skils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.bannet.skils.home.responce.ViewPageImage;
import com.bannet.skils.R;

import java.util.List;

public class AdapterViewPagerStatic extends SliderViewAdapter<AdapterViewPagerStatic.Holder> {

    private Context context;
    LayoutInflater layoutInflater;
    List<ViewPageImage> image;

    public AdapterViewPagerStatic(Context context, List<ViewPageImage> image) {
        this.context = context;
        this.image = image;
    }

    @Override
    public AdapterViewPagerStatic.Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_home_layout_three,parent,false);
        return new AdapterViewPagerStatic.Holder(view);
    }

    @Override
    public void onBindViewHolder(AdapterViewPagerStatic.Holder viewHolder, int position) {

        int random_image = image.get(position).getImage();

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
