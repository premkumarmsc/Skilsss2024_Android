package com.bannet.skils.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bannet.skils.login.responce.LoginModel;
import com.google.gson.Gson;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Config {


    public static Gson gson = new Gson();

    public static Context context;
    public static SharedPreferences.Editor editPreferences;
    public static SharedPreferences sharedPreferences;
    public static LoginModel.Details currentUser;
    public static String PREFERENCE = "PREFERENCE";
    public static String USER = "USER";
    public static String PUSH_NOTIFICATION_ENABLED = "PUSH_NOTIFICATION_ENABLED";

    public static String getString(String s) {
        return Config.sharedPreferences.getString(s, "");
    }

    public static boolean getBoolean(String s) {
        return Config.sharedPreferences.getBoolean(s, false);
    }

    public static void setString(String key, String s) {
        Config.editPreferences.putString(key, s).apply();
    }

    public static void setString(String key, boolean s) {
        Config.editPreferences.putBoolean(key, s).apply();
    }

    public static RequestBody getMultiPartText(String s) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), s);

        return requestBody;
    }

    public static MultipartBody.Part getMultiPartFile(File finalFile) {
        //pass it like this
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), finalFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file_name", finalFile.getName(), requestFile);

        return body;
    }

    public static void shareCard(Activity activity) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Ipseitie");
        String shareMessage = "Ipseitie\n" + "Card Holder : Name\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        activity.startActivity(Intent.createChooser(shareIntent, "choose one"));
    }

    public static void shareCard(Activity activity, String shareMessage) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Ipseitie");
//         shareMessage = "Ipseitie\n" + "Card Holder : Name\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        activity.startActivity(Intent.createChooser(shareIntent, "choose one"));
    }

    public static void clearCurrentUser() {
        Config.setString(Config.USER, null);
    }

    public static String formatMessageTime(String startTime) {
        startTime = "2013-02-27 21:06:30";
        StringTokenizer tk = new StringTokenizer(startTime);
        String date = tk.nextToken();
        String time = tk.nextToken();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt;
        try {
            dt = sdf.parse(time);
            System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here
            return sdfs.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";

    }

    public static LoginModel.Details getCurrentUser() {
        return Config.gson.fromJson(Config.sharedPreferences.getString(Config.USER, null), LoginModel.Details.class);
    }
}
