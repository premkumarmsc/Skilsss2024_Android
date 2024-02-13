package com.bannet.skils.favorite.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bannet.skils.Adapter.AdapterFavouriteList;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.favorite.responce.FavouriteListModel;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentFavusers extends Fragment {

    View view;
    Context context;
    AdapterFavouriteList adapterFavouriteList;
    List<ProfessionalList> favlist;
    private String USER_ID,proff_id;
    AdapterFavouriteList.ItemClickListener itemClickListener;
    RecyclerView recycler_fav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_savedusers, container, false);
        context = getActivity();

        init(view);
        return view;

    }

    private  void init(View view){

        //onbackword();

        recycler_fav = view.findViewById(R.id.recycler_chat);

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");

        chatListResposne(USER_ID);



        itemClickListener = new AdapterFavouriteList.ItemClickListener() {
            @Override
            public void ItemClick(String position, View layout) {
                proff_id = position;
                callFav(USER_ID,proff_id,layout);
            }
        };


    }

    public void chatListResposne(String user_id){

        favlist = new ArrayList<>();
        if (GlobalMethods.isNetworkAvailable(context)) {


            Api.getClient().fav_professionalsList(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<FavouriteListModel>() {
                @Override
                public void onResponse(Call<FavouriteListModel> call, Response<FavouriteListModel> response) {

                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("1")){


                            adapterFavouriteList = new AdapterFavouriteList(context,itemClickListener,response.body().getPostingList());
                            recycler_fav.setHasFixedSize(true);
                            recycler_fav.setLayoutManager(new LinearLayoutManager(context));
                            recycler_fav.setAdapter(adapterFavouriteList);


                        }else {
                        }
                    }
                }

                @Override
                public void onFailure(Call<FavouriteListModel> call, Throwable t) {

                }
            });

        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }

    public void callFav(String user_id,String prof_id,View layout){
        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().addOrremoveFavProfessional(user_id,prof_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {

                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("1")){
                            layout.setVisibility(View.GONE);
                            GlobalMethods.Toast(context,response.body().getMessage());
                            chatListResposne(USER_ID);
                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }

                    }
                    else {


                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {



                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }

    private void onbackword(){

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                Intent intent = new Intent(context, ActivityBottom.class);
                startActivity(intent);
                requireActivity().finish();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback((LifecycleOwner) context, callback);

    }
}