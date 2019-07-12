package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.adapter.MyFragmentStatePagerAdapter;
import com.newczl.androidtraining1.fragment.BaseFragment;
import com.newczl.androidtraining1.fragment.ChartFragment;
import com.newczl.androidtraining1.fragment.HomeFragment;
import com.newczl.androidtraining1.fragment.MeFragment;
import com.newczl.androidtraining1.fragment.VideoFragment;
import com.newczl.androidtraining1.view.MainViewPager;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
/**
 * 首页的ACITVITY:
 * author:czl
 */
public class MainActivity extends BaseActivity {
    public static final int REQUEST_CODE_SCAN = 0x1234;
    private MainViewPager viewPager;//Fragment的切换
    private BottomNavigationView navigation;//底部导航栏



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//设置布局
        navigation = findViewById(R.id.navigation);//底部导航栏
        viewPager=findViewById(R.id.viewPager);//找到viewPager
        boolean toggle = getIntent().getBooleanExtra("toggle", false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);//设置底部导航栏按钮事件。meun在布局设置
        ArrayList<BaseFragment> data=new ArrayList<>();//导航栏数据集合
        data.add(new HomeFragment());//主页
        data.add(new ChartFragment());//图表
        data.add(new VideoFragment());//视频
        data.add(new MeFragment());//我
        viewPager.setAdapter(new MyFragmentStatePagerAdapter(getSupportFragmentManager(),data));//设置viewPager的适配器
        viewPager.setOffscreenPageLimit(4);//缓存数量为4个页面
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){//viewPager滑动监听，完成滑动改变底部导航菜单的状态
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                navigation.getMenu().getItem(position).setChecked(true);//设置当前位置的菜单为点击状态
            }
        });
       // Log.i("www2", "onCreate: "+toggle);
        if(toggle){
            viewPager.setCurrentItem(3);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_SCAN){
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    if(result!=null) {
                        if (result.contains("www") || result.toLowerCase().contains("http") ) {
                            Intent intent=new Intent(MainActivity.this, NewsDetailActivity.class);
                            if(result.contains("http") || result.contains("Http")){
                                intent.putExtra("url",result);
                            }else{
                                intent.putExtra("url","http://"+result);
                            }
                            startActivity(intent);
                        }
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
