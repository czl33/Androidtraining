package com.newczl.androidtraining1.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.utils.PrefUtil;

public abstract class BaseWebViewActivity extends AppCompatActivity {
    private int x1;
    private int y1;

    private String TAG;//当前Activity的信息
    protected Toolbar  toolbar;//工具栏
    public boolean isShow;//是否显示过菜单

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        int theme = PrefUtil.getint(this, "theme", R.style.theme_grass);//设置默认主题
        setTheme(theme);
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题栏
        TAG=getClass().getSimpleName();//打印log时使用的标记
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isShow) {
            toolbar = findViewById(R.id.toolbars);//找到工具栏
            toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);//设置导航按钮
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();//关闭页面
                }
            });
            menuHandle();//菜单的操作在这里执行
            isShow=true;
        }
    }

    protected abstract void menuHandle();//菜单实例化操作

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {//重写事件分发
        switch (ev.getAction()){//获取当前的动作
            case MotionEvent.ACTION_DOWN://按下
                x1= (int) ev.getX();//获取当前手指的坐标
                y1= (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP://手拿起来
                int x2= (int) ev.getX();//获取手拿起来时候的坐标
                int y2= (int) ev.getY();
                int dx=x1-x2;
                int dy=y1-y2;
                if(Math.abs(dx)>Math.abs(dy)){
                    if(dx<0){//左边滑动
                        Log.i("左边滑动", dx+"");
                        if(Math.abs(dx)>350){//如果偏移量大于350就finish掉
                            finish();
                        }
                    }else{//右边滑动(不管)
                        Log.i("右边滑动", dx+"");
                    }
                }
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}
