package com.bannet.skils.hire.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.fragments.FragmentCategoryProffesional;
import com.bannet.skils.subcategoryhome.adapter.AdapterSubCategory;
import com.bannet.skils.subcategoryhome.responce.SubcategoryResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentHire extends Fragment {

    public Context context;
    public AppCompatButton userBtn,postingBtn;
    public RecyclerView subCategoryList;
    public ProgressDialog progressDialog;
    public ImageView backBtn;
    public String catogeryID,skilssId="";
    public View view;
    private ArrayList<SubcategoryResponse.Skill> userModalArrayList;
    public AdapterSubCategory.itemClickLissener itemClickListenerskills;
    private AdapterSubCategory userRVAdapter;
    int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_hire, container, false);

        context = getActivity();
        init(view);
        return view;

    }


    private void init(View view) {

        userBtn = view.findViewById(R.id.user_btn);
        postingBtn = view.findViewById(R.id.posting_btn);
        subCategoryList = view.findViewById(R.id.subcatlist);
        backBtn = view.findViewById(R.id.back_btn);

        catogeryID = "37";
        userModalArrayList = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putString("cat_id", catogeryID);
        bundle.putString("skill_id", skilssId);
        bundle.putString("type", "hire");
        FragmentCategoryProffesional fragobj = new FragmentCategoryProffesional();
        fragobj.setArguments(bundle);
        replaceFragment(fragobj);

        subCatgorylList(page, catogeryID);

        itemClickListenerskills = skilssId -> {

                Bundle bundle1 = new Bundle();
                bundle1.putString("cat_id", catogeryID);
                bundle1.putString("skill_id", skilssId);
                bundle1.putString("type", "hire");
                FragmentCategoryProffesional fragobj1 = new FragmentCategoryProffesional();
                fragobj1.setArguments(bundle1);
                replaceFragment(fragobj1);

            Log.e("skilssId", skilssId);
        };
    }


    private void subCatgorylList(int page,String catId) {

        if(GlobalMethods.isNetworkAvailable(context)){
            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            Api.getClient().subcategoryList(catId, PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""), String.valueOf(page),"").enqueue(new Callback<SubcategoryResponse>() {
                @Override
                public void onResponse(@NonNull Call<SubcategoryResponse> call, @NonNull Response<SubcategoryResponse> response) {
                    progressDialog.dismiss();
                    Log.e("mycatery",new Gson().toJson(response.body()));

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        userModalArrayList.addAll(response.body().getSkillList());

                        // passing array list to our adapter class.
                        userRVAdapter = new AdapterSubCategory(context,userModalArrayList,itemClickListenerskills);

                        // setting layout manager to our recycler view.
                        subCategoryList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

                        // setting adapter to our recycler view.
                        subCategoryList.setAdapter(userRVAdapter);



                    }else {

                        GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<SubcategoryResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();

                    Log.e("failure professional res",t.getMessage());
                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framLayout_home,fragment);
        fragmentTransaction.commit();
    }
}