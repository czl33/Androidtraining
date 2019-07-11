package com.newczl.androidtraining1.activity.QQListener;

import android.util.Log;

import com.google.gson.Gson;
import com.newczl.androidtraining1.bean.QQLoginBean;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class BaseUiListener implements IUiListener {

    public BaseUiListener(){};

    private QQLoginBean qqLoginBean;

    public QQLoginBean getQqLoginBean() {
        return qqLoginBean;
    }

    @Override
    public void onComplete(Object response) {
        if (null == response) {
            Log.i("返回为空", "登录失败");
            return;
        }
        JSONObject jsonResponse = (JSONObject) response;
        if (null != jsonResponse && jsonResponse.length() == 0) {
            Log.i("返回为空", "登录失败");
            return;
        }
        Log.i("sd", "登录成功");
        Log.i("sd", "onComplete: "+jsonResponse.toString());
        Gson gson=new Gson();
        qqLoginBean = gson.fromJson(jsonResponse.toString(), QQLoginBean.class);


        // 有奖分享处理
//            handlePrizeShare();

    }


    @Override
    public void onError(UiError e) {
        Log.i("onError: ", e.errorDetail);

    }

    @Override
    public void onCancel() {
        Log.i("ss", "onCancel: ");
    }
}
