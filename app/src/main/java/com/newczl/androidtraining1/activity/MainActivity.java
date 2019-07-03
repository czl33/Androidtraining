package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.adapter.MyFragmentStatePagerAdapter;
import com.newczl.androidtraining1.fragment.BaseFragment;
import com.newczl.androidtraining1.fragment.ChartFragment;
import com.newczl.androidtraining1.fragment.HomeFragment;
import com.newczl.androidtraining1.fragment.MeFragment;
import com.newczl.androidtraining1.fragment.VideoFragment;

import java.util.ArrayList;
/**
 * 首页的ACITVITY:
 * author:czl
 */
public class MainActivity extends BaseActivity {
    private ViewPager viewPager;//Fragment的切换
    private BottomNavigationView navigation;//底部导航栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//设置布局
        navigation = findViewById(R.id.navigation);//底部导航栏
        viewPager=findViewById(R.id.viewPager);//找到viewPager
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);//设置底部导航栏按钮事件。meun在布局设置
        ArrayList<BaseFragment> data=new ArrayList<>();//导航栏数据集合
        data.add(new HomeFragment());//主页
        data.add(new ChartFragment());//图表
        data.add(new VideoFragment());//视频
        data.add(new MeFragment());//我
        viewPager.setAdapter(new MyFragmentStatePagerAdapter(getSupportFragmentManager(),data));//设置viewPager的适配器
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){//viewPager滑动监听，完成滑动改变底部导航菜单的状态
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                navigation.getMenu().getItem(position).setChecked(true);//设置当前位置的菜单为点击状态
            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            //底部导航栏的点击事件监听
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home://当选中为首页时，将viewpager切换为第一个Fragement
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_chart://当选中为图表时，将viewpager切换为第二个Fragement
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_video://当选中为视频时，将viewpager切换为第三个Fragement
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_me://当选中为我时，将viewpager切换为第四个Fragement
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };
    public void finish() {//重写finish，加入转场动画
        //overridePendingTransition(0, R.anim.out_top2bottom);// 次数调用无效
        super.finish();
        overridePendingTransition(0, R.anim.anim_fade_out);//结束的动画
    }




}
