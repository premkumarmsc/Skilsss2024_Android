package com.bannet.skils.chat.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bannet.skils.Model.WebModel.CommonModel;
import com.bumptech.glide.Glide;
import com.bannet.skils.Fcm.AdapterChatDetailTwo;
import com.bannet.skils.chat.responce.SentMessageModel;
import com.bannet.skils.Model.UserDetailsOther;
import com.bannet.skils.profile.responce.ImageUpload;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
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

public class ActivityChatDetails extends AppCompatActivity {

    ImageView img_back;
    DatabaseReference rootRef;
    Firebase reference1;
    AdapterChatDetailTwo adapterChatDetailTwo;
    String user_id="", time, other_user_id="", chat_else = "0",chat_id="",profile_url="",blocked_status="1",txtmessage="";
    List<UserDetailsOther> userDetailsOthers = new ArrayList<>();
    ImageView chat_camara_btn,msgSendButton;
    RecyclerView chatingpage;
    TextView txt_title;
    EditText inputmsg;
    String firebaseId;
    Context context;
    private Uri uri;
    private Bitmap bitmap;
    ProgressDialog progressDialog;
    private static int RESULT_LOAD_IMAGE = 1;
    String imageName,imageUrl;
    String file_name = "",path;
    File finalFile;
    ImageView img_user_report,img_user_block;
    Dialog dialog;
    AdapterChatDetailTwo.ItemClickListener itemClickListener;
    Dialog dialogImageview;
    ImageView PhotoView,close_Img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        getSupportActionBar().hide();
        context=ActivityChatDetails.this;
        Firebase.setAndroidContext(this);

        init();

    }
    private void init(){
        img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img_user_report =findViewById(R.id.img_user_report);
        img_user_block =findViewById(R.id.img_user_block);
        img_user_report.setOnClickListener(view -> {

            genrate_Dialog("Report User !","Do you really want to Report this user? ","1");
        });
        img_user_block.setOnClickListener(view -> {
            genrate_Dialog("Block User !","Do you really want to Block this user?","2");
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        }
        user_id = PrefConnect.readString(context, PrefConnect.USER_ID, "");

        if (getIntent() != null) {
            other_user_id = getIntent().getStringExtra("other_user_id");
            getotherUserProfile(other_user_id);
        }

        imageUrl=PrefConnect.readString(context,PrefConnect.USER_IMAGE_URL,"");
        imageName=PrefConnect.readString(context,PrefConnect.USER_IMAGE_NAME,"");
        profile_url=imageUrl+imageName;


        chat_camara_btn=(ImageView)findViewById(R.id.chat_camara_btn);
        chatingpage = (RecyclerView) findViewById(R.id.chatingpage);
        txt_title = (TextView) findViewById(R.id.chatting_person_name);

        // Set RecyclerView layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chatingpage.setLayoutManager(linearLayoutManager);

        rootRef = FirebaseDatabase.getInstance().getReference("messages");


        rootRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.e("USERID OTHER USER ID", user_id + "::" + other_user_id);
                if (snapshot.hasChild(user_id + "-" + other_user_id)) {
                    Log.e("FIREBASE", " in if");
                    reference1 = new Firebase(GlobalMethods.FIREBASE_CHAT_URL + "messages/" + user_id + "-" + other_user_id);
                    chat_else = "0";
                    chat_id=user_id + "-" + other_user_id;
                    setFirebaseChatDetail();
                }
                else if (snapshot.hasChild(other_user_id + "-" + user_id)) {
                    Log.e("FIREBASE", " in else if");
                    reference1 = new Firebase(GlobalMethods.FIREBASE_CHAT_URL + "messages/" + other_user_id + "-" + user_id);
                    chat_else = "0";
                    chat_id=other_user_id + "-" + user_id;
                    setFirebaseChatDetail();
                }
                else {
                    Log.e("FIREBASE", " in else");
                    reference1 = new Firebase(GlobalMethods.FIREBASE_CHAT_URL + "messages/" + other_user_id + "-" + user_id);
                    chat_else = "1";
                    chat_id=other_user_id + "-" + user_id;
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        firebaseId = rootRef.child("messages").push().getKey();


        inputmsg = (EditText) findViewById(R.id.chat_input_msg);
        msgSendButton = (ImageView) findViewById(R.id.chat_send_msg);

        msgSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtmessage = inputmsg.getText().toString().trim();

                    if (!txtmessage.equals("")) {
                        setFirebaseSenMesg(txtmessage,"");
                    } else {
                        GlobalMethods.Toast(context, "Enter Message");
                    }


            }
        });


        chat_camara_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent();
            }
        });

        if (GlobalMethods.isNetworkAvailable(context)){

        }else {
            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }




        itemClickListener=new AdapterChatDetailTwo.ItemClickListener() {
            @Override
            public void ItemClick(String imageurl) {
                genrateDilaog(imageurl);

            }
        };


    }
    public void genrateDilaog( String imgurl)
    {

        dialogImageview=new Dialog(context) ;
        dialogImageview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogImageview.setContentView(R.layout.dialog_imageview);
        dialogImageview.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogImageview.setCancelable(false);


        PhotoView=(ImageView) dialogImageview.findViewById(R.id.img_photoView);

        close_Img=(ImageView)dialogImageview.findViewById(R.id.img_close);

        Glide.with(context).load(imgurl).into(PhotoView);

        close_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogImageview.dismiss();
            }
        });
        dialogImageview.show();

    }

    private void setFirebaseChatDetail() {

        reference1.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String useRid = map.get("user_id").toString();
                String profile_url_image = map.get("profile_url").toString();
                String image_url = map.get("image").toString();
                String time = getDate(Long.parseLong(map.get("time").toString()), "dd MMM yy hh:mm a");
                UserDetailsOther other = new UserDetailsOther();

                String type = map.get("type").toString();

                if (type.contains("-")){
                    String[] TEMP =type.split("-");
                    if (!TEMP[1].equals(user_id)){
                        other.setUserid(useRid);
                        other.setUserMessage(message);
                        other.setTime(time);
                        other.setProfile_url(profile_url_image);
                        other.setImage(image_url);
                        userDetailsOthers.add(other);

                    }
                }else {

                    other.setUserid(useRid);
                    other.setUserMessage(message);
                    other.setTime(time);
                    other.setProfile_url(profile_url_image);
                    other.setImage(image_url);
                    userDetailsOthers.add(other);
                }


                final LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityChatDetails.this);
                chatingpage.setLayoutManager(layoutManager);
                layoutManager.setStackFromEnd(true);
                adapterChatDetailTwo = new AdapterChatDetailTwo(context, userDetailsOthers, itemClickListener);
                chatingpage.setAdapter(adapterChatDetailTwo);
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(com.firebase.client.FirebaseError firebaseError) {

            }
        });

    }


    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

    }

    private void setSendMsgApi(String type,String msg){


        if(GlobalMethods.isNetworkAvailable(ActivityChatDetails.this)){

            Api.getClient().sentMessage(user_id,other_user_id,chat_id,msg,firebaseId,type,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<SentMessageModel>() {
                @Override
                public void onResponse(Call<SentMessageModel> call, Response<SentMessageModel> response) {
                    Log.e("suss",new Gson().toJson(response.body()));

                    if(response.isSuccessful()){

                    }
                }

                @Override
                public void onFailure(Call<SentMessageModel> call, Throwable t) {

                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }
    public void getotherUserProfile(String user_id){

        if(GlobalMethods.isNetworkAvailable(ActivityChatDetails.this)){
              progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            Api.getClient().getprofile(user_id,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<getprofileModel>() {
                @Override
                public void onResponse(Call<getprofileModel> call, Response<getprofileModel> response) {
                    Log.e("efuegfhdefh",new Gson().toJson(response.body()));

                       progressDialog.dismiss();

                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("1")){
                            Log.e("name",response.body().getDetails().getFirstName());
                            txt_title.setText(response.body().getDetails().getFirstName()+" "+response.body().getDetails().getLastName());

                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<getprofileModel> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
        }

    }
    private void setFirebaseSenMesg(String message,String image) {

        Map<String, String> map = new HashMap<String, String>();
        Calendar calendar = Calendar.getInstance();
        time = calendar.getTimeInMillis() + "";
        map.put("message", message);
        map.put("user_id", user_id);
        map.put("time", time);
        map.put("image", image);
        map.put("profile_url", profile_url);

        String type;

        type="1";
        setSendMsgApi("text",message);

        map.put("type", type);

        reference1.push().setValue(map);
        if (chat_else.equals("1")) {
            chat_else = "0";
            setFirebaseChatDetail();
        }
        inputmsg.setText("");

    }

    private void galleryIntent() {
        try{

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if(resultCode == RESULT_OK){
                Uri selectedImage = data.getData();

                finalFile = new File(getRealPathFromURI(selectedImage));
                file_name = finalFile.getName();
                path = finalFile+"";

                UploadImage();

            }

        } else {
            Toast.makeText(context, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void UploadImage() {

        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(path);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file_name", file.getName(), requestBody);

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), "skills_image");

        Api.getClient().profileImageUpload(fileToUpload, filename).enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {

                if (response.body().getStatus().equals("1")) {

                    Map<String, String> map = new HashMap<String, String>();
                    Calendar calendar = Calendar.getInstance();
                    time = calendar.getTimeInMillis() + "";
                     inputmsg.setText("");
                    map.put("message", "");
                    map.put("user_id", user_id);
                    map.put("time", time);
                    map.put("image", response.body().getFileUrl());
                    map.put("profile_url", profile_url);

                    String type;
                   

                    type="1";
                    setSendMsgApi("Image","Image");

                    map.put("type", type);

                    reference1.push().setValue(map);
                    if (chat_else.equals("1")) {
                        chat_else = "0";
                        setFirebaseChatDetail();
                    }
                    inputmsg.setText("");
                } else {


                }

            }

            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {

            }
        });

    }



    private void genrate_Dialog(String title,String desc,String Type)
    {
        TextView  dialog_txt_title,dialog_txt_description;
        Button btn_cancel_dialog,btn_ok_dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_block_report);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dialog_txt_title=dialog.findViewById(R.id.txt_title);
        dialog_txt_title.setText(title);
        dialog_txt_description=dialog.findViewById(R.id.txt_descripition);
        dialog_txt_description.setText(desc);
        btn_cancel_dialog =  dialog.findViewById(R.id.btn_cancel_dialog);
        btn_ok_dialog =  dialog.findViewById(R.id.btn_ok_dialog);

        btn_cancel_dialog.setOnClickListener(v -> dialog.dismiss());

        btn_ok_dialog.setOnClickListener(v -> {
            dialog.dismiss();
            toReport_Block(Type,other_user_id);


        });



    }
    private void genrate_DialogResult(String title,String desc)
    {
        TextView  dialog_txt_title,dialog_txt_description;
        Button btn_cancel_dialog,btn_ok_dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_block_report);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dialog_txt_title=dialog.findViewById(R.id.txt_title);
        dialog_txt_title.setText(title);
        dialog_txt_description=dialog.findViewById(R.id.txt_descripition);
        dialog_txt_description.setText(desc);
        btn_cancel_dialog =  dialog.findViewById(R.id.btn_cancel_dialog);
        btn_ok_dialog =  dialog.findViewById(R.id.btn_ok_dialog);

        btn_cancel_dialog.setOnClickListener(v -> dialog.dismiss());

        btn_ok_dialog.setOnClickListener(v -> {
            dialog.dismiss();
             finish();


        });



    }

    private void toReport_Block(String type,String report_userid)
    {

        progressDialog = ProgressDialog.show(context, "", "Loading...", true);

        Api.getClient().report_block_user(user_id,report_userid,PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,""),type).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                progressDialog.dismiss();
                if(type.equals("1"))
                {
                    genrate_DialogResult("Message","Your Request has been received");
                }else {
                    genrate_DialogResult("Message","You Blocked this User");
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                progressDialog.dismiss();

                if(type.equals("1"))
                {
                    genrate_DialogResult("Message","Your Request has been received");
                }else {
                    genrate_DialogResult("Message","You Blocked this User");
                }
            }
        });


        }

}
