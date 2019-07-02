package com.newczl.androidtraining1.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;
/**
 * 基页:其他的ACtivit继承自本页
 * author:czl
 */
public abstract class BaseActivity extends AppCompatActivity {
    private String TAG;//当前Activity的信息
    private long exitTime;//退出的时间，默认为0

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题栏
        TAG=getClass().getSimpleName();//打印log时使用的标记
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){//按下返回键
            if(System.currentTimeMillis()-exitTime>2000){
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
            }else{
                finish();//结束当前Activity
                System.exit(0);
            }
            return true;//不在进行事件传递
        }
        return super.onKeyDown(keyCode, event);
    }
}
