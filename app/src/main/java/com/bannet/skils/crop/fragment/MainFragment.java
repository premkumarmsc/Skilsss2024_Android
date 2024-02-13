package com.bannet.skils.crop.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.bannet.skils.addskilss.adapter.AdapterChooseCategery;
import com.bannet.skils.explore.response.CategoryResponce;
import com.bannet.skils.profile.responce.ImageUpload;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 版权所有：XXX有限公司
 *
 * MainFragment
 *
 * @author zhou.wenkai ,Created on 2016-5-5 10:25:49
 * 		   Major Function：<b>MainFragment</b>
 *
 *         注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
 * @author mender，Modified Date Modify Content:
 */
public class MainFragment extends PictureSelectorFragment {

    ImageView mPictureIv;
    private ProgressDialog progressDialog;
    public View view;
    Context context;
    ImageView backbtn;
    public Dialog timepickerdialog;
    LinearLayout relative_add_new_skill;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1,RESULT_LOAD_IMAGE = 1;
    private int PICK_PDF_REQUEST = 2;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    Dialog camera_dialog;
    TextView txt_gallery, txt_take_photo,select_p,edicatName;
    Button btn_cancel_dialog;
    ImageView uploaded_image;
    EditText edit_about_skill,edit_skill_name;
    String file_name = "",path;
    File finalFile;
    String file_name_skill = "",catId = "",catName;
    String user_id;

    private AdapterChooseCategery adapterChooseCategery;
    public AdapterChooseCategery.ItemClickListener itemClickListenercategory;

