package com.bannet.skils.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.R;
import com.bannet.skils.Model.PlanStatusModel;
import com.bannet.skils.service.GlobalMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AdapterUpgradePlanStatus  extends RecyclerView.Adapter<AdapterUpgradePlanStatus.MyViewholder>{

    Context context;
    ItemClickListener itemClickListener;
    List<PlanStatusModel.Chat> nameModels;
    public String currentdate,expiredDate;

    public AdapterUpgradePlanStatus(Context context, ItemClickListener itemClickListener, List<PlanStatusModel.Chat> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    public interface ItemClickListener{
        public void ItemClick(int position);
    }

    @NonNull
    @Override
    public AdapterUpgradePlanStatus.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upgrade_plan_status,parent,false);
        return new AdapterUpgradePlanStatus.MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUpgradePlanStatus.MyViewholder holder, int position) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String d = formatter.format(date);

        String validity = GlobalMethods.getString(context,R.string.validity);

        String dt = nameModels.get(position).getExpired();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        formatter = new SimpleDateFormat("EEE, dd MMM yyyy");
        String today = formatter.format(c.getTime());

        holder.txt_validity.setText(validity + ": " +today );

        if (nameModels.get(position).getPlanId().equals("1")) {

            String plan_one = GlobalMethods.getString(context,R.string.plan1);
            holder.txt_plan.setText(GlobalMethods.getString(context,R.string.plan1));


        } else if (nameModels.get(position).getPlanId().equals("2")) {

            String plan_two = GlobalMethods.getString(context,R.string.plan2);
            holder.txt_plan.setText(plan_two);


        } else if (nameModels.get(position).getPlanId().equals("3")) {

            String plan_three = GlobalMethods.getString(context,R.string.plan3);
            holder.txt_plan.setText(plan_three);


        }
        else if (nameModels.get(position).getPlanId().equals("4")) {

            String plan_four = GlobalMethods.getString(context,R.string.plan4);
            holder.txt_plan.setText(plan_four);


        }

        expiredDate = nameModels.get(holder.getAdapterPosition()).getExpired();

        String expiredDateFormat = null;

        if(expiredDate.isEmpty())
        {

        }else{

            String[] str = expiredDate.split(" ");
            // Iterating over the string
            for (int i = 0; i < str.length; i++) {

                expiredDateFormat = str[0];
            }
        }

        SimpleDateFormat formatter11 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        currentdate = formatter11.format(date1);

        try {


            if(Objects.requireNonNull(sdf.parse(String.valueOf(expiredDateFormat))).before(sdf.parse(currentdate))){

                holder.txt_plan_expired.setVisibility(View.VISIBLE);
                holder.txt_plan_expired.setText(GlobalMethods.getString(context,R.string.plan_expired));


            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        TextView txt_plan;
        TextView txt_validity;
        LinearLayout linear_layout;
        TextView txt_plan_expired;


        public MyViewholder(View itemView) {
            super(itemView);
            txt_plan = (TextView) itemView.findViewById(R.id.txt_plan);
            txt_validity = (TextView) itemView.findViewById(R.id.txt_validity);
            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
            txt_plan_expired=itemView.findViewById(R.id.txt_plan_expired);

        }
    }
}
