package com.bannet.skils.Adapter;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bannet.skils.addposting.activity.ActivityAddPosting;
import com.bannet.skils.postingdetails.activity.ActivityPostingsDetailsScreen;
import com.bannet.skils.myposting.responce.MypostingListModel;
import com.bannet.skils.R;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import java.util.List;


public class AdapterMyPosingList extends RecyclerView.Adapter<AdapterMyPosingList.MyViewholder>{

    Context context;
    ItemClickListener itemClickListener;
    List<MypostingListModel.Posting> nameModels;
    String USER_ID;
    String images_list;
    Resources resources;

    public AdapterMyPosingList(Context context, List<MypostingListModel.Posting> nameModels, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
    }

    public interface ItemClickListener{

        void ItemClick(MypostingListModel.Posting posting, View view);
    }


    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myposting_single_page,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");
        holder.txt_skils.setText(nameModels.get(holder.getAdapterPosition()).getSkillsName());

        holder.header.setText(nameModels.get(holder.getAdapterPosition()).getTitle());
        holder.sub_header.setText(nameModels.get(holder.getAdapterPosition()).getDescription());

        changeLanguagedialog(holder.skill);

        images_list = nameModels.get(holder.getAdapterPosition()).getPostImages();

        if(images_list.isEmpty()){

            holder.post_image.setImageResource(R.drawable.select_skil_image);

        }
        else{

            String[] str = images_list.split(",");

            for (int i = 0; i < str.length; i++) {

                Glide.with(context).load(nameModels.get(holder.getAdapterPosition()).getPostImageUrl()+str[0]).into(holder.post_image);

            }

        }
        holder.itemView.setOnClickListener(v -> {

            Intent in = new Intent(context, ActivityPostingsDetailsScreen.class);
            in.putExtra("post_id",nameModels.get(holder.getAdapterPosition()).getId());
            in.putExtra("post_user_id", nameModels.get(holder.getAdapterPosition()).getUserId());
            in.putExtra("notification_id","");
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);

        });

        holder.editPost.setOnClickListener(view -> {

            Intent in=new Intent(context, ActivityAddPosting.class);
            in.putExtra("type","edit_post");
            in.putExtra("post_id",nameModels.get(holder.getAdapterPosition()).getId());
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);

        });

        holder.detetedPost.setOnClickListener(view -> itemClickListener.ItemClick(nameModels.get(holder.getAdapterPosition()),holder.itemView));
    }



    @Override
    public int getItemCount() {
        return nameModels.size();
    }


    public static class MyViewholder extends RecyclerView.ViewHolder {


        TextView txt_skils,skill,header,sub_header;
        ImageView detetedPost;
        AppCompatButton editPost;
        ImageView post_image;

        public MyViewholder(View itemView) {
            super(itemView);

            skill=itemView.findViewById(R.id.skill);
            txt_skils=itemView.findViewById(R.id.txt_skils);
            editPost=itemView.findViewById(R.id.txt_edit_post);
            detetedPost=itemView.findViewById(R.id.txt_delete_post);
            post_image=itemView.findViewById(R.id.carviewimage_post_image);
            header = itemView.findViewById(R.id.header);
            sub_header = itemView.findViewById(R.id.sub_header);

        }
    }

    private void changeLanguagedialog(TextView skill) {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();


            skill.setText(resources.getText(R.string.skills));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));



        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            skill.setText(resources.getText(R.string.skills));

        }
    }
}
