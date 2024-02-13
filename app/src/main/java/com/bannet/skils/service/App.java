package com.bannet.skils.service;

import android.app.Application;
import android.os.Build;

import com.google.gson.GsonBuilder;

public class App extends Application {

    public static String deviceModel;
    public static String deviceVersion;
    public static String deviceType;
    public static String deviceToken;

    @Override
    public void onCreate() {
        super.onCreate();

        Config.context = getApplicationContext();
        Config.sharedPreferences = getSharedPreferences(Config.PREFERENCE, 0);
        Config.editPreferences = Config.sharedPreferences.edit();
        Config.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        deviceModel = Build.MODEL;
        deviceVersion = Build.VERSION.RELEASE;
        deviceType = Build.MANUFACTURER;
        deviceToken = "";

    }
}
