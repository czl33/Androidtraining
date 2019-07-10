package com.newczl.androidtraining1.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.newczl.androidtraining1.R;

/**
 * 快速注册页面
 */

public class QuiteReActivity extends BaseWebViewActivity {
    private EditText editText;
    private EditText editText2;
    private TextView timer1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quitere);
        final TextView titleView = findViewById(R.id.title);

        titleView.setText("快速登录");
         editText=findViewById(R.id.edit1);
         editText2=findViewById(R.id.edit2);
         timer1=findViewById(R.id.timer);//时间轴
        timer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer1.setClickable(false);
                mycuttoin mCDT = new mycuttoin(60000, 1000);
                mCDT.start();

            }
        });

    }

    @Override
    protected void menuHandle() {

    }

    public void login(View view) {
    }

    class mycuttoin extends CountDownTimer {

        public mycuttoin(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            timer1.setTextColor(Color.RED);
            timer1.setText(  l / 1000 + "秒后重发");
        }

        @Override
        public void onFinish() {
            timer1.setText("重新发送信息");
            timer1.setTextColor(Color.BLACK);
            timer1.setClickable(true);
        }
    }

}
