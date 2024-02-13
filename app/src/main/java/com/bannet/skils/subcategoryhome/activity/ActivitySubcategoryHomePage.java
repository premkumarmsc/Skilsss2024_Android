package com.bannet.skils.subcategoryhome.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.fragments.FragmentCategoryProffesional;
import com.bannet.skils.fragments.FragmentCatogeryPostings;
import com.bannet.skils.subcategoryhome.adapter.AdapterSubCategory;
import com.bannet.skils.subcategoryhome.responce.SubcategoryResponse;
import com.google.gson.Gson;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySubcategoryHomePage extends AppCompatActivity {

    public Context context;
    public AppCompatButton userBtn,postingBtn;
    public RecyclerView subCategoryList;
    public ProgressDialog progressDialog;
    public ImageView backBtn;
    public String catogeryID,skilssId="";
    public int currentpage = 1;

    public String type;
    private ArrayList<SubcategoryResponse.Skill> userModalArrayList;
    public AdapterSubCategory.itemClickLissener itemClickListenerskills;
    private AdapterSubCategory userRVAdapter;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory_home_page);

        context = ActivitySubcategoryHomePage.this;

        if(getSupportActionBar() != null){

            getSupportActionBar().hide();

        }

        init();

    }

    private void init() {

        userBtn = findViewById(R.id.user_btn);
        postingBtn = findViewById(R.id.posting_btn);
        subCategoryList = findViewById(R.id.subcatlist);
        backBtn = findViewById(R.id.back_btn);

        catogeryID = getIntent().getStringExtra("cat_id");
        type = getIntent().getStringExtra("type");
        userModalArrayList = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putString("cat_id", catogeryID);
        bundle.putString("skill_id", skilssId);
        bundle.putString("type", type);
        FragmentCategoryProffesional fragobj = new FragmentCategoryProffesional();
        fragobj.setArguments(bundle);
        replaceFragment(fragobj);

        subCatgorylList(page, catogeryID);

//        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                // on scroll change we are checking when users scroll as bottom.
//                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
//                    // in this method we are incrementing page number,
//                    // making progress bar visible and calling get data method.
//                    page++;
//                    Log.e("pageeee",page+"");
//                    loadingPB.setVisibility(View.VISIBLE);
//                    subCatgorylList(page,"");
//                }
//            }
//        });

        userBtn.setOnClickListener(view1 -> {
            currentpage = 1;
            userBtn.setBackgroundResource(R.drawable.home_layout_bg);
            postingBtn.setBackgroundResource(0);
            userBtn.setTextColor(getResources().getColor(R.color.white));
            postingBtn.setTextColor(getResources().getColor(R.color.appcolour));
            Bundle bundle1 = new Bundle();
            bundle1.putString("cat_id", catogeryID);
            bundle1.putString("skill_id", skilssId);
            bundle1.putString("type", type);
            FragmentCategoryProffesional fragobj1 = new FragmentCategoryProffesional();
            fragobj1.setArguments(bundle1);
            replaceFragment(fragobj1);

        });

        postingBtn.setOnClickListener(view1 -> {
            currentpage = 2;
            postingBtn.setBackgroundResource(R.drawable.home_layout_bg);
            userBtn.setBackgroundResource(0);
            postingBtn.setTextColor(getResources().getColor(R.color.white));
            userBtn.setTextColor(getResources().getColor(R.color.appcolour));

            Bundle bundle2 = new Bundle();
            bundle2.putString("cat_id", catogeryID);
            bundle2.putString("skill_id", skilssId);
            FragmentCatogeryPostings fragobj2 = new FragmentCatogeryPostings();
            fragobj2.setArguments(bundle2);
            replaceFragment(fragobj2);

        });

        backBtn.setOnClickListener(view -> finish());

        itemClickListenerskills = skilssId -> {

            skilssId = skilssId;

            if (currentpage == 1) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("cat_id", catogeryID);
                bundle1.putString("skill_id", skilssId);
                bundle1.putString("type", type);
                FragmentCategoryProffesional fragobj1 = new FragmentCategoryProffesional();
                fragobj1.setArguments(bundle1);
                replaceFragment(fragobj1);

            } else {

                Bundle bundle2 = new Bundle();
                bundle2.putString("cat_id", catogeryID);
                bundle2.putString("skill_id", skilssId);
                FragmentCatogeryPostings fragobj2 = new FragmentCatogeryPostings();
                fragobj2.setArguments(bundle2);
                replaceFragment(fragobj2);
            }

            Log.e("skilssId", skilssId);
        };
    }


    private void subCatgorylList(int page,String catId) {

        if(GlobalMethods.isNetworkAvailable(context)){
            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            Api.getClient().subcategoryList(catId,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""), String.valueOf(page),"").enqueue(new Callback<SubcategoryResponse>() {
                @Override
                public void onResponse(@NonNull Call<SubcategoryResponse> call, @NonNull Response<SubcategoryResponse> response) {
                    progressDialog.dismiss();
                    Log.e("mycatery",new Gson().toJson(response.body()));

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){

                        userModalArrayList.addAll(response.body().getSkillList());

                        int totalCount = response.body().getTotalCount();


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
                public void onFailure(Call<SubcategoryResponse> call, Throwable t) {
                    progressDialog.dismiss();

                    Log.e("failure professional res",t.getMessage());
                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framLayout_home,fragment);
        fragmentTransaction.commit();
    }
}