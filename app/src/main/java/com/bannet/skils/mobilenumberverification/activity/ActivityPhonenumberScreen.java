package com.bannet.skils.mobilenumberverification.activity;
//7896546787
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bannet.skils.service.localdatabase.SqliteDatabase;
import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.explore.response.CategoryLocalREsponse;
import com.bannet.skils.explore.response.CategoryResponce;
import com.bannet.skils.login.actvity.ActivityLoginScreen;
import com.bannet.skils.otpverification.activity.ActivityOtpScreen;
import com.bannet.skils.mobilenumberverification.responce.GetOtpModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPhonenumberScreen extends AppCompatActivity {

    private Context context;
    public AppCompatButton nextbtn;
    private EditText enterMobileNumber;
    public String mobilenumber,USER_ID,newPhoneNumber;
    public String languageResponce;
    public Intent intent;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private ProgressDialog progressDialog;

    public TextView p_s_tv5,skipbtn,login,alreadyAccount;
    public Resources resources;
    private String toNumber="",messageContent="";
    private CountryCodePicker countryCodePicker;
    private String otp,phn_number;
    public final int REQUEST_READ_EXTERNAL_STORAGE = 1;

    private SqliteDatabase mDatabase;
    ArrayList<CategoryLocalREsponse> allContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber_screen);

        if(getSupportActionBar() != null){

            getSupportActionBar().hide();
        }

        context=ActivityPhonenumberScreen.this;

        p_s_tv5=findViewById(R.id.p_s_tv5);
        alreadyAccount=findViewById(R.id.p_s_tv7);
        login=findViewById(R.id.login);
        nextbtn = findViewById(R.id.phonenumber_screen_next_btn);
        countryCodePicker = findViewById(R.id.ccp);
        enterMobileNumber=findViewById(R.id.number);
        skipbtn = findViewById(R.id.skipbtn);

        mDatabase = new SqliteDatabase(context);
        allContacts = mDatabase.listContacts();

        catgorylListadd();

        callMultiplePermissions();

        p_s_tv5.setText(GlobalMethods.getString(context,R.string.mobile_number));
        skipbtn.setText(GlobalMethods.getString(context,R.string.skip_now));
        login.setText(GlobalMethods.getString(context,R.string.login));
        alreadyAccount.setText(GlobalMethods.getString(context,R.string.already_you_have_an_account_please));
        enterMobileNumber.setHint(GlobalMethods.getString(context,R.string.enter_mobile_number));
        nextbtn.setText(GlobalMethods.getString(context,R.string.submit));


        languageResponce = PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"");

        login.setOnClickListener(view -> {

            Intent intent = new Intent(context, ActivityLoginScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });

        if(languageResponce.isEmpty()){

            languageResponce = "en";

        }

        nextbtn.setOnClickListener(view -> {

            if(validation()){

                registerMobilenumber();

            }

        });

        skipbtn.setOnClickListener(view -> {

            if(isPermissionGrantedFirst()){

                if(!isLocationEnabled(context))

                    showMessageEnabledGPS();

                else{

                    intent = new Intent(context, ActivityBottom.class);
                    PrefConnect.writeString(context,PrefConnect.USER_ID,"customer");
                    PrefConnect.writeString(context,PrefConnect.USER_ID_REGISTER_COMPLETED,"customer");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            }
            else{
                isPermissionGranted();
            }


        });


    }

    private void catgorylListadd() {

        if(GlobalMethods.isNetworkAvailable(context)){

            Api.getClient().categoryList(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"")).enqueue(new Callback<CategoryResponce>() {
                @Override
                public void onResponse(@NonNull Call<CategoryResponce> call, @NonNull Response<CategoryResponce> response) {

                    assert response.body() != null;
                    if(response.body().getStatus().equals("1")){
                        mDatabase.deletaAll();
                        for (int i = 0;i < response.body().getCategoryList().size();i++){


                            CategoryLocalREsponse newContact = new CategoryLocalREsponse(response.body().getCategoryList().get(i).getCategoryId(),
                                    response.body().getCategoryList().get(i).getCategoryName(),response.body().getCategoryList().get(i).getImageUrl() +
                                    response.body().getCategoryList().get(i).getCategoryImage());

                            mDatabase.addContacts(newContact);

                        }

                    }else {

                        // GlobalMethods.Toast(context,response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CategoryResponce> call, @NonNull Throwable t) {

                    Log.e("failure professional res",t.getMessage());

                }
            });
        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }
    }


    private void registerMobilenumber() {

        phn_number =  countryCodePicker.getSelectedCountryCodeWithPlus() + enterMobileNumber.getText().toString();
        newPhoneNumber = enterMobileNumber.getText().toString().trim();

        Log.e("languageResponce",languageResponce);
        if (GlobalMethods.isNetworkAvailable(context)) {

            progressDialog = ProgressDialog.show(ActivityPhonenumberScreen.this, "", "Loading...", true);
            Api.getClient().RegisterMobileNumber(phn_number,languageResponce).enqueue(new Callback<GetOtpModel>() {
                @Override
                public void onResponse(@NonNull Call<GetOtpModel> call, @NonNull Response<GetOtpModel> response) {
                    Log.e("suess", new Gson().toJson(response.body()));

                    progressDialog.dismiss();

                    if(response.isSuccessful()){

                        assert response.body() != null;
                        if(response.body().getStatus().equals("1")){

                            otp = response.body().getOtp();

                            USER_ID = response.body().getUserId();

                            PrefConnect.writeString(context,PrefConnect.USER_MOBILE,enterMobileNumber.getText().toString());
                            PrefConnect.writeString(context,PrefConnect.USER_ID,USER_ID);
                            Intent in = new Intent(context, ActivityOtpScreen.class);
                            in.putExtra("otp",otp);
                            Log.e("otp",otp);
                            in.putExtra("phone_number",phn_number);
                            in.putExtra("user_id",USER_ID);
                            in.putExtra("type","register");
                            in.putExtra("usertype",response.body().getType());
                            in.putExtra("code",response.body().getRepeatedUser());
                            startActivity(in);
                            finish();

                            messageContent="Skilsss App OTP For Registration - "+ otp;
                            toNumber = phn_number;

//                            ExecuteTaskMsgSendTwillio gt = new ExecuteTaskMsgSendTwillio();
//                            gt.execute(toNumber,messageContent);
                            NewSmsService newSmsService = new NewSmsService();
                            newSmsService.execute();

                        }else {
                            GlobalMethods.Toast(context,response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetOtpModel> call, @NonNull Throwable t) {
                    progressDialog.dismiss();

                }
            });

        }
        else {

            GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));

        }

    }

    private boolean isLocationEnabled(Context context){
//        String locationProviders;
        boolean enabled = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            enabled = (mode != Settings.Secure.LOCATION_MODE_OFF);
        }else{
            LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            enabled =  service.isProviderEnabled(LocationManager.GPS_PROVIDER)||service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return enabled;
    }

    public void showMessageEnabledGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.this_service_requires_the_activation_of_the_gps))
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public boolean isPermissionGrantedFirst() {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
        }
        else {

            return true;
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
                return true;
            } else {

                //Toast.makeText(Accueil.this, "Permission is revoked", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(ActivityPhonenumberScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_READ_EXTERNAL_STORAGE);
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }


    class ExecuteTaskMsgSendTwillio extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String toNumber=params[0];
            String messageContent=params[1];

            String res = PostDataMsgSendTwillio(toNumber,messageContent);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                Log.e("JSONobjectTwillio", jsonObject.toString());
                String error_code = jsonObject.getString("error_code");
                String error_message = jsonObject.getString("error_message");
                //Toast(context, message);
                Log.e("error_code", error_code+" ,error_message: "+error_message);


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSONobjectTwillio", e.getMessage());
            }
        }
    }

    //Submit the answer to server using api
    public static String PostDataMsgSendTwillio(String toNumber,String messageContent) {

        Log.e("toNumber", toNumber + " nan");
        Log.e("messageContent", messageContent + " nan");
        String response = "";
        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "From=+13069881772&Body="+messageContent+"&To="+toNumber);

            //  RequestBody body = RequestBody.create(mediaType, "From=+13069881772&Body="+messageContent+"&To="+toNumber);
            Request request = new Request.Builder()
                    .url("https://api.twilio.com/2010-04-01/Accounts/AC4a3f99546a10166610c1e9bfca75cce4/Messages.json")
                    .method("POST", body)
                    .addHeader("Authorization", "Basic QUM0YTNmOTk1NDZhMTAxNjY2MTBjMWU5YmZjYTc1Y2NlNDoyYjkyMzRjN2VkNWIyZmJmNmFlZjNlNzVmOWI4MDcwZA==")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            response = client.newCall(request).execute().body().string();
            return response;

        } catch (Exception exception) {
        }
        return response;
    }


    public class NewSmsService extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            String apiUrl = "https://www.fast2sms.com/dev/bulkV2?authorization=0FGTHC6UycDIbOZ4q5hixA8pPvNkjlKVoJXe1rdBn3SL9RstWzJ1fVHzuDyiLogNvAjOGbPla09Epr6m&route=q&message=Your Skillsss OTP is "+otp+":&flash=0&numbers="+newPhoneNumber+"";
            //JulMiZpYqHW96r5TV0b8mtXs2hOQxLKUe34NGDvdfCSRcAjFngRNATueXbF8EypaWC47Odl60JBQKGYL
            String apiUrl1 = "https://www.fast2sms.com/dev/bulkV2?authorization=0FGTHC6UycDIbOZ4q5hixA8pPvNkjlKVoJXe1rdBn3SL9RstWzJ1fVHzuDyiLogNvAjOGbPla09Epr6m&route=otp&variables_values="+otp+"&flash=0&numbers="+newPhoneNumber+"";
            //String apiUrl1 = "https://www.fast2sms.com/dev/bulkV2?authorization=JulMiZpYqHW96r5TV0b8mtXs2hOQxLKUe34NGDvdfCSRcAjFngRNATueXbF8EypaWC47Odl60JBQKGYL&route=otp&variables_values="+otp+"&flash=0&numbers="+newPhoneNumber+"";

            try {
                URL url = new URL(apiUrl1);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Optional: Set request headers if required

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }

                    bufferedReader.close();
                    inputStream.close();
                } else {
                    result = "Error: " + responseCode;
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                result = "Error: " + e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the API response here (update UI or perform other actions)
            Log.d("API_CALL_RESULT", result);


        }
    }

    private boolean validation() {
        mobilenumber=enterMobileNumber.getText().toString().trim();

        if(mobilenumber.isEmpty()){
            Toast.makeText(context,GlobalMethods.getString(context,R.string.enter_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void callMultiplePermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ EXTERNAL STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE EXTERNAL STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("ACCESS_FINE_LOCATION");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("ACCESS_COARSE_LOCATION");
        if (!addPermission(permissionsList, Manifest.permission.POST_NOTIFICATIONS))
            permissionsNeeded.add("POST_NOTIFICATIONS");
        if (!addPermission(permissionsList, Manifest.permission.READ_MEDIA_IMAGES))
            permissionsNeeded.add("READ_MEDIA_IMAGES");
        if (!addPermission(permissionsList, Manifest.permission.READ_MEDIA_VIDEO))
            permissionsNeeded.add("READ_MEDIA_VIDEO");

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
                }

                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }

            return;
        }

    }
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
                perms.put(android.Manifest.permission.READ_MEDIA_IMAGES, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_MEDIA_VIDEO, PackageManager.PERMISSION_GRANTED);

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
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
                ) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    //Toast.makeText(context, "Permissin is denied", Toast.LENGTH_SHORT).show();

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}