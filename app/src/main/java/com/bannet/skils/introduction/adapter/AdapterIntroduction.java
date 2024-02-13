package com.bannet.skils.introduction.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;

import com.bannet.skils.R;
import com.bannet.skils.introduction.model.ModelIntroductionImage;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bumptech.glide.Glide;


import java.util.List;

public class AdapterIntroduction extends PagerAdapter {

    public Context context;
    public LayoutInflater layoutInflater;
    public List<ModelIntroductionImage> image;
    public AppCompatButton start;

    public AdapterIntroduction(Context context, List<ModelIntroductionImage> image) {
        this.context = context;
        this.image = image;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View itemView = layoutInflater.inflate(R.layout.item_introduction, container, false);

        ImageView imageView_main = itemView.findViewById(R.id.intro_image);
        Glide.with(context).load(image.get(position).getImage()).into(imageView_main);

        start = itemView.findViewById(R.id.start_btn);

        start.setText(image.get(position).getBtnTitle());

         start.setOnClickListener(v -> {

            Intent intent=new Intent(context, ActivityPhonenumberScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });
        container.addView(itemView);

        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
