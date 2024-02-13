package com.bannet.skils.splash.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.introduction.activity.ActivityIntroduction;
import com.bannet.skils.mobilenumberverification.activity.ActivityPhonenumberScreen;
import com.google.firebase.messaging.FirebaseMessaging;

import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.R;
import com.bannet.skils.service.Api;
import com.bannet.skils.service.GlobalMethods;
import com.bannet.skils.service.PrefConnect;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplashScreen extends AppCompatActivity {

    Handler handler;
    Context context;
    FusedLocationProviderClient mFusedLocationClient;

    // Initializing other items
    // from layout file
    String latitude, longitude;
    int PERMISSION_ID = 44;
    String deviceToken;
    Intent intent;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = ActivitySplashScreen.this;

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        init();
        // callMultiplePermissions();

    }
    private void init(){

        if(!gpsisLocationEnabled(context)) {

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            finish();

        }

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {

                Log.e("task", String.valueOf(task.getException()));
                return;

            }
            // Get new FCM registration token
            String token = task.getResult();
            deviceToken = token+"";
            Log.e("deviceToken",deviceToken);
            PrefConnect.writeString(context,PrefConnect.DEVICE_TOKEN,token);

        });

        context = ActivitySplashScreen.this;

        if(PrefConnect.readString(context,PrefConnect.LANGUAGE_RESPONCE,"").isEmpty()){

            PrefConnect.writeInteger(context,PrefConnect.LANGUAGE,1);
            PrefConnect.writeString(context,PrefConnect.LANGUAGE_RESPONCE,"en");

        }

        final String userRegister = PrefConnect.readString(ActivitySplashScreen.this, PrefConnect.USER_ID_REGISTER_COMPLETED,"");

        String UserId = PrefConnect.readString(context,PrefConnect.USER_ID,"");
        Log.e("userRegister",userRegister);
        handler = new Handler();
        handler.postDelayed(new Runnable() {


            @Override
            public void run() {

                if(GlobalMethods.isNetworkAvailable(ActivitySplashScreen.this)){

                    if(userRegister.equals("yes")){

                        userCheck(UserId);

                    }
                    else if(userRegister.equals("customer")){

                        intent = new Intent(context, ActivityBottom.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                    else {

                        Intent intent=new Intent(context, ActivityIntroduction.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }

                }
                else{
                    GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.No_Internet_Connection));
                }

            }
        },3000);
    }

    private boolean gpsisLocationEnabled(Context context){
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

    public void userCheck(String id){

        if(GlobalMethods.isNetworkAvailable(ActivitySplashScreen.this)){
            Api.getClient().CheckUser(id).enqueue(new Callback<CommonModel>() {

                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    Log.e("splash", new Gson().toJson(response.body()));

                    if (response.isSuccessful()){

                        assert response.body() != null;

                        if(response.body().getStatus().equals("1")){

                            if(!gpsisLocationEnabled(context)){

                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                finish();

                            }

                            else{

                                intent = new Intent(context, ActivityBottom.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }

//                            intent = new Intent(context, ActivityAddprofileScreen.class);
//                            intent.putExtra("code","0");
//                            intent.putExtra("user_id","84");

                        }
                        else {

                            intent = new Intent(context, ActivityPhonenumberScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }

                    }

                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {

                    Log.e("splash","error "+t.getMessage().toString());

                }
            });
        }
        else {
            GlobalMethods.Toast(context, "No Internet Connection");
        }
    }

  /*  @SuppressLint("MissingPermission")
    private void getLastLocation() {

        if (checkPermissions()) {

            if (isLocationEnabled()) {

                   mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                            Log.e("longitude","dfgdf");

                        } else {

                            Geocoder geocoder=new Geocoder(ActivitySplashScreen.this, Locale.getDefault());

                            try{
                                List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),
                                        1);

                                PrefConnect.writeString(context,PrefConnect.LATITUDE,String.valueOf(addressList.get(0).getLatitude()));
                                PrefConnect.writeString(context,PrefConnect.LOGITUDE,String.valueOf(addressList.get(0).getLongitude()));
                                String address=addressList.get(0).getAddressLine(0);
                                PrefConnect.writeString(context,PrefConnect.ADDRESS,addressList.get(0).getAddressLine(0));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            latitude= String.valueOf(location.getLatitude());
                            longitude= String.valueOf(location.getLongitude());
                            Log.e("longitude",longitude);
                            Log.e("latitude",latitude);

                            PrefConnect.writeString(context,PrefConnect.LATITUDE,latitude);
                            PrefConnect.writeString(context,PrefConnect.LOGITUDE,longitude);


                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

*/

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {


        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude= String.valueOf(mLastLocation.getLatitude());
            longitude= String.valueOf(mLastLocation.getLongitude());
//            Log.e("latitude",latitude);
//            Log.e("longitude",longitude);
        }
    };

    // If everything is alright then

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
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
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
                )
                {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    GlobalMethods.Toast(context, GlobalMethods.getString(context,R.string.Permissin_is_denied));

                }

                if (requestCode == PERMISSION_ID) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     //   getLastLocation();
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}