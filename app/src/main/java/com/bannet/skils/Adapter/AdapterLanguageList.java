package com.bannet.skils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.language.responce.SelectLanguageModel;
import com.bannet.skils.R;

import java.util.List;

public class AdapterLanguageList extends RecyclerView.Adapter<AdapterLanguageList.MyViewholder>{

    int row_index=-1;
    Context context;
    AdapterLanguageList.ItemClickListener itemClickListener;
    List<SelectLanguageModel.Language> nameModels;


    public AdapterLanguageList(Context context, ItemClickListener itemClickListener, List<SelectLanguageModel.Language> nameModels) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    public interface ItemClickListener{
        public void ItemClick(String position);
    }
    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutlanguage,parent,false);

        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

        holder.language.setText(nameModels.get(position).getLanguage());
        holder.language.setChecked(position == row_index);

        holder.language.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(b){



                    itemClickListener.ItemClick(holder.language.getText().toString());

                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        RadioButton language;

        public MyViewholder(View itemView) {
            super(itemView);
            language=itemView.findViewById(R.id.language_list);

        }
    }
}
