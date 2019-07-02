package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.newczl.androidtraining1.R;


/**
 * 闪屏页
 * author:czl
 */

public class SplashActivity extends BaseActivity {
    private TextView textView;//文本
    private ImageView logo1;
    private ImageView logo2;
    private MyCount myCount;//定时器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.anim_fade_in, 0);//进入的动画
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去除状态栏
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_splash);//设置闪屏页布局
        textView=findViewById(R.id.textView2);//找到文本
        logo1=findViewById(R.id.logo1);//找到学校图片
        logo2=findViewById(R.id.logo2);//学院图片
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.set);//厦门理工动画
        final Animation animation1=AnimationUtils.loadAnimation(this,R.anim.setlogo2);//软件工程学院动画

        logo1.startAnimation(animation);//开始动画
        logo2.startAnimation(animation1);//动画开始
        myCount= (MyCount) new MyCount(4000,1000).start();//4秒后跳转

    }

    
    class MyCount extends CountDownTimer{
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            textView.setText("剩余"+(millisUntilFinished/1000)+"秒 | 跳过");
        }

        @Override
        public void onFinish() {
            textView.setText("跳转中...");//跳转
            Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                //声明意图；
            startActivity(intent);//跳转意图
            finish();//结束掉当前的Activity
        }
    }


    public void skip(View view) {//跳过本页
        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
        //声明意图；
        startActivity(intent);//跳转意图

        finish();//结束掉当前的Activity


        myCount.cancel();//一定要取消掉定时器
    }

    @Override
    public void finish() {//重写finish，加入转场动画
        //overridePendingTransition(0, R.anim.out_top2bottom);// 次数调用无效
        super.finish();
        overridePendingTransition(0, R.anim.anim_fade_out);//结束的动画
    }

}
