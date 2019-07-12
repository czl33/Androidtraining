package com.newczl.androidtraining1.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.utils.PrefUtil;

import es.dmoral.toasty.Toasty;

/**
 * 基页:其他的ACtivit继承自本页
 * author:czl
 */
public abstract class BaseActivity extends AppCompatActivity {
    private String TAG;//当前Activity的信息
    private long exitTime;//退出的时间，默认为0
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        int theme = PrefUtil.getint(this, "theme", R.style.AppTheme);//设置默认主题
        setTheme(theme);
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题栏
        TAG=getClass().getSimpleName();//打印log时使用的标记
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){//按下返回键
            if(System.currentTimeMillis()-exitTime>2000){
                Toasty.info(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
