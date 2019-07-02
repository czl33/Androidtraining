package com.newczl.androidtraining1.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newczl.androidtraining1.bean.ConstellationBean;
import com.newczl.androidtraining1.bean.NewsBean;
import com.newczl.androidtraining1.bean.PythonBean;

import java.lang.reflect.Type;
import java.util.List;

/**
 * json帮助类
 */
public class JsonParseUtils {
    private static Gson gson=new Gson();

    public static List<NewsBean> getNewsList(String json){//解析新闻类json数据，返回一个集合新闻类
        Type listType=new TypeToken<List<NewsBean>>(){}.getType();
        return gson.fromJson(json,listType);
    }

    public static List<NewsBean> getADList(String json){//解析广告类json数据，返回一个集合广告类
        Type listType=new TypeToken<List<NewsBean>>(){}.getType();
        return gson.fromJson(json,listType);
    }

    public static List<PythonBean> getPythonList(String json){//解析python类json数据，返回一个集合python类
        Type listType=new TypeToken<List<PythonBean>>(){}.getType();
        return gson.fromJson(json,listType);
    }

    public static List<ConstellationBean> getConstellationBeanList(String json){//解析星座类json数据，返回一个集合星座类
        Type listType=new TypeToken<List<ConstellationBean>>(){}.getType();
        return gson.fromJson(json,listType);
    }

}
