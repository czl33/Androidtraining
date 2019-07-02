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
    private FragmentActivity activity;//当前Activity
    private String TAG;//标记名

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

    private int setLayoutID() {//获取布局文件的RId
        return 0 ;
    }

}
