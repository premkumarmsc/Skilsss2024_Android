package com.bannet.skils.profile.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.addskilss.activity.ActivityAddskils;
import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.changepassword.responce.ChangepasswordModel;
import com.bannet.skils.databinding.FragmentProfileBinding;
import com.bannet.skils.favposting.activity.ActivitySavedPostingList;
import com.bannet.skils.language.activity.ActivityLanguageScreen;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.bannet.skils.myeanings.activity.ActivityMyEarnings;
import com.bannet.skils.myposting.activity.ActivityMyPostScreen;
import com.bannet.skils.myposting.responce.MypostingListModel;
import com.bannet.skils.privacy.activity.ActivityPrivacy;
import com.bannet.skils.profile.activity.ActivityEditProfileScreen;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bannet.skils.terms.activity.ActivityTerms;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentProfile extends Fragment {

    public Context context;
    public View view;
    public String USER_ID;
    public ProgressDialog progressDialog;
    public FragmentProfileBinding binding;

    public Dialog dialog;
    public TextView txt_cancel,txt_ok,cp1;
    public EditText toldpasswoed,tnewpassword,tconfirmpassword;
    private String  oldpassword,newpassword,newconfirmpass;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        view = binding.getRoot();

        context = getActivity();

        init();

        return view;

    }

    private void init(){

        //onbackword();

        binding.postingView.setText(GlobalMethods.getString(context,R.string.view));
        binding.earningsview.setText(GlobalMethods.getString(context,R.string.view));
        binding.p1.setText(GlobalMethods.getString(context,R.string.postings));
        binding.p2.setText(GlobalMethods.getString(context,R.string.earnings));
        binding.p3.setText(GlobalMethods.getString(context,R.string.profile));
        binding.p4.setText(GlobalMethods.getString(context,R.string.edit_profile));
        binding.p5.setText(GlobalMethods.getString(context,R.string.change_skills));
        binding.p6.setText(GlobalMethods.getString(context,R.string.change_password));
        binding.p7.setText(GlobalMethods.getString(context,R.string.others));
        binding.p8.setText(GlobalMethods.getString(context,R.string.privacyPolicy));
        binding.p9.setText(GlobalMethods.getString(context,R.string.termsandConditions));
        binding.p10.setText(GlobalMethods.getString(context,R.string.emailSupport));
        binding.p11.setText(GlobalMethods.getString(context,R.string.changeLanguage));
        binding.saved.setText(GlobalMethods.getString(context,R.string.saved));

        binding.p4.setSelected(true);
        binding.p5.setSelected(true);
        binding.p6.setSelected(true);
        binding.p7.setSelected(true);
        binding.p8.setSelected(true);
        binding.p9.setSelected(true);
        binding.p10.setSelected(true);
        binding.p11.setSelected(true);
        binding.saved.setSelected(true);

        USER_ID= PrefConnect.readString(context,PrefConnect.USER_ID,"");

        getprofile(USER_ID);
        mypostingList(USER_ID);
        userCheck(USER_ID);

        binding.ediSkilssBtn.setOnClickListener(view -> {

            Intent in=new Intent(context, ActivityAddskils.class);
            in.putExtra("type","edit_skils");
            in.putExtra("user_id",USER_ID);
            startActivity(in);

        });

        binding.ediSavedBtn.setOnClickListener(view -> {

            Intent in=new Intent(context, ActivitySavedPostingList.class);
            startActivity(in);

        });

        binding.editProfileBtn.setOnClickListener(view -> {

            if(USER_ID.equals("customer")){

                userLogin();

            }
            else {

                Intent intent=new Intent(context, ActivityEditProfileScreen.class);
                startActivity(intent);

            }

        });

        binding.changeLanguageBtn.setOnClickListener(view -> {

            if(USER_ID.equals("customer")){

                userLogin();

            }
            else {

                Intent intent=new Intent(context, ActivityLanguageScreen.class);
                intent.putExtra("select","settings");
                intent.putExtra("user_id",USER_ID);
                startActivity(intent);

            }


        });

        binding.privacyBtn.setOnClickListener(view -> {

            Intent intent=new Intent(context, ActivityPrivacy.class);
            startActivity(intent);

        });

        binding.termsBtn.setOnClickListener(view -> {

            Intent intent=new Intent(context, ActivityTerms.class);
            startActivity(intent);

        });

        binding.earningsview.setOnClickListener(view -> {

            if(USER_ID.equals("customer")){

                userLogin();
            }
            else {

                Intent intent=new Intent(context, ActivityMyEarnings.class);
                startActivity(intent);

            }


        });

        binding.emailsupportBtn.setOnClickListener(view -> emailSupportHelp());

        binding.changePasswordBtn.setOnClickListener(view ->{

            if(USER_ID.equals("customer")){

                userLogin();
            }
            else {

                changePasswordDialog();

            }

        });

        binding.postingView.setOnClickListener(view -> {

            if(USER_ID.equals("customer")){
                userLogin();
            }
            else {

                Intent intent=new Intent(context, ActivityMyPostScreen.class);
                startActivity(intent);

            }


        });

        binding.logoutBtn.setOnClickListener(view -> logOut());

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

    private void logOut() {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView title=dialog.findViewById(R.id.title);
        AppCompatButton cancel =  dialog.findViewById(R.id.cancel);
        AppCompatButton ok =  dialog.findViewById(R.id.ok);

        title.setText(GlobalMethods.getString(context,R.string.Are_you_sure_to_logout));
        cancel.setText(GlobalMethods.getString(context,R.string.cancel));
        ok.setText(GlobalMethods.getString(context,R.string.ok));

        cancel.setOnClickListener(v -> dialog.dismiss());

        ok.setOnClickListener(v -> {

            dialog.dismiss();
            PrefConnect.writeString(context, PrefConnect.USER_ID, "");
            PrefConnect.writeString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"");
            Intent in = new Intent(context, ActivityPhonenumberScreen.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            requireActivity().finish();

        });


    }

    private void getprofile(String  user_id) {

        if(GlobalMethods.isNetworkAvailable(context)){
            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            Api.getClient().getprofile(user_id, PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<getprofileModel>() {
                @Override
                public void onResponse(@NonNull Call<getprofileModel> call, @NonNull Response<getprofileModel> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            String userName = response.body().getDetails().getFirstName()+" "+response.body().getDetails().getLastName();
                            binding.userName.setText(userName);
                            binding.userCode.setText(response.body().getDetails().getReferalCode());
                            binding.earningsAmount.setText(response.body().getDetails().getEarnings());
                            Glide.with(context).load(response.body().getDetails().getImageUrl() + response.body().getDetails().getImageName()).error(R.drawable.profile_image).into(binding.userImage);

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<getprofileModel> call, @NonNull Throwable t) {

                    progressDialog.dismiss();

                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }

    public void userCheck(String id){
//
//        if(GlobalMethods.isNetworkAvailable(context)){
//            Api.getClient().CheckUser(id).enqueue(new Callback<CommonModel>() {
//
//                @Override
//                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
//                    Log.e("status",new Gson().toJson(response.body()));
//
//                    if (response.isSuccessful()){
//
//                        assert response.body() != null;
//                        if(!response.body().getStatus().equals("1")){
//
//                            GlobalMethods.Toast(context,"Your Account has been deleted");
//                            PrefConnect.writeString(context, PrefConnect.USER_ID, "");
//                            PrefConnect.writeString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"");
//                            Intent in = new Intent(context, ActivityPhonenumberScreen.class);
//                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(in);
//                            requireActivity().finish();
//
//                        }
//
//                    }
//
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
//
//
//                }
//            });
//        }else {
//            GlobalMethods.Toast(context, "No Internet Connection");
//        }
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

    public void mypostingList(String userid){

        if (GlobalMethods.isNetworkAvailable(context)) {

            Api.getClient().mypostingslist(userid,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<MypostingListModel>() {
                @Override
                public void onResponse(@NonNull Call<MypostingListModel> call, @NonNull Response<MypostingListModel> response) {

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            if(response.body().getPostingList().size() == 0){

                                binding.totalPosting.setText("0");

                            }
                            else {

                                binding.totalPosting.setText(String.valueOf(response.body().getPostingList().size()));

                            }

                        }

                        else {

                            binding.totalPosting.setText("0");

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MypostingListModel> call, @NonNull Throwable t) {


                }
            });

        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }

    public void emailSupportHelp(){

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"support@skilsss.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");

        try {

            startActivity(Intent.createChooser(i, "Send mail..."));

        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }


    private void changePasswordDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox_change_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        cp1=dialog.findViewById(R.id.cp1);
        txt_cancel =  dialog.findViewById(R.id.txt_cancel);
        toldpasswoed=dialog.findViewById(R.id.txt_oldpassword);
        tnewpassword=dialog.findViewById(R.id.txt_newpassword);
        tconfirmpassword=dialog.findViewById(R.id.txt_confirmpassword);
        txt_ok =  dialog.findViewById(R.id.txt_ok);


        txt_cancel.setOnClickListener(view -> dialog.dismiss());
        txt_ok.setOnClickListener(view -> {


            if(validation()){

                changePassword(USER_ID);

            }

        });

    }

    public Boolean validation() {

        oldpassword=toldpasswoed.getText().toString();
        newpassword=tnewpassword.getText().toString();
        newconfirmpass=tconfirmpassword.getText().toString();

        if(oldpassword.isEmpty()){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Please_Enter_Old_Password));
            return false;
        }

        else if(newpassword.isEmpty()){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Please_Enter_New_Password));
            return false;
        }
        else if(newconfirmpass.isEmpty()){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Please_Enter_Confirm_Password));
            return false;
        }
        else if(!newpassword.equals(newconfirmpass)){

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Password_Not_Match));
            return false;
        }
        return  true;
    }

    private void changePassword(String user_id) {

        if (GlobalMethods.isNetworkAvailable(context)) {

            progressDialog = ProgressDialog.show(context, "", "Loading...", true);

            Api.getClient().getchangepassword(user_id,oldpassword,newpassword,newconfirmpass,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<ChangepasswordModel>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<ChangepasswordModel> call, @NonNull Response<ChangepasswordModel> response) {

                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            dialog.dismiss();
                            GlobalMethods.Toast(context,response.body().getMessage());
                            // finish();
                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ChangepasswordModel> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                }
            });

        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }
    }
}