package com.bannet.skils.fragments;

import static java.lang.Math.abs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bannet.skils.R;
import com.bannet.skils.service.ApiPage;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.professinoldetails.activity.ActivityProffDetails;
import com.bannet.skils.professinoldetails.activity.ActivityWithoutimage;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import com.bannet.skils.viewallproffessionals.adapter.AdapterViewAllProff;
import com.google.gson.Gson;

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentCategoryProffesional extends Fragment {

    public Context context;
    public ProgressDialog progressDialog;
    public View view;
    public RecyclerView proffesionalList;
    public String longitude,lattitude,USER_ID,cat_id,skils_id;
    private ArrayList<ProfessionalList.Professional> userModalArrayList;
    private AdapterViewAllProff userRVAdapter;
    private AdapterViewAllProff.itemClikcLissner itemClikcLissner;
    private RecyclerView userRV;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    int page = 1, limit = 2;
    public String key = "";
    public String type = "";

    public GpsMyLocationProvider provider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_category_proffesional, container, false);

        context = getActivity();

        init(view);


        return view;

    }

    private void init(View view){

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");

        longitude=PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude=PrefConnect.readString(context,PrefConnect.LATITUDE,"");

        userModalArrayList = new ArrayList<>();

        // initializing our views.
        userRV = view.findViewById(R.id.proff_list);
        loadingPB = view.findViewById(R.id.idPBLoading);
        nestedSV = view.findViewById(R.id.idNestedSV);

        if(getArguments() != null){

            cat_id = getArguments().getString("cat_id");
            skils_id = getArguments().getString("skill_id");
            type = getArguments().getString("type");
            Log.e("type",type);
            if(longitude.isEmpty()){

                getCurrentLocation();

            }
            else {

                getProfessionalList(lattitude,longitude,cat_id,skils_id);

            }
        }
        else {


            if(longitude.isEmpty()){

                getCurrentLocation();

            }
            else {

                getProfessionalList(lattitude,longitude,cat_id,skils_id);

            }

        }

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    Log.e("pageeee",page+"");
                    loadingPB.setVisibility(View.VISIBLE);
                    getProfessionalList(lattitude,longitude,cat_id,skils_id);
                }
            }
        });

        itemClikcLissner = professional -> {

            if(cat_id.equals("37")){

                if(professional.getBannerImage().equals("") || professional.getBannerImage().isEmpty()){



                    Intent in = new Intent(context, ActivityWithoutimage.class);
                    in.putExtra("profe_id",professional.getId());
                    in.putExtra("profileurl",professional.getImageUrl());
                    in.putExtra("profile_name",professional.getImageName());
                    in.putExtra("type","1");
                    in.putExtra("typehire","hire");
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }
                else {
                    Intent in = new Intent(context, ActivityProffDetails.class);
                    in.putExtra("profe_id",professional.getId());
                    in.putExtra("profileurl",professional.getImageUrl());
                    in.putExtra("profile_name",professional.getImageName());
                    in.putExtra("latitude",professional.getLatitude());
                    in.putExtra("logitude",professional.getLongitude());
                    in.putExtra("type","1");
                    in.putExtra("typehire","hire");
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }

            }
            else {

                if(professional.getBannerImage().equals("") || professional.getBannerImage().isEmpty()){



                    Intent in = new Intent(context, ActivityWithoutimage.class);
                    in.putExtra("profe_id",professional.getId());
                    in.putExtra("profileurl",professional.getImageUrl());
                    in.putExtra("profile_name",professional.getImageName());
                    in.putExtra("type","1");
                    in.putExtra("typehire",type);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }
                else {
                    Intent in = new Intent(context, ActivityProffDetails.class);
                    in.putExtra("profe_id",professional.getId());
                    in.putExtra("profileurl",professional.getImageUrl());
                    in.putExtra("profile_name",professional.getImageName());
                    in.putExtra("latitude",professional.getLatitude());
                    in.putExtra("logitude",professional.getLongitude());
                    in.putExtra("type","1");
                    in.putExtra("typehire",type);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }
            }


        };

    }

    public  void getCurrentLocation( ) {

        provider = new GpsMyLocationProvider(context);
        provider.setLocationUpdateMinDistance(0);
        provider.setLocationUpdateMinTime(0);
        provider.startLocationProvider((location, source) -> {

            provider.stopLocationProvider();
            Log.e("Latitude : ", location.getLatitude() + "\nLongitude : " + location.getLongitude() + "\nAccuracy : " + location.getAccuracy());

            lattitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";

            getProfessionalList(lattitude,longitude,cat_id,skils_id);

        });

    }


    public void getProfessionalList(String lati,String logi,String catid,String skilId){

        Log.e("lati",lati);
        Log.e("logi",logi);
        Log.e("catid",catid);
        Log.e("skilId",skilId);

        if(GlobalMethods.isNetworkAvailable(context)){

           // progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            ApiPage.getClient().catProff(lati,logi, PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""),catid,skilId,"", String.valueOf(page)).enqueue(new Callback<ProfessionalList>() {
                @Override
                public void onResponse(@NonNull Call<ProfessionalList> call, @NonNull Response<ProfessionalList> response) {
                    //progressDialog.dismiss();
                    if(response.isSuccessful()){
                        Log.e("sucess",new Gson().toJson(response.body()));

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            int totalCount = response.body().getTotal_count();

                            userModalArrayList.addAll(response.body().getProfessionalList());
                            if(totalCount <=10){
                                loadingPB.setVisibility(View.GONE);
                            }

                            // passing array list to our adapter class.
                            userRVAdapter = new AdapterViewAllProff(context,userModalArrayList,"2",itemClikcLissner);

                            // setting layout manager to our recycler view.
                            userRV.setLayoutManager(new GridLayoutManager(context,2));

                            // setting adapter to our recycler view.
                            userRV.setAdapter(userRVAdapter);

                        }
                        else {
                            loadingPB.setVisibility(View.GONE);
                           // GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProfessionalList> call, @NonNull Throwable t) {

                    //progressDialog.dismiss();

                }
            });

        }else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }


    }

}