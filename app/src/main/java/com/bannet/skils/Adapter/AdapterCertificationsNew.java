package com.bannet.skils.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.profile.responce.CertificateList;
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


public class AdapterCertificationsNew extends RecyclerView.Adapter<AdapterCertificationsNew.MyViewholder> {

    Context context;
    ItemClickListener itemClickListener;
    List<CertificateList.Certificate> nameModels;
    String USER_ID;
    ArrayList<String> checked_certificates_id = new ArrayList<String>();
    ArrayList<String> checked_certificates_name = new ArrayList<String>();
    public Boolean select=false;
    ArrayList<String> checked_id = new ArrayList<>();

    public interface ItemClickListener{
        public void ItemClick(String id, String certification_name);
    }

    public AdapterCertificationsNew(Context context, ItemClickListener itemClickListener, List<CertificateList.Certificate> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {


        USER_ID = PrefConnect.readString(context,PrefConnect.USER_ID,"");
//        getprofile(USER_ID,nameModels.get(position).getCertificateName(),holder.tick_selected);
        holder.txt_value.setText(nameModels.get(position).getCertificateName());

        if(nameModels.get(holder.getAdapterPosition()).getSelected() !=null){

            if(nameModels.get(holder.getAdapterPosition()).getSelected()){

                holder.tick_selected.setVisibility(View.VISIBLE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!select){

                    holder.tick_selected.setVisibility(View.VISIBLE);
                    checked_certificates_id.add(nameModels.get(position).getId());
                    checked_certificates_name.add(nameModels.get(position).getCertificateName());
                    String formattedString = checked_certificates_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim(); Log.e("selected cer",formattedString +"test");
                    String formattedname = checked_certificates_name.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim(); Log.e("selected cer",formattedString +"test");

                    itemClickListener.ItemClick(formattedString,formattedname);
                    select = true;

                }
                else {

                    holder.tick_selected.setVisibility(View.GONE);
                    checked_certificates_id.remove(nameModels.get(position).getId());
                    checked_certificates_name.remove(nameModels.get(position).getCertificateName());
                    String formattedString = checked_certificates_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim(); Log.e("selected cer",formattedString +"test");

                    String formattedname = checked_certificates_name.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim(); Log.e("selected cer",formattedString +"test");

                    itemClickListener.ItemClick(formattedString,formattedname);

                    select = false;

                }
            }
        });

        holder.tick_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!select){

                    holder.tick_selected.setVisibility(View.VISIBLE);
                    checked_certificates_id.add(nameModels.get(position).getId());
                    checked_certificates_name.add(nameModels.get(position).getCertificateName());
                    String formattedString = checked_certificates_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim(); Log.e("selected cer",formattedString +"test");
                    String formattedname = checked_certificates_name.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim(); Log.e("selected cer",formattedString +"test");

                    itemClickListener.ItemClick(formattedString,formattedname);
                    select = true;

                }
                else {

                    holder.tick_selected.setVisibility(View.GONE);
                    checked_certificates_id.remove(nameModels.get(position).getId());
                    checked_certificates_name.remove(nameModels.get(position).getCertificateName());
                    String formattedString = checked_certificates_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim(); Log.e("selected cer",formattedString +"test");

                    String formattedname = checked_certificates_name.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim(); Log.e("selected cer",formattedString +"test");

                    itemClickListener.ItemClick(formattedString,formattedname);

                    select = false;

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        TextView txt_value;
        ImageView tick_selected;
        LinearLayout linear_layout;

        public MyViewholder(View itemView) {
            super(itemView);
            txt_value =  itemView.findViewById(R.id.txt_value);
            linear_layout =  itemView.findViewById(R.id.linear_layout);
            tick_selected =  itemView.findViewById(R.id.tick_selected);

        }
    }

    private void getprofile(String user_id, String id, ImageView check) {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().getprofile(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<getprofileModel>() {
                @Override
                public void onResponse(Call<getprofileModel> call, Response<getprofileModel> response) {

                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("1")){

                            String text = response.body().getDetails().getCertification();

                            String[] arrSplit = text.split(", ");

                            for (int i=0; i < arrSplit.length; i++) {

                                String imageName=arrSplit[i];
                                checked_id.add(imageName);

                            }

                            for(int j=0; j<checked_id.size();j++){
                                if(checked_id.get(j).equals(id)){
                                    check.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<getprofileModel> call, Throwable t) {
                    //  progressDialog.dismiss();
                }
            });
        }
        else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }
}
