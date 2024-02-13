package com.bannet.skils.favposting.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bannet.skils.Adapter.AdapterMySavedPostingList;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.favposting.responce.FavpostingsListModel;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentFavPostings extends Fragment {

    View view;
    Context context;
    AdapterMySavedPostingList adapterMySavedPostingList;
    List<ProfessionalList> professionalLists;
    AdapterMySavedPostingList.ItemClickListener itemClickListener;
    String USER_ID;
    public RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_fav_postings, container, false);
        context = getActivity();

        init(view);
        return view;
    }

    private void init(View view){

        recyclerView = view.findViewById(R.id.recycler_chat);

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");
        postListResposne(USER_ID);

        itemClickListener = (post_id, img_fav,itemview) -> callFav(USER_ID, post_id.getPostId(),itemview);


    }

    public void callFav(String user_id, String post_id,View view){

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().addOrremoveFavPost(user_id,post_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                    Log.e("success response", new Gson().toJson(response.body()));

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            view.setVisibility(View.GONE);
                            postListResposne(USER_ID);

                        }
                       // GlobalMethods.Toast(context,response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {

                    Log.e("failure res",t.getMessage());
                }
            });
        }
        else {

            GlobalMethods.Toast(context, "No Internet Connection");

        }
    }

    public void postListResposne(String USER_ID){
        professionalLists = new ArrayList<>();
        if (GlobalMethods.isNetworkAvailable(context)) {

            Api.getClient().fav_postingslist(USER_ID,"en").enqueue(new Callback<FavpostingsListModel>() {
                @Override
                public void onResponse(@NonNull Call<FavpostingsListModel> call, @NonNull Response<FavpostingsListModel> response) {

                    Log.e("mysdave",new Gson().toJson(response.body()));
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            adapterMySavedPostingList = new AdapterMySavedPostingList(context,itemClickListener,response.body().getPostingList());
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(adapterMySavedPostingList);

                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FavpostingsListModel> call, @NonNull Throwable t) {

                    Log.e("failure res",t.getMessage());
                }
            });

        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }
}