package com.newczl.androidtraining1;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.kongzue.dialog.util.DialogSettings;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.bmob.v3.Bmob;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"b53ddcbb968ffbbe5e6de1105dba728f");
        DialogSettings.style = (DialogSettings.STYLE.STYLE_IOS);// //全局主题风格，提供三种可选风格，STYLE_MATERIAL, STYLE_KONGZUE, STYLE_IOS

        ZXingLibrary.initDisplayOpinion(this);
        //百度地图
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
