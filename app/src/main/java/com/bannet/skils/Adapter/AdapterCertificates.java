package com.bannet.skils.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.profile.responce.CertificateList;
import com.bannet.skils.R;

import java.util.ArrayList;
import java.util.List;


public class AdapterCertificates extends RecyclerView.Adapter<AdapterCertificates.MyViewholder> {

    Context context;
    ItemClickListener itemClickListener;
    List<CertificateList.Certificate> nameModels;
    int row_index;
    String USER_ID;
    ArrayList<String> checked_certificates_id = new ArrayList<String>();

    public interface ItemClickListener{
        public void ItemClick(String id);
    }

    public AdapterCertificates(Context context, ItemClickListener itemClickListener, List<CertificateList.Certificate> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.certificate_recycler,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {




        holder.checkbox_certificate.setText(nameModels.get(position).getCertificateName());
        holder.checkbox_certificate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    checked_certificates_id.add(nameModels.get(position).getId());
                    String formattedString = checked_certificates_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim(); Log.e("selected cer",formattedString +"test");
                            itemClickListener.ItemClick(formattedString);
                }else {
                    checked_certificates_id.remove(nameModels.get(position).getId());
                    String formattedString = checked_certificates_id.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim(); Log.e("selected cer",formattedString +"test");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        CheckBox checkbox_certificate;
        LinearLayout linear_layout;

        public MyViewholder(View itemView) {
            super(itemView);
            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
            checkbox_certificate = (CheckBox) itemView.findViewById(R.id.checkbox_certificate);

        }
    }
}
