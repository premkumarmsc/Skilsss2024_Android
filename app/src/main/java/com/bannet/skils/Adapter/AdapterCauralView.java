package com.bannet.skils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.bannet.skils.Model.AdsCoverageList;
import com.bannet.skils.R;
import com.bannet.skils.service.GlobalMethods;


import java.util.List;

public class AdapterCauralView extends RecyclerView.Adapter<AdapterCauralView.SliderViewHolder> {

    public Context context;
    private List<AdsCoverageList.Plan>cauralViews;
    private ViewPager2 viewPager2;
    public AdapterCauralView.ItemClickListener itemClickListener;
    public int currentposition;

    public AdapterCauralView(Context context, List<AdsCoverageList.Plan> cauralViews, ViewPager2 viewPager2, ItemClickListener itemClickListener) {
        this.context = context;
        this.cauralViews = cauralViews;
        this.viewPager2 = viewPager2;
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        public void ItemClick(String position);
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sliderview,parent,false));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {

        holder.amount.setText("$ " + cauralViews.get(holder.getAdapterPosition()).getCost());

        String validity = GlobalMethods.getString(context,R.string.validity);
        String days = GlobalMethods.getString(context,R.string.days);
        itemClickListener.ItemClick(String.valueOf(holder.getOldPosition()));
        holder.title2.setText(validity+" "+cauralViews.get(position).getValidityMonths() +" "+days);
        holder.title1.setText(cauralViews.get(position).getPlanName()+" ("+cauralViews.get(position).getCoverage()+")");


    }

    @Override
    public int getItemCount() {
        return cauralViews.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {

        public AppCompatButton amount;
        private TextView title1,title2;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            title1 = itemView.findViewById(R.id.title1);
            title2 = itemView.findViewById(R.id.title2);

        }

    }


}
