package com.bannet.skils.chat.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bannet.skils.Adapter.AdapterChatList;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.chat.responce.ChatListModel;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChat extends Fragment {

    public Context context;
    public View view;
    RecyclerView recycler_chat;
    AdapterChatList adapterChatList;
    AdapterChatList.ItemClickListener itemClickListener;
    ProgressDialog progressDialog;
    String USER_ID;

    TextView cl1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_chat, container, false);

        context = getActivity();

        init(view);

        return view;
    }

    private void init(View view){

        //onbackword();

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");
        recycler_chat = view.findViewById(R.id.recycler_chat);
        cl1 = view.findViewById(R.id.ch1);

        userCheck(USER_ID);
        chatListResposne(USER_ID);

        cl1.setText(GlobalMethods.getString(context,R.string.chat));

    }

    public void chatListResposne(String user_id){

        if(GlobalMethods.isNetworkAvailable(context)){

            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            Api.getClient().chatList(user_id,"en").enqueue(new Callback<ChatListModel>() {

                @Override
                public void onResponse(@NonNull Call<ChatListModel> call, @NonNull Response<ChatListModel> response) {

                    progressDialog.dismiss();

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            adapterChatList = new AdapterChatList(context,itemClickListener,response.body().getChatList());
                            recycler_chat.setHasFixedSize(true);
                            recycler_chat.setLayoutManager(new LinearLayoutManager(context));
                            recycler_chat.setAdapter(adapterChatList);
                        }
                        else {


                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ChatListModel> call, @NonNull Throwable t) {


                }
            });
        }
        else {

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

    public void userCheck(String id){

        if(GlobalMethods.isNetworkAvailable(context)){
            Api.getClient().CheckUser(id).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    Log.e("status",new Gson().toJson(response.body()));

                    if (response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){



                        }
                        else {

                            GlobalMethods.Toast(context,"Your Account has been deleted");
                            PrefConnect.writeString(context, PrefConnect.USER_ID, "");
                            PrefConnect.writeString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"");
                            Intent in = new Intent(context, ActivityPhonenumberScreen.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);


                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {


                }
            });
        }
        else {
            GlobalMethods.Toast(context, "No Internet Connection");
        }
    }


}