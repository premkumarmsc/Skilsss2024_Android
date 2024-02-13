package com.bannet.skils.Activitys.fragment.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;


import com.bannet.skils.R;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.chat.activity.ActivityChatDetails;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.post.responce.PostinglistModel;
import com.bannet.skils.postingdetails.activity.ActivityPostingsDetailsScreen;
import com.bumptech.glide.Glide;


import java.util.LinkedList;
import java.util.List;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    public String images_list;
    private List<PostinglistModel.Posting> Filterpostlist;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;
    public String USER_ID;

    String status;
    public PaginationAdapter(Context context) {
        this.context = context;
        Filterpostlist = new LinkedList<>();
    }

    public void setMovieList(List<PostinglistModel.Posting> movieList) {
        this.Filterpostlist = movieList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (ITEM) {

            case ITEM:
                View viewItem = inflater.inflate(R.layout.activity_home_layout_two_new, parent, false);
                viewHolder = new MyViewholder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.layout_progress_bar, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder11, int position) {

         PostinglistModel.Posting subCategoryResponse = Filterpostlist.get(position);
         switch (ITEM) {
               case ITEM:


                   Log.e("position", String.valueOf(position));
                          MyViewholder holder = (MyViewholder) holder11;

                          images_list = Filterpostlist.get(holder.getAdapterPosition()).getPostImages();
                          USER_ID = PrefConnect.readString(context, PrefConnect.USER_ID, "");
                          if (images_list == null) {
                              holder.img_profession.setImageResource(R.drawable.select_skil_image);

                          } else {
                              String[] str = images_list.split(",");
                              // Iterating over the string
                              for (int i = 0; i < str.length; i++) {
                                  // Printing the elements of String array;
                                  Glide.with(context).load(Filterpostlist.get(holder.getAdapterPosition()).getPostImageUrl() + str[0]).into(holder.img_profession);
                              }
                          }
                          holder.txt_name.setText(Filterpostlist.get(holder.getAdapterPosition()).getTitle());
                          holder.txt_skills.setText(Filterpostlist.get(holder.getAdapterPosition()).getSkillsName());
                          holder.txt_created_at.setText(Filterpostlist.get(position).getCreatedAt());
                          holder.linear_layout.setOnClickListener(v -> {
                              Intent in = new Intent(context, ActivityPostingsDetailsScreen.class);
                              in.putExtra("post_id", Filterpostlist.get(holder.getAdapterPosition()).getId());
                              in.putExtra("post_user_id", Filterpostlist.get(holder.getAdapterPosition()).getUserId());
                              in.putExtra("notification_id", "");
                              in.putExtra("type", "1");
                              in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                              context.startActivity(in);
                          });
                          holder.img_chat_postings_new.setOnClickListener(view -> {

                              if(USER_ID.equals("customer")){

                                  userLogin();

                              }
                              else {
                                  if (!USER_ID.equalsIgnoreCase(Filterpostlist.get(holder.getAdapterPosition()).getUserId())) {

                                      Intent in = new Intent(context, ActivityChatDetails.class);
                                      in.putExtra("other_user_id", Filterpostlist.get(holder.getAdapterPosition()).getUserId());
                                      in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                      context.startActivity(in);
                                  }
                              }



                          });


                    break;
                    case LOADING:
                    LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder11;
                    loadingViewHolder.progressBar.setVisibility(View.GONE);
                    break;
         }
    }

    private void userLogin() {

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_not_logon_dialog);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialoAnimasion;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


        TextView title = dialog.findViewById(R.id.title);
        AppCompatButton login = dialog.findViewById(R.id.login_btn);

        title.setText(GlobalMethods.getString(context,R.string.please_login));
        login.setText(GlobalMethods.getString(context,R.string.go_to_login));

        login.setOnClickListener(view -> {

           Intent intent = new Intent(context, ActivityPhonenumberScreen.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
           context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return Filterpostlist == null ? 0 : Filterpostlist.size()-1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == Filterpostlist.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new PostinglistModel.Posting());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = Filterpostlist.size() - 1;
        PostinglistModel.Posting result = getItem(position);

        if (result != null) {
            Filterpostlist.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(PostinglistModel.Posting movie) {
        Filterpostlist.add(movie);
        notifyItemInserted(Filterpostlist.size() - 1);
    }

    public void addAll(List<PostinglistModel.Posting> moveResults) {

        Log.d("page","adaptersize "+moveResults.size());
        for (PostinglistModel.Posting result : moveResults) {
            add(result);
        }
    }
    public void ClearAll() {
        Filterpostlist.clear();
        notifyDataSetChanged();
    }



    public PostinglistModel.Posting getItem(int position) {
        return Filterpostlist.get(position);
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
    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);

        }
    }


}