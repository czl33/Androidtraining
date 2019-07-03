package com.newczl.androidtraining1.utils;

import android.content.Context;
import android.content.SharedPreferences;

/***
 * 对sharePreference进行封装
 * */
public class PrefUtil {
    public static boolean getBoolean(Context context,String key,boolean defValue){   //获取一个boolean的配置信息
        SharedPreferences config = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return   config.getBoolean(key, defValue);
    }
    public static void setBoolean(Context context,String key,boolean defValue){   //设置一个boolean的配置信息
        SharedPreferences config = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        config.edit().putBoolean(key, defValue).commit();
    }
    public static String getString(Context context,String key,String defValue){   //获取一个String的配置信息
        SharedPreferences config = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return   config.getString(key, defValue);
    }
    public static void setString(Context context,String key,String defValue){   //设置一个String的配置信息
        SharedPreferences config = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        config.edit().putString(key, defValue).commit();
    }
    public static int getint(Context context,String key,int defValue){   //获取一个int的配置信息
        SharedPreferences config = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return   config.getInt(key, defValue);
    }
    public static void setint(Context context,String key,int defValue){   //设置一个int的配置信息
        SharedPreferences config = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        config.edit().putInt(key, defValue).commit();
    }
}
