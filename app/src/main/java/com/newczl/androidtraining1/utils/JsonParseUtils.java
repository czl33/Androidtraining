package com.newczl.androidtraining1.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newczl.androidtraining1.bean.ConstellationBean;
import com.newczl.androidtraining1.bean.NewsBean;
import com.newczl.androidtraining1.bean.PythonBean;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ParameterMetaData;
import java.util.List;

/**
 * json帮助类
 */
public class JsonParseUtils {


    public static <T>List<T> getList(Class<T> t,String json){//解析新闻类json数据，返回一个集合新闻类
        Gson gson=new Gson();
        Type listType=new MyParameterizedType(t);
        return gson.fromJson(json,listType);
    }
    private  static class MyParameterizedType implements ParameterizedType{

        Class raw;

        public MyParameterizedType(Class raw) {
            this.raw = raw;
        }

        @NotNull
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{raw};
        }


        @NotNull
        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

}
