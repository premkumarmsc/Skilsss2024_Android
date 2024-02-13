package com.bannet.skils.fragments;

import static java.lang.Math.abs;

import android.app.ProgressDialog;
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
import com.bannet.skils.Activitys.fragment.adapter.PaginationAdapter;
import com.bannet.skils.R;
import com.bannet.skils.service.ApiPage;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.pagination.PaginationScrollListener;
import com.bannet.skils.post.responce.PostinglistModel;
import com.google.gson.Gson;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCatogeryPostings extends Fragment {

    public Context context;
    public ProgressDialog progressDialog;
    public View view;
    public RecyclerView postList;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;
    PaginationAdapter paginationAdapter;
    PaginationScrollListener paginationScrollListener;
    String lattitude,longitude,cat_id,skils_id;

    public GpsMyLocationProvider provider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_catogery_postings, container, false);

        context = getActivity();

        init(view);

        return view;

    }

    private void init(View view){

        longitude = PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude = PrefConnect.readString(context,PrefConnect.LATITUDE,"");

        postList = view.findViewById(R.id.post_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        paginationAdapter = new PaginationAdapter(context);
        postList.setHasFixedSize(true);
        postList.setLayoutManager(linearLayoutManager);
        postList.setAdapter(paginationAdapter);

        paginationScrollListener = new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItems() {

                Log.d("nextpage","init");
                isLoading = true;
                currentPage += 1;
                genextpages(lattitude,longitude,cat_id,skils_id);

            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

        };

        postList.addOnScrollListener(paginationScrollListener);

        if(getArguments() != null){

            cat_id = getArguments().getString("cat_id");
            skils_id = getArguments().getString("skill_id");

            paginationAdapter.ClearAll();
            if(longitude.isEmpty()){

                getCurrentLocation();

            }
            else {

                getPostList(lattitude,longitude,cat_id,skils_id);

            }

        }

        else {

            if(longitude.isEmpty()){

                getCurrentLocation();

            }
            else {

                getPostList(lattitude,longitude,cat_id,skils_id);

            }
        }
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

            getPostList(lattitude,longitude,cat_id,skils_id);

        });

    }

    public void getPostList(String lati,String logi,String catid,String skilId){

        Log.e("lati",lati);
        Log.e("logi",logi);
        Log.e("catid",catid);
        Log.e("skilId",skilId);

        if (GlobalMethods.isNetworkAvailable(context)) {
            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            String currentPaged= String.valueOf(currentPage);
            ApiPage.getClient().catPosting(lati,logi, PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""),catid,skilId,"",currentPaged).enqueue(new Callback<PostinglistModel>() {
                @Override
                public void onResponse(@NonNull Call<PostinglistModel> call, @NonNull Response<PostinglistModel> response) {

                    Log.e("swruweyfe",new Gson().toJson(response.body()));
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){


                            TOTAL_PAGES=  roundUp(response.body().getTotal_count(),10);

                            if(TOTAL_PAGES<=1) {
                                isLastPage = true;
                            }

                            paginationAdapter.addAll(response.body().getPostingList());

                            if (currentPage != TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                            else isLastPage = true;

                        }
                        else {

                            GlobalMethods.Toast(context,response.body().getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PostinglistModel> call, @NonNull Throwable t) {

                    progressDialog.dismiss();

                }
            });

        }else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }

    public static int roundUp(long num, long divisor) {

        int sign = (num > 0 ? 1 : -1) * (divisor > 0 ? 1 : -1);
        return (int) (sign * (abs(num) + abs(divisor) - 1) / abs(divisor));

    }

    public void genextpages(String lati,String logi,String catid,String skilId){

        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);

        if (GlobalMethods.isNetworkAvailable(context)) {
            String currentPaged= String.valueOf(currentPage);
            ApiPage.getClient().catPosting(lati,logi, PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""),catid,skils_id,"",currentPaged).enqueue(new Callback<PostinglistModel>() {
                @Override
                public void onResponse(@NonNull Call<PostinglistModel> call, @NonNull Response<PostinglistModel> response) {
                    progressDialog.dismiss();
                    Log.e("swruweyfe",new Gson().toJson(response.body()));
                    paginationAdapter.removeLoadingFooter();
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            if(response.body().getPostingList().size()>1) {
                                paginationAdapter.addAll(response.body().getPostingList());
                            }
                            if (currentPage != TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                            else isLastPage = true;

                        }
                        else {
                            isLastPage = true;

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PostinglistModel> call, @NonNull Throwable t) {

                    progressDialog.dismiss();

                }
            });

        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }
}