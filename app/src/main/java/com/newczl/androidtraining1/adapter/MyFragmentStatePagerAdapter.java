package com.newczl.androidtraining1.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.newczl.androidtraining1.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * MainActivity的ViewPager的适配器:
 * author:czl
 */
public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<BaseFragment> data;

    public MyFragmentStatePagerAdapter(FragmentManager fm,ArrayList<BaseFragment> data) {
        super(fm);
        this.data=data;//接受传进来得到集合
    }

    @Override
    public Fragment getItem(int i) {
        return data.get(i);//获取当前选择得到Fragement
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