    //language
    TextView ans2,ans3,ans4,ans5;
    Resources resources;
    public RelativeLayout selectCatagery;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        context = getActivity();
        init(view);
        callMultiplePermissions();
        return view;

    }

    public void init(View view){

        backbtn=view.findViewById(R.id.back_btn);
        relative_add_new_skill = view.findViewById(R.id.relative_add_new_skill);
        edit_about_skill = (EditText) view.findViewById(R.id.edit_about_skill);
        edit_skill_name = (EditText) view.findViewById(R.id.edit_skill_name);
        selectCatagery = view.findViewById(R.id.selectcat);
        edicatName = view.findViewById(R.id.edit_catogery);

        ans2=view.findViewById(R.id.ans2);
        ans3=view.findViewById(R.id.ans3);
        ans4=view.findViewById(R.id.ans4);
        ans5=view.findViewById(R.id.ans5);

        if(getArguments() != null){

            user_id = getArguments().getString("user_id");

        }

        relative_add_new_skill.setOnClickListener(view1 -> {
            if(validation()){
                callAddSkills();
            }
        });


        selectCatagery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCategory();
            }
        });

        itemClickListenercategory = (cat_id, cat_name) -> {

            catName = cat_name;
            catId = cat_id;

        };

        languageChange();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPictureIv = view.findViewById(R.id.main_frag_picture_iv);
        initEvents();
    }

    public void initEvents() {
        // 设置图片点击监听
        mPictureIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

        // 设置裁剪图片结果监听
        setOnPictureSelectedListener(new OnPictureSelectedListener() {
            @Override
            public void onPictureSelected(Uri fileUri, Bitmap bitmap) {
                mPictureIv.setImageBitmap(bitmap);

                String filePath = fileUri.getEncodedPath();
                String imagePath = Uri.decode(filePath);
                uploadFile(imagePath);
                Toast.makeText(getContext(), "hiiiii:" + imagePath, Toast.LENGTH_LONG).show();
            }
        });
    }


    public void callAddSkills(){

        if(GlobalMethods.isNetworkAvailable(context)){
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            Api.getClient().addSkills(edit_skill_name.getText().toString(), edit_about_skill.getText().toString(),
                    file_name_skill,user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""),catId).enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    progressDialog.dismiss();
                    Log.d("addskills",new Gson().toJson(response.body()));
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("1")){
                            GlobalMethods.Toast(context,response.body().getMessage());
                            requireActivity().finish();
                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });

        }else {
            GlobalMethods.Toast(context,GlobalMethods.getString(context,R.string.No_Internet_Connection) );
        }
    }

    private void chooseCategory() {
        timepickerdialog= new Dialog(getActivity());
        timepickerdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timepickerdialog.setContentView(R.layout.dialogbox_addstate);
        timepickerdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timepickerdialog.setCancelable(false);
        TextView ss = timepickerdialog.findViewById(R.id.ss);
        AppCompatButton closeDialog=(AppCompatButton) timepickerdialog.findViewById(R.id.state_dialogbox_close);
        AppCompatButton dialogbox_done = (AppCompatButton) timepickerdialog.findViewById(R.id.state_dialogbox_done);
        RecyclerView recycler_cat = (RecyclerView) timepickerdialog.findViewById(R.id.recycler_state);

        ss.setText(GlobalMethods.getString(context,R.string.select_category));
        dialogbox_done.setText(GlobalMethods.getString(context,R.string.done));
        closeDialog.setText(GlobalMethods.getString(context,R.string.close));

        dialogbox_done.setOnClickListener(view -> {

            timepickerdialog.dismiss();
            edicatName.setText(catName);

        });

        closeDialog.setOnClickListener(view -> timepickerdialog.dismiss());

        if(GlobalMethods.isNetworkAvailable(context)){
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            Api.getClient().categoryList(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CategoryResponce>() {
                @Override
                public void onResponse(@NonNull Call<CategoryResponce> call, @NonNull Response<CategoryResponce> response) {
                    progressDialog.dismiss();
                    Log.e("mycatery",new Gson().toJson(response.body()));

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){
                        timepickerdialog.show();
                        //  GridLayoutManager linearLayoutManager =new GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,false);
                        adapterChooseCategery = new AdapterChooseCategery(context,itemClickListenercategory,response.body().getCategoryList());
                        recycler_cat.setHasFixedSize(true);
                        recycler_cat.setLayoutManager(new LinearLayoutManager(context));
                        recycler_cat.setAdapter(adapterChooseCategery);


                    }else {

                        GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CategoryResponce> call, @NonNull Throwable t) {
                    progressDialog.dismiss();

                    Log.e("failure professional res",t.getMessage());
                }
            });
        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }


    public boolean validation(){

        if(edit_skill_name.getText().toString().isEmpty()){

            Toast.makeText(context, GlobalMethods.getString(context,R.string.enter_new_skill_name), Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(edit_about_skill.getText().toString().isEmpty()){
            Toast.makeText(context, GlobalMethods.getString(context,R.string.Enter_about_skill), Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(file_name_skill.equals("")){
            Toast.makeText(context, GlobalMethods.getString(context,R.string.Upload_Skill_Image), Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(catId.equals("")){
            Toast.makeText(context, GlobalMethods.getString(context,R.string.please_select_catoger), Toast.LENGTH_SHORT).show();
            return  false;
        }
        return  true;

    }


    private void uploadFile(String imagePath) {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(imagePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file_name", file.getName(), requestBody);

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), "skills_image");
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
        Api.getClient().profileImageUpload(fileToUpload,filename).enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(@NonNull Call<ImageUpload> call, @NonNull Response<ImageUpload> response) {
                Log.e("sssss",new Gson().toJson(response.body()));

                progressDialog.dismiss();
                assert response.body() != null;
                if(response.body().getStatus().equals("1")){

                    file_name_skill= response.body().getFileName();


                }
                else {

                    GlobalMethods.Toast(context,response.body().getMessage()+"");

                }

            }
            @Override
            public void onFailure(@NonNull Call<ImageUpload> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e("fff",t.getMessage());
            }
        });

    }


    private void callMultiplePermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("ACCESS_FINE_LOCATION");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("ACCESS_COARSE_LOCATION");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    // Pre-Marshmallow
                }

                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {
                // Pre-Marshmallow
            }

            return;
        }

    }

    /**
     * add Permissions
     *
     * @param permissionsList
     * @param permission
     * @return
     */
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        } else {
            // Pre-Marshmallow
        }

        return true;
    }

    /**
     * Permissions results
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and others

              /*  perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&*/

                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    Toast.makeText(context, GlobalMethods.getString(context,R.string.Permissin_is_denied), Toast.LENGTH_SHORT).show();

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void languageChange() {

        if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==1)
        {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            ans2.setText(resources.getText(R.string.skill_name));
            ans3.setText(resources.getText(R.string.about_skill));
            ans4.setText(resources.getText(R.string.add_skill_image));
            ans5.setText(resources.getText(R.string.request));
            edit_skill_name.setHint(resources.getText(R.string.enter_new_skill_name));
            edit_about_skill.setHint(resources.getText(R.string.about_skill));



        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==2)
        {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            ans2.setText(resources.getText(R.string.skill_name));
            ans3.setText(resources.getText(R.string.about_skill));
            ans4.setText(resources.getText(R.string.add_skill_image));
            ans5.setText(resources.getText(R.string.request));
            edit_skill_name.setHint(resources.getText(R.string.enter_new_skill_name));
            edit_about_skill.setHint(resources.getText(R.string.about_skill));

        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==3)
        {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            ans2.setText(resources.getText(R.string.skill_name));
            ans3.setText(resources.getText(R.string.about_skill));
            ans4.setText(resources.getText(R.string.add_skill_image));
            ans5.setText(resources.getText(R.string.request));
            edit_skill_name.setHint(resources.getText(R.string.enter_new_skill_name));
            edit_about_skill.setHint(resources.getText(R.string.about_skill));



        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==4)
        {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            ans2.setText(resources.getText(R.string.skill_name));
            ans3.setText(resources.getText(R.string.about_skill));
            ans4.setText(resources.getText(R.string.add_skill_image));
            ans5.setText(resources.getText(R.string.request));
            edit_skill_name.setHint(resources.getText(R.string.enter_new_skill_name));
            edit_about_skill.setHint(resources.getText(R.string.about_skill));



        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==5)
        {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            ans2.setText(resources.getText(R.string.skill_name));
            ans3.setText(resources.getText(R.string.about_skill));
            ans4.setText(resources.getText(R.string.add_skill_image));
            ans5.setText(resources.getText(R.string.request));
            edit_skill_name.setHint(resources.getText(R.string.enter_new_skill_name));
            edit_about_skill.setHint(resources.getText(R.string.about_skill));



        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==6)
        {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            ans2.setText(resources.getText(R.string.skill_name));
            ans3.setText(resources.getText(R.string.about_skill));
            ans4.setText(resources.getText(R.string.add_skill_image));
            ans5.setText(resources.getText(R.string.request));
            edit_skill_name.setHint(resources.getText(R.string.enter_new_skill_name));
            edit_about_skill.setHint(resources.getText(R.string.about_skill));



        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==7)
        {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            ans2.setText(resources.getText(R.string.skill_name));
            ans3.setText(resources.getText(R.string.about_skill));
            ans4.setText(resources.getText(R.string.add_skill_image));
            ans5.setText(resources.getText(R.string.request));
            edit_skill_name.setHint(resources.getText(R.string.enter_new_skill_name));
            edit_about_skill.setHint(resources.getText(R.string.about_skill));


        }
    }
    private void languagechangecamara() {
        if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 1) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));




        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 2) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 3) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 4) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 5) {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));


        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 6) {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));

        } else if (PrefConnect.readInteger(context, PrefConnect.LANGUAGE, 1) == 7) {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();

            txt_gallery.setText(resources.getText(R.string.gallery));
            txt_take_photo.setText(resources.getText(R.string.camera));
            btn_cancel_dialog.setText(resources.getText(R.string.cancel));
            select_p.setText(resources.getText(R.string.select_picture));
        }
    }


}
