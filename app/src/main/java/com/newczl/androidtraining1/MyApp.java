package com.newczl.androidtraining1;

import android.app.Application;

import com.kongzue.dialog.util.DialogSettings;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DialogSettings.style = (DialogSettings.STYLE.STYLE_IOS);// //全局主题风格，提供三种可选风格，STYLE_MATERIAL, STYLE_KONGZUE, STYLE_IOS
    }
}
