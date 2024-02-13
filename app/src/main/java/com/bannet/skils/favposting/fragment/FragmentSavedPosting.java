package com.bannet.skils.favposting.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bannet.skils.Adapter.AdapterViewPagerStatic;
import com.bannet.skils.Adapter.SliderAdapter;
import com.bannet.skils.Model.WebModel.AdvertismentModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.databinding.FragmentSavedPostingBinding;
import com.bannet.skils.favorite.fragment.FragmentFavusers;
import com.bannet.skils.home.responce.ViewPageImage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentSavedPosting extends Fragment {

    public FragmentSavedPostingBinding binding;
    public View view;
    public String USER_ID;
    public int currentPage = 1;
    private Context context;
    public String latitude,logitude;
    int viewPageImages = 0;
    SliderAdapter sliderAdapter;
    AdapterViewPagerStatic adapterViewPagerStatic;
    List<ViewPageImage> viewPageImagesstatic;

    public static FragmentSavedPosting newInstance() {
        return new FragmentSavedPosting();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        binding = FragmentSavedPostingBinding.inflate(getLayoutInflater());
        context = getActivity();
        view = binding.getRoot();

        init();
        return view;

    }

    private void init(){

        replaceFragment(new FragmentFavusers());

        binding.userBtn.setText(GlobalMethods.getString(context,R.string.users));
        binding.postingBtn.setText(GlobalMethods.getString(context,R.string.postings));

        binding.userBtn.setOnClickListener(view1 -> {

            currentPage = 1;
            binding.userBtn.setBackgroundResource(R.drawable.home_layout_bg);
            binding.postingBtn.setBackgroundResource(0);
            replaceFragment(new FragmentFavusers());

        });

        binding.postingBtn.setOnClickListener(view1 -> {

            currentPage = 2;
            binding.postingBtn.setBackgroundResource(R.drawable.home_layout_bg);
            binding.userBtn.setBackgroundResource(0);
            replaceFragment(new FragmentFavPostings());
        });

        latitude = PrefConnect.readString(context, PrefConnect.LATITUDE, "");
        logitude = PrefConnect.readString(context, PrefConnect.LOGITUDE, "");

        getAdvertisment(latitude,logitude);

    }

    public void getAdvertisment(String latitude,String longitude) {

        if (GlobalMethods.isNetworkAvailable(context)) {
            Random random = new Random();
            Api.getClient().getAdsList(latitude, longitude, PrefConnect.readString(context, PrefConnect.LANGUAGE_RESPONCE, "")).enqueue(new Callback<AdvertismentModel>() {
                @Override
                public void onResponse(@NonNull Call<AdvertismentModel> call, @NonNull Response<AdvertismentModel> response) {

                    Log.e("res", new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus().equals("1")) {

                            viewPageImages = response.body().getAdsList().size();

                            sliderAdapter = new SliderAdapter(context, response.body().getAdsList());
                            binding.viewPager.setSliderAdapter(sliderAdapter);
//                            sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                            binding.viewPager.startAutoCycle();

                        } else {

                            getBannerList();

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AdvertismentModel> call, @NonNull Throwable t) {

                }
            });

        } else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context, R.string.No_Internet_Connection));

        }
    }

    public void getBannerList(){
        viewPageImagesstatic = new ArrayList<>();
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.textimaagr_electrical));
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.image));
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.textimaagr_electrical));
        viewPageImagesstatic.add(new ViewPageImage(R.drawable.image));

        adapterViewPagerStatic = new AdapterViewPagerStatic(context, viewPageImagesstatic);
        binding.viewPager.setSliderAdapter(adapterViewPagerStatic);
//      sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        binding.viewPager.startAutoCycle();


    }


    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framLayout_home,fragment);
        fragmentTransaction.commit();
    }
}