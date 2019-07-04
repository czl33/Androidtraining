package com.newczl.androidtraining1.utils;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * 封装okhttp逻辑
 */
public  class NetUtils {

    public interface MyCallBack{//外部掉用接口回调
        void onFailure();
        void onResponse(String json);
    }

    public static void getDataAsyn(String url, final MyCallBack callBack){
        OkHttpClient client=new OkHttpClient();//新建类
        final Request request=new Request.Builder()
                .url(url).build();//建立请求
        Call call=client.newCall(request);//发起请求
        call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Toasty.error(activity, "刷新成功!", Toast.LENGTH_SHORT, true).show();//吐司
                    //数据池借用消息
                    callBack.onFailure();//执行传进来的失败回调
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    // Log.i(TAG, response.body().string());
                    ResponseBody body = response.body();//获取请求的身体

                    if(body!=null){//如果不为空
                        String str=body.string();//将转换为字符串
//                    Log.i(TAG, str);
                        callBack.onResponse(str);//将获取的字符串传进外部回调

                    }
                }
            });



    }
}
