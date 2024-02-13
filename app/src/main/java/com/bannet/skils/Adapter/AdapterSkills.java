package com.bannet.skils.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.subcategoryhome.responce.SubcategoryResponse;
import com.bumptech.glide.Glide;
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


public class AdapterSkills extends RecyclerView.Adapter<AdapterSkills.MyViewholder> implements Filterable {

    Context context;
    ItemClickListener itemClickListener;
    List<SubcategoryResponse.Skill> nameModels;
    int row_index;
    ArrayList<String> checked_skills_id = new ArrayList<String>();
    ArrayList<String> checked_skills_name = new ArrayList<>();
    public List<SubcategoryResponse.Skill> Filtercatagery = new ArrayList<>();

    ArrayList<String> checked_id = new ArrayList<>();
    View view;
    String skilll_idd;
    String iddd;

    public interface ItemClickListener{
        public void ItemClick(String id,String name);
    }

    public AdapterSkills(Context context, ItemClickListener itemClickListener, List<SubcategoryResponse.Skill> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
        this.Filtercatagery=nameModels;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_skils,parent,false);
        return new MyViewholder(view);
    }


    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        String USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");

        Glide.with(context).load(Filtercatagery.get(position).getSkillsImageUrl()+Filtercatagery.get(position).getSkillsImage()).error(R.drawable.sample_skilsss).into(holder.skil_image);
        holder.txt_skils_name.setText(Filtercatagery.get(position).getSkillName());

        getprofile(USER_ID,Filtercatagery.get(position).getId(),holder.checkbox_skils);

        holder.checkbox_skils.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()){
                    checked_skills_id.add(Filtercatagery.get(position).getId());
                    String formattedString = checked_skills_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();


                    checked_skills_name.add(Filtercatagery.get(position).getSkillName());
                    String formattedString_skilName = checked_skills_name.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();


                    itemClickListener.ItemClick(formattedString,formattedString_skilName);

                }else {
                    checked_skills_id.remove(Filtercatagery.get(position).getId());
                    String formattedString = checked_skills_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();


                    checked_skills_name.remove(Filtercatagery.get(position).getSkillName());
                    String formattedString_skilName = checked_skills_name.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();


                    itemClickListener.ItemClick(formattedString,formattedString_skilName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Filtercatagery.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    Filtercatagery = nameModels;
                } else {
                    List<SubcategoryResponse.Skill> filteredList = new ArrayList<>();
                    for (SubcategoryResponse.Skill row : nameModels) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSkillName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    Filtercatagery = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = Filtercatagery;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                nameModels = (ArrayList<SubcategoryResponse.Skill>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        CheckBox checkbox_skils,check1;
        LinearLayout linear_layout;
        ImageView skil_image;
        TextView txt_skils_name;

        public MyViewholder(View itemView) {
            super(itemView);

            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
            checkbox_skils = (CheckBox) itemView.findViewById(R.id.checkbox_skils);
            skil_image = (ImageView) itemView.findViewById(R.id.skil_image);
            txt_skils_name = (TextView) itemView.findViewById(R.id.txt_skils_name);

        }
    }

    private void getprofile(String user_id,String id,CheckBox check) {

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


                                checked_id.add(imageName);

                            }

                            for(int j=0; j<checked_id.size();j++){
                                if(checked_id.get(j).equals(id)){
                                    check.setChecked(true);
                                }
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
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }
}
