package com.bannet.skils.viewallpostings.activity;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bannet.skils.Activitys.fragment.adapter.PaginationAdapter;
import com.bannet.skils.Adapter.AdapterViewPagerStatic;
import com.bannet.skils.Adapter.SliderAdapter;
import com.bannet.skils.Model.WebModel.AdvertismentModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.ApiPage;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.home.responce.ViewPageImage;
import com.bannet.skils.pagination.PaginationScrollListener;
import com.bannet.skils.post.responce.PostinglistModel;
import com.bannet.skils.viewallpostings.adapter.AdapterViewAllPosting;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.SliderView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewAllPostings extends AppCompatActivity {

    public Context context;
    public ProgressDialog progressDialog;
    private static final int PAGE_START = 1;
    public boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    public int currentPage = PAGE_START;
    public String longitude,lattitude;
    public String search_text,status="";
    public PaginationScrollListener paginationScrollListener;
    PaginationAdapter paginationAdapter;
    public EditText search;
    public String querytext = "";
    public RelativeLayout searchBtn;
    public GpsMyLocationProvider provider;
    int viewPageImages = 0;
    public ImageView backBtn,backBtn1;
    SliderView sliderView;
    SliderAdapter sliderAdapter;
    AdapterViewPagerStatic adapterViewPagerStatic;
    List<ViewPageImage> viewPageImagesstatic;
    public TextView p1;
    private ArrayList<PostinglistModel.Posting> userModalArrayList;
    private AdapterViewAllPosting userRVAdapter;
    private RecyclerView userRV;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    int page = 1, limit = 2;
    public String Skey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_postings);

        context = ActivityViewAllPostings.this;

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        init();

    }

    private void init(){

        search = findViewById(R.id.edi_search);
        searchBtn = findViewById(R.id.search_btn);
        sliderView = findViewById(R.id.view_pager);
        backBtn = findViewById(R.id.back_btn);
        backBtn1 = findViewById(R.id.back_image);
        p1 = findViewById(R.id.p1);

        p1.setText(GlobalMethods.getString(context,R.string.posts_around_you));
        search.setHint(GlobalMethods.getString(context,R.string.search_with_skills_and_name));

        userModalArrayList = new ArrayList<>();

        // initializing our views.
        userRV = findViewById(R.id.proff_list);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);

        searchBtn.setOnClickListener(view -> {

            if(status.equals("1")){

                String query;

                query = search.getText().toString();

                if(query.isEmpty()){

                    querytext="";
                    Skey = "";
                    currentPage=1;
                    userRVAdapter.ClearAll();
                    getPostList(lattitude,longitude,"");

                }
                else {
                    querytext=query;
                    Skey = query;
                    page=1;
                    userRVAdapter.ClearAll();
                    getPostList(lattitude,longitude,query);
                    Log.e("nottemp...","empty");
                }

            }

        });

        longitude = PrefConnect.readString(context,PrefConnect.LOGITUDE,"");
        lattitude = PrefConnect.readString(context,PrefConnect.LATITUDE,"");

        String key = getIntent().getStringExtra("key");

        if(key.equals(" ")){

            if(longitude.isEmpty()){
                Skey = "";
                getCurrentLocation("");

            }
            else {
                Skey = "";
                getPostList(lattitude,longitude,"");
                getAdvertisment(lattitude,longitude);

            }
        }
        else {

            if(longitude.isEmpty()){

                Skey = key;
                getCurrentLocation(key);

            }
            else {
                Skey = key;
                getPostList(lattitude,longitude,key);
                getAdvertisment(lattitude,longitude);

            }
        }

        backBtn.setOnClickListener(view -> finish());
        backBtn1.setOnClickListener(view -> finish());

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
                    getPostList(lattitude,longitude,Skey);
                }
            }
        });

    }

    public  void getCurrentLocation(String key) {

        provider = new GpsMyLocationProvider(context);
        provider.setLocationUpdateMinDistance(0);
        provider.setLocationUpdateMinTime(0);
        provider.startLocationProvider((location, source) -> {

            provider.stopLocationProvider();
            Log.e("Latitude : ", location.getLatitude() + "\nLongitude : " + location.getLongitude() + "\nAccuracy : " + location.getAccuracy());

            lattitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";

            if(key.equals("")){

                getPostList(lattitude,longitude,"");

            }
            else {

                getPostList(lattitude,longitude,key);
            }


            getAdvertisment(lattitude,longitude);

        });

    }

    public void getPostList(String lati,String logi,String query){
        Log.e("lati",lati);
        Log.e("logi",logi);
        Log.e("query",query);
        search_text = search.getText().toString()+"";
        if (GlobalMethods.isNetworkAvailable(context)) {
            ApiPage.getClient().postingslist(lati,logi,"",query,PrefConnect.readString(context, PrefConnect.LANGUAGE_RESPONCE, ""), String.valueOf(page)).enqueue(new Callback<PostinglistModel>() {
                @Override
                public void onResponse(@NonNull Call<PostinglistModel> call, @NonNull Response<PostinglistModel> response) {
                    Log.e("swruweyfe",new Gson().toJson(response.body()));

                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            status = response.body().getStatus();

                            int totalCount = response.body().getTotal_count();

                            userModalArrayList.addAll(response.body().getPostingList());
                            if(totalCount <=10){
                                loadingPB.setVisibility(View.GONE);
                            }

                            // passing array list to our adapter class.
                            userRVAdapter = new AdapterViewAllPosting(context,userModalArrayList);

                            // setting layout manager to our recycler view.
                            userRV.setLayoutManager(new LinearLayoutManager(context));

                            // setting adapter to our recycler view.
                            userRV.setAdapter(userRVAdapter);

                        }
                        else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                            loadingPB.setVisibility(View.GONE);
                            status=response.body().getStatus();

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PostinglistModel> call, @NonNull Throwable t) {

                    Log.e("errr",t.getMessage());

                }
            });

        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }

    public void getAdvertisment(String latitude,String longitude) {

        if (GlobalMethods.isNetworkAvailable(context)) {
            Api.getClient().getAdsList(latitude, longitude, PrefConnect.readString(context, PrefConnect.LANGUAGE_RESPONCE, "")).enqueue(new Callback<AdvertismentModel>() {
                @Override
                public void onResponse(@NonNull Call<AdvertismentModel> call, @NonNull Response<AdvertismentModel> response) {

                    Log.e("res", new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus().equals("1")) {

                            viewPageImages = response.body().getAdsList().size();

                            sliderAdapter = new SliderAdapter(context, response.body().getAdsList());
                            sliderView.setSliderAdapter(sliderAdapter);
//                            sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                            sliderView.startAutoCycle();

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
        sliderView.setSliderAdapter(adapterViewPagerStatic);
//      sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();


    }

}