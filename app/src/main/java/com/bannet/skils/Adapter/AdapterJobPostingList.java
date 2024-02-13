package com.bannet.skils.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bannet.skils.chat.activity.ActivityChatDetails;
import com.bannet.skils.postingdetails.activity.ActivityPostingsDetailsScreen;
import com.bannet.skils.post.responce.PostinglistModel;
import com.bannet.skils.R;
import com.bannet.skils.service.PrefConnect;

import java.util.ArrayList;
import java.util.List;


public class AdapterJobPostingList extends RecyclerView.Adapter<AdapterJobPostingList.MyViewholder>implements Filterable {

    Context context;
    ItemClickListener itemClickListener;
    List<PostinglistModel.Posting> nameModels;
    List<PostinglistModel.Posting> Filterpostlist;
    String USER_ID;
    String images_list;
    String status;

    public interface ItemClickListener{
        public void ItemClick(int position);
    }

    public AdapterJobPostingList(Context context, ItemClickListener itemClickListener, List<PostinglistModel.Posting> nameModels,  String status) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.nameModels = nameModels;
        this.Filterpostlist=nameModels;
        this.status=status;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_layout_two_new,parent,false);

        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        images_list = Filterpostlist.get(holder.getAdapterPosition()).getPostImages();
        USER_ID = PrefConnect.readString(context,PrefConnect.USER_ID,"");

        if(images_list.isEmpty()) {

            holder.img_profession.setImageResource(R.drawable.select_skil_image);

        }else{

            String[] str = images_list.split(",");
            // Iterating over the string
            for (int i = 0; i < str.length; i++) {
                // Printing the elements of String array;
                Glide.with(context).load(Filterpostlist.get(holder.getAdapterPosition()).getPostImageUrl()+str[0]).into(holder.img_profession);

            }
        }

//        DateFormat formatter;
//        String dt = Filterpostlist.get(position).getCreatedAt();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar c = Calendar.getInstance();
//        try {
//            c.setTime(sdf.parse(dt));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        formatter = new SimpleDateFormat("EEE, dd MMM yyyy");
//        String today = formatter.format(c.getTime());

        holder.txt_name.setText(Filterpostlist.get(holder.getAdapterPosition()).getTitle());
        holder.txt_skills.setText(Filterpostlist.get(holder.getAdapterPosition()).getSkillsName());
        holder.txt_created_at.setText(Filterpostlist.get(position).getCreatedAt());

        holder.linear_layout.setOnClickListener(v -> {

            Intent in = new Intent(context, ActivityPostingsDetailsScreen.class);
            in.putExtra("post_id",Filterpostlist.get(holder.getAdapterPosition()).getId());
            in.putExtra("post_user_id",nameModels.get(holder.getAdapterPosition()).getUserId());
            in.putExtra("notification_id","");
            in.putExtra("type","1");

            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);


        });

        holder.img_chat_postings_new.setOnClickListener(view -> {

            if(!USER_ID.equalsIgnoreCase(nameModels.get(holder.getAdapterPosition()).getUserId())){

                Intent in = new Intent(context, ActivityChatDetails.class);
                in.putExtra("other_user_id",nameModels.get(holder.getAdapterPosition()).getUserId());
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);

            }

        });
    }

    @Override
    public int getItemCount() {

        if(status.equals("1")){
            return Filterpostlist.size();
        }
        else {
            return 0;
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    Filterpostlist = nameModels;
                } else {
                    List<PostinglistModel.Posting> filteredList = new ArrayList<>();
                    for (PostinglistModel.Posting row : nameModels) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSkillsName().toLowerCase().contains(charString.toLowerCase()) || row.getSkillsName().contains(charSequence) ||
                                row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getTitle().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    if(filteredList.size() == 0){

                        Filterpostlist = filteredList;

                    }
                    else {
                        Filterpostlist = filteredList;
                    }

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = Filterpostlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Filterpostlist = (ArrayList<PostinglistModel.Posting >) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {

        ImageView img_profession;
        TextView txt_name;
        TextView txt_skills;
        RelativeLayout linear_layout;
        ImageView img_chat_postings_new;
        TextView txt_created_at;

        public MyViewholder(View itemView) {
            super(itemView);
            img_profession =  itemView.findViewById(R.id.img_profession);
            txt_name =  itemView.findViewById(R.id.txt_name);
            txt_skills =  itemView.findViewById(R.id.txt_skills);
            linear_layout =  itemView.findViewById(R.id.linear_layout);
            img_chat_postings_new =  itemView.findViewById(R.id.img_chat_postings_new);
            txt_created_at =  itemView.findViewById(R.id.txt_created_at);
        }
    }
}
