package com.bannet.skils.Activitys;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.addskilss.adapter.AdapterChooseCategery;
import com.bannet.skils.crop.fragment.MainFragment;
import com.bannet.skils.explore.response.CategoryResponce;
import com.bannet.skils.profile.responce.ImageUpload;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.languagechangeclass.LocaleHelper;
import com.bannet.skils.service.PrefConnect;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddNewSkill extends AppCompatActivity{

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
    ProgressDialog progressDialog;

    private AdapterChooseCategery adapterChooseCategery;
    public AdapterChooseCategery.ItemClickListener itemClickListenercategory;

    //language
    TextView ans1,ans2,ans3,ans4,ans5;
    Resources resources;
    public RelativeLayout selectCatagery;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_skill);
        getSupportActionBar().hide();
        context= ActivityAddNewSkill.this;
        callMultiplePermissions();

        init();


    }
    public void init(){
//
        backbtn=findViewById(R.id.back_btn);
//        relative_add_new_skill = findViewById(R.id.relative_add_new_skill);
//        uploaded_image = (ImageView) findViewById(R.id.uploaded_image);
//        edit_about_skill = (EditText) findViewById(R.id.edit_about_skill);
//        edit_skill_name = (EditText) findViewById(R.id.edit_skill_name);
//        selectCatagery = findViewById(R.id.selectcat);
//        edicatName = findViewById(R.id.edit_catogery);
//        user_id = PrefConnect.readString(context,PrefConnect.USER_ID,"");
//
        ans1=findViewById(R.id.ans1);

        ans1.setText(GlobalMethods.getString(context,R.string.add_new_skill));

        user_id = getIntent().getStringExtra("user_id");
        initMainFragment();
//        ans2=findViewById(R.id.ans2);
//        ans3=findViewById(R.id.ans3);
//        ans4=findViewById(R.id.ans4);
//        ans5=findViewById(R.id.ans5);
//
//
//        uploaded_image.setOnClickListener(view -> setVerifiedDialog());
//
//        relative_add_new_skill.setOnClickListener(view -> {
//            if(validation()){
//                callAddSkills();
//            }
//        });
//
        backbtn.setOnClickListener(view -> finish());
//
//        selectCatagery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                chooseCategory();
//            }
//        });
//
//        itemClickListenercategory = (cat_id, cat_name) -> {
//
//            catName = cat_name;
//            catId = cat_id;
//
//        };
//
//        initMainFragment();
//        languageChange();
//
    }

    public void initMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle in = new Bundle();
        in.putString("user_id",user_id);
        MainFragment mFragment = MainFragment.newInstance();
        mFragment.setArguments(in);
        transaction.replace(R.id.main_act_container, mFragment, mFragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void callAddSkills(){

        if(GlobalMethods.isNetworkAvailable(ActivityAddNewSkill.this)){
            progressDialog = ProgressDialog.show(ActivityAddNewSkill.this, "", "Loading...", true);
            Api.getClient().addSkills(edit_skill_name.getText().toString(), edit_about_skill.getText().toString(),
                    file_name_skill,user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""),catId).enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    progressDialog.dismiss();

                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("1")){
                            GlobalMethods.Toast(context,response.body().getMessage());
                            finish();
                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                    else {

                        GlobalMethods.Toast(context,GlobalMethods.getString(context,R.string.please_try_again_later));

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
        timepickerdialog= new Dialog(ActivityAddNewSkill.this);
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
            progressDialog = ProgressDialog.show(ActivityAddNewSkill.this, "", "Loading...", true);
            Api.getClient().categoryList(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CategoryResponce>() {
                @Override
                public void onResponse(@NonNull Call<CategoryResponce> call, @NonNull Response<CategoryResponce> response) {
                    progressDialog.dismiss();

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

    private void setVerifiedDialog() {
        camera_dialog = new Dialog(this);
        camera_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        camera_dialog.setContentView(R.layout.dialog_photo);
        camera_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        camera_dialog.show();
        camera_dialog.setCancelable(false);
        txt_gallery = (TextView) camera_dialog.findViewById(R.id.txt_gallery);
        select_p = (TextView) camera_dialog.findViewById(R.id.select_p);
        txt_take_photo = (TextView) camera_dialog.findViewById(R.id.txt_take_photo);
        btn_cancel_dialog = (Button) camera_dialog.findViewById(R.id.btn_cancel_dialog);
        btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera_dialog.dismiss();
            }
        });
        txt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera_dialog.dismiss();
                galleryIntent();
            }
        });
        txt_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera_dialog.dismiss();
                cameraIntent();
            }
        });
        languagechangecamara();
    }

    private void cameraIntent()
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityIfNeeded(cameraIntent, 0);

    }

    private void galleryIntent()
    {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityIfNeeded(pickPhoto , 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){

                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");


                    Uri tempUri = getImageUri(context, photo);

                    if(tempUri == null){

                       // Toast.makeText(context, GlobalMethods.getString(context,R.string.Upload_Skill_Image), Toast.LENGTH_SHORT).show();

                    }
                    else{

                        uploaded_image.setImageBitmap(photo);
                        finalFile = new File(getRealPathFromURI(tempUri));
                        file_name = finalFile.getName();
                        path = finalFile+"";
                        uploadFile();

                    }

                }

                break;
            case 1:

                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();

                    finalFile = new File(getRealPathFromURI(selectedImage));
                    file_name = finalFile.getName();

                    Bitmap fullSizeBitMap = BitmapFactory.decodeFile(finalFile+"");

                    int imageRotation = getImageRotation(finalFile);

                    if (imageRotation != 0)
                        fullSizeBitMap = getBitmapRotatedByDegree(fullSizeBitMap, imageRotation);

                    if(fullSizeBitMap == null){

                    }
                    else {
                        Uri tempUri = getImageUri(context, fullSizeBitMap);
                        uploaded_image.setImageBitmap(fullSizeBitMap);
                        finalFile = new File(getRealPathFromURI(tempUri));
                        file_name = finalFile.getName();
                        path = finalFile+"";
                        uploadFile();
                    }

                }

                break;
        }

    }

    private static int getImageRotation(final File imageFile) {

        ExifInterface exif = null;
        int exifRotation = 0;

        try {
            exif = new ExifInterface(imageFile.getPath());
            exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif == null)
            return 0;
        else
            return exifToDegrees(exifRotation);
    }

    private static Bitmap getBitmapRotatedByDegree(Bitmap bitmap, int rotationDegree) {
        Matrix matrix = new Matrix();
        matrix.preRotate(rotationDegree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static int exifToDegrees(int rotation) {
        if (rotation == ExifInterface.ORIENTATION_ROTATE_90)
            return 90;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_180)
            return 180;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_270)
            return 270;

        return 0;
    }



    private void uploadFile() {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(path);


        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file_name", file.getName(), requestBody);

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), "skills_image");
        progressDialog = ProgressDialog.show(ActivityAddNewSkill.this, "", "Loading...", true);
        Api.getClient().profileImageUpload(fileToUpload,filename).enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(@NonNull Call<ImageUpload> call, @NonNull Response<ImageUpload> response) {

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

            }
        });

    }


    private File getMapFile(Bitmap reduceBitMap) {

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "reduced_file");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        reduceBitMap.compress(Bitmap.CompressFormat.JPEG,0,bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            file.createNewFile();
            fos.flush();
            fos.close();
            return file;

        }
        catch (IOException e) {

            e.printStackTrace();


        }

        return file;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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

            ans1.setText(resources.getText(R.string.add_new_skill));
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

            ans1.setText(resources.getText(R.string.add_new_skill));
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

            ans1.setText(resources.getText(R.string.add_new_skill));
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

            ans1.setText(resources.getText(R.string.add_new_skill));
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

            ans1.setText(resources.getText(R.string.add_new_skill));
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

            ans1.setText(resources.getText(R.string.add_new_skill));
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

            ans1.setText(resources.getText(R.string.add_new_skill));
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