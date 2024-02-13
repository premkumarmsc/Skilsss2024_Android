package com.bannet.skils.addskilss.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bannet.skils.R;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bannet.skils.subcategoryhome.responce.SubcategoryResponse;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import com.bannet.skils.service.Api;

public class AdapterSkillsPagenassion extends RecyclerView.Adapter<AdapterSkillsPagenassion.MyViewholder>{

    public List<SubcategoryResponse.Skill> subCategoryResponse;
    public AdapterSkillsPagenassion.ItemClickListener itemClickListener;
    private Context context;
    public ArrayList<String> checked_skills_id = new ArrayList<>();
    public ArrayList<String> checked_skills_name = new ArrayList<>();

    public ArrayList<String> checked_id = new ArrayList<>();
    public String USER_ID,SKILS_ID;

    public AdapterSkillsPagenassion(ArrayList<SubcategoryResponse.Skill> subCategoryResponse, AdapterSkillsPagenassion.ItemClickListener itemClickListener, Context context) {
        this.subCategoryResponse = subCategoryResponse;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    public interface ItemClickListener{
        public void ItemClick(String id,String name);
    }

    @NonNull
    @Override
    public AdapterSkillsPagenassion.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_skils, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSkillsPagenassion.MyViewholder holder, int position) {

        SKILS_ID = PrefConnect.readString(context,PrefConnect.SKILSS_ID,"");

        String[] arrSplit = SKILS_ID.split(", ");
        for (int i=0; i < arrSplit.length; i++)
        {
            String imageName=arrSplit[i];
            checked_id.add(imageName);

        }

        for(int j=0; j<checked_id.size();j++){

            if(checked_id.get(j).equals(subCategoryResponse.get(position).getId())){

                subCategoryResponse.get(position).setSelected(true);
                Log.e("id",subCategoryResponse.get(position).getId());


            }


        }

        Glide.with(context).load(subCategoryResponse.get(holder.getAdapterPosition()).getSkillsImageUrl()+subCategoryResponse.get(holder.getAdapterPosition()).getSkillsImage()).into(holder.skil_image);
        USER_ID = PrefConnect.readString(context,PrefConnect.USER_ID,"");

        holder.txt_skils_name.setText(subCategoryResponse.get(holder.getAdapterPosition()).getSkillName());
        holder.checkbox_skils.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()){

                    subCategoryResponse.get(position).setSelected(true);
                    checked_skills_id.add(subCategoryResponse.get(position).getId());
                    String formattedString = checked_skills_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();

                    checked_skills_name.add(subCategoryResponse.get(position).getSkillName());
                    String formattedString_skilName = checked_skills_name.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();


                    itemClickListener.ItemClick(formattedString,formattedString_skilName);

                }else {
                    subCategoryResponse.get(position).setSelected(false);
                    checked_skills_id.remove(subCategoryResponse.get(position).getId());
                    String formattedString = checked_skills_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();


                    checked_skills_name.remove(subCategoryResponse.get(position).getSkillName());
                    String formattedString_skilName = checked_skills_name.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();


                    itemClickListener.ItemClick(formattedString,formattedString_skilName);
                }
            }
        });

        Log.e("iddd", String.valueOf(subCategoryResponse.get(position).isSelected()));

        if(subCategoryResponse.get(position).isSelected()){

            holder.checkbox_skils.setChecked(true);


        }
        else {

            holder.checkbox_skils.setChecked(false);
        }


    }

    @Override
    public int getItemCount() {
        return subCategoryResponse.size();
    }

    public void ClearAll() {
        subCategoryResponse.clear();
        notifyDataSetChanged();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        CheckBox checkbox_skils,check1;
        LinearLayout linear_layout;
        ImageView skil_image;
        TextView txt_skils_name;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
            checkbox_skils = (CheckBox) itemView.findViewById(R.id.checkbox_skils);
            skil_image = (ImageView) itemView.findViewById(R.id.skil_image);
            txt_skils_name = (TextView) itemView.findViewById(R.id.txt_skils_name);
        }
    }


}
