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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bannet.skils.subcategoryhome.responce.SubcategoryResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRVAdapter extends RecyclerView.Adapter<UserRVAdapter.ViewHolder>{

    private ArrayList<SubcategoryResponse.Skill> subCategoryResponse;
    public UserRVAdapter.ItemClickListener itemClickListener;
    private Context context;
    ArrayList<String> checked_skills_id = new ArrayList<String>();
    ArrayList<String> checked_skills_name = new ArrayList<>();

    ArrayList<String> checked_id = new ArrayList<>();
    public String USER_ID;

    public UserRVAdapter(ArrayList<SubcategoryResponse.Skill> subCategoryResponse, ItemClickListener itemClickListener, Context context) {
        this.subCategoryResponse = subCategoryResponse;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    public interface ItemClickListener{
        public void ItemClick(String id,String name);
    }

    @NonNull
    @Override
    public UserRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_skils, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRVAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(subCategoryResponse.get(holder.getAdapterPosition()).getSkillsImageUrl()+subCategoryResponse.get(holder.getAdapterPosition()).getSkillsImage()).into(holder.skil_image);
        USER_ID = PrefConnect.readString(context,PrefConnect.USER_ID,"");
       // getprofile(USER_ID,subCategoryResponse.get(holder.getAdapterPosition()).getId(),holder.checkbox_skils);
        holder.txt_skils_name.setText(subCategoryResponse.get(holder.getAdapterPosition()).getSkillName());
        holder.checkbox_skils.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()){
                    checked_skills_id.add(subCategoryResponse.get(holder.getAdapterPosition()).getId());
                    String formattedString = checked_skills_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();


                    checked_skills_name.add(subCategoryResponse.get(holder.getAdapterPosition()).getSkillName());
                    String formattedString_skilName = checked_skills_name.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();


                    itemClickListener.ItemClick(formattedString,formattedString_skilName);

                }else {
                    checked_skills_id.remove(subCategoryResponse.get(holder.getAdapterPosition()).getId());
                    String formattedString = checked_skills_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();


                    checked_skills_name.remove(subCategoryResponse.get(holder.getAdapterPosition()).getSkillName());
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
        return subCategoryResponse.size();
    }

    public void ClearAll() {
        subCategoryResponse.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        CheckBox checkbox_skils,check1;
        LinearLayout linear_layout;
        ImageView skil_image;
        TextView txt_skils_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
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

                    Log.e("failure responce",t.getMessage());
                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }

}
