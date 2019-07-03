package com.newczl.androidtraining1.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * 基础Fragment:其他页面的Fragment继承自本Fragment
 * author:czl
 */
public class BaseFragment extends Fragment {
    protected FragmentActivity activity;//当前Activity
    protected String TAG;//标记名

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=getActivity();//得到Activity
        TAG=getClass().getSimpleName();//获得当前类的标记名，用于Log
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if(setLayoutID()==0){
            TextView textView=new TextView(activity);
            textView.setText(TAG);
            view=textView;//默认设置一个TextView
        }else{
            view=inflater.inflate(setLayoutID(),container,false);//布局在父容器进行实例化
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {//声明周期函数在 activity创建好后在加载数据也就是在onViewCreated
        super.onActivityCreated(savedInstanceState);
        initData();//初始化数据
    }

    protected void initData() {//c初始化数据

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {//在创建好视图时加载fragment初始化视图
        super.onViewCreated(view, savedInstanceState);
        initView(view);//初始化视图方法

    }

    protected int setLayoutID() {//获取布局文件的RId
        return 0 ;
    }

    protected void initView(View view){//初始化布局

    }

}
