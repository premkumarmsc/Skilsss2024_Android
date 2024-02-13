package com.bannet.skils.service;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.bannet.skils.service.languagechangeclass.LocaleHelper;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalMethods {


    public static String[] mediaColumns = {MediaStore.Video.Media._ID};

    public static String FIREBASE_CHAT_URL="https://skill-1301.firebaseio.com/";

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$";

    public static String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static long getFileId(Activity context, Uri fileUri) {
        Cursor cursor = context.managedQuery(fileUri, mediaColumns, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int id = cursor.getInt(columnIndex);
            return id;
        }
        return 0;
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static void Toast(Context context, String Message) {
        Toast toast = Toast.makeText(context, Message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static String getString(Context context, int String) {
        Resources resources;
        String  outout = null;

        if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==1) {

            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();
            outout = resources.getString(String);

        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==2)
        {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();
            outout = resources.getString(String);

        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==3)
        {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();
            outout = resources.getString(String);



        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==4)
        {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();
            outout = resources.getString(String);



        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==5)
        {
            context = LocaleHelper.setLocale(context, "fr");
            resources = context.getResources();
            outout = resources.getString(String);



        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==6)
        {
            context = LocaleHelper.setLocale(context, "es");
            resources = context.getResources();
            outout = resources.getString(String);



        }
        else if(PrefConnect.readInteger(context,PrefConnect.LANGUAGE,1)==7)
        {
            context = LocaleHelper.setLocale(context, "ar");
            resources = context.getResources();


        }

        return outout;
    }

    public static final boolean validateEmail(String target) {
        if (target !=null && target.length() > 1) {
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(target);
            return matcher.matches();
        } else if (target.length() == 0) {
            return false;
        } else {
            return false;
        }
    }
    public static boolean validateEmailAddressFormat(String emailAddress) {

        //android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();

        String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }
    public static String getUniqueId(Context c) {
        return Settings.Secure.getString(c.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }



    public static String Date(String date){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("dd MMM yyyy");
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(date);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }

    public static String Date1(String date){

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd");
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(date);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }
    public static String DateConverdion(String date){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("dd MMM yyyy");
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(date);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }
    public static String DobConversion(String date){

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd");
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(date);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }

    public static String DobConversion1(String date){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("dd/MM/yyyy");
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(date);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }

    public static String DobConversion2(String date){

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("dd MMM yyyy");
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(date);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }

    public static String DobConversion4(String date,Locale spanish_locale){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", spanish_locale);
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd",spanish_locale);
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(date);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }

    public static String DateTime24(String date){

        DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(date);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }

    public static String DateTime12(String date){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(date);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }

    public static String TimeConversion(String date){

        DateFormat df = new SimpleDateFormat("hh:mm a");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("hh:mm a");
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(date);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }
    public static String TimeConversionHome(String date){

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("hh:mm a");
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(date);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String Time24TO12(String time){
        String output = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            final Date dateObj = sdf.parse(time);

            output = new SimpleDateFormat("hh:mm a").format(dateObj);

        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String Time12TO124(String time){
        Log.e("TIME",time);
      /*  String output = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            final Date dateObj = sdf.parse(time);

            output = new SimpleDateFormat("HH:mm:ss").format(dateObj);

        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return output;*/

        DateFormat df = new SimpleDateFormat("hh:mm a");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
        Date dates = null;
        String output = null;
        try{
            //Converting the input String to Date
            dates= df.parse(time);
            //Changing the format of date and storing it in String
            output = outputformat.format(dates);
            //Displaying the date

        }catch(ParseException pe){
            pe.printStackTrace();
            Log.e("Exception",pe.getMessage());
        }

        return output;
    }

}
