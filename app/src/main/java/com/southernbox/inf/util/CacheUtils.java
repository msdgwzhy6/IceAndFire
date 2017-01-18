package com.southernbox.inf.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by SouthernBox on 2016/3/28.
 * SharedPreferences缓存工具类
 */

public class CacheUtils {

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "config", Context.MODE_PRIVATE);
        Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "config", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "config", Context.MODE_PRIVATE);
        Editor edit = sharedPreferences.edit();
        edit.putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "config", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "config", Context.MODE_PRIVATE);
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "config", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defValue);
    }

}
