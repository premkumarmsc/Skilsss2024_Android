package com.bannet.skils.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class PrefConnect {
    public static final String PREF_NAME = "MY_PREF";
    public static final int MODE = Context.MODE_PRIVATE;

    public static String USER_TYPE = "user_type";
    public static String USER_ID = "user_id";
    public static String USER_ID_REGISTER_COMPLETED = "user_id_completed";
    public static String USER_PROFILE_VERIFIED_STATUS = "profile_verified_status";
    public static String USER_Mobilenumber = "mobile_number";
    public static String USER_IMAGE_URL = "user_image_url";
    public static String USER_IMAGE_NAME = "user_image_name";
    public  static Integer LANGUAGE=1;
    public static String USER_IDENTIFICATION_IMAGE_URL = "user_identificatin_url";
    public static String USER_EMAIL = "user_email";
    public static String USER_PASSWORD = "user_password";
    public static String USER_NAME = "user_name";
    public static String USER_SELECTED_CERTIFICATE = "user_certificates";
    public static String USER_SELECTED_SKIS_id = "user_skils_id";
    public static String USER_SELECTED_SKIS_NAME = "user_skils_name";
    public static String USER_MOBILE = "user_mobile";
    public static String DEVICE_TOKEN = "device_tokeen";
    public static String DEVICE_VERSION = "devide_version";
    public static String DEVICE_MODEL = "device_model";
    public static String DEVICE_TYPE = "device_type";
    public static String LANGUAGE_ID = "language_id";
    public static String LANGUAGE_NAME = "language_name";
    public static String CURRENT_PLAN = "current_plan";
    public static String LATITUDE = "latitude";
    public static String LOGITUDE = "logitude";
    public static String ADDRESS = "address";
    public static String LANGUAGE_RESPONCE = "language_responce";
    public static String POST_ID = "post_id";

    public static String PLAN_ONE = "plan_one";
    public static String PLAN_TWO = "plan_two";
    public static String PLAN_THREE = "plan_three";
    public static String PLAN_FOUR = "plan_three";

    public static String SKILSS_ID = "skilss_id";


    public static void clearAllPrefs(Context context) {
        getEditor(context).clear().commit();
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void writeInteger(Context context, Integer key, int value) {
        getEditor(context).putInt(String.valueOf(key), value).commit();

    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int readInteger(Context context, Integer key, int defValue) {
        return getPreferences(context).getInt(String.valueOf(key), defValue);
    }


    /**
     * @param context
     * @param key
     * @param value
     */
    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    /**
     * @param context
     * @return
     */
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    /**
     * @param context
     * @return
     */
    public static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }


}
