package com.bannet.skils.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.bannet.skils.Model.WebModel.AdvertismentModel;
import com.bannet.skils.R;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {

    private Context context;
    List<AdvertismentModel.Ads> image;

    public SliderAdapter(Context context, List<AdvertismentModel.Ads> image) {
        this.context = context;
        this.image = image;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_home_layout_three,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {

        String url = image.get(position).getData().getExternalLink();
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(url.equals("")){

                }else{

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }

            }
        });
        String random_image = image.get(position).getData().getImageUrl() + image.get(position).getData().getAdsImage();

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
