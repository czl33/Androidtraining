package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.bean.User;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import es.dmoral.toasty.Toasty;

/**
 * 快速注册页面
 */

public class QuiteReActivity extends BaseWebViewActivity {
    private EditText editText;
    private EditText editText2;
    private TextView timer1;
    private User user;

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
                String number=editText.getText().toString();//获取电话号码
                if(number.isEmpty()){
                    Toasty.info(QuiteReActivity.this,"电话号码不能为空哦！",Toasty.LENGTH_SHORT).show();
                    return;
                }
                user=new User();
                user.setUsername(number);
                user.setMobilePhoneNumber(number);//设置号码
                user.setNickName(number).setSex(false).setInfo("这家伙很懒什么都没有留下。");
                BmobFile bf=new BmobFile("timg.jpg","","http://cc.newczl.cn/2019/07/10/e8179c9b40d278cf809e917e75096b75.jpg");
                //bf.setUrl("http://cc.newczl.cn/2019/07/10/e8179c9b40d278cf809e917e75096b75.jpg");
                user.setHeadImage(bf);
                timer1.setClickable(false);
                BmobSMS.requestSMSCode(number, "", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer smsId, BmobException e) {
                        if (e == null) {
                           Toasty.success(QuiteReActivity.this,"发送验证码成功！",Toasty.LENGTH_SHORT).show();
                            mycuttoin mCDT = new mycuttoin(60000, 1000);
                            mCDT.start();
                        } else {
                            Log.i("czlews", "done: "+e.getMessage());
                            Toasty.error(QuiteReActivity.this,"发送验证码失败！code:"+e.getMessage(),Toasty.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }

    @Override
    protected void menuHandle() {

    }

    public void login(View view) {

        String code = editText2.getText().toString();
        if(editText.getText().toString().isEmpty()){
            Toasty.error(QuiteReActivity.this,"电话号码不能为空哦！",Toasty.LENGTH_SHORT).show();
            return;
        }else if(code.isEmpty()){
            Toasty.error(QuiteReActivity.this,"验证码不能为空哦！",Toasty.LENGTH_SHORT).show();
            return;
        }

        user.signOrLogin(code, new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
//                    mTvInfo.append("短信注册或登录成功：" + user.getUsername());
                    Intent data = new Intent();
                    data.putExtra("isLogin", true);
                    data.putExtra("userName", user.getUsername());
                    setResult(RESULT_OK,data);
                    finish();
                    Toasty.success(QuiteReActivity.this,"验证成功！"+user.getUsername(),Toasty.LENGTH_SHORT).show();
                  //  startActivity(new Intent(UserSignUpOrLoginSmsActivity.this, UserMainActivity.class));
                } else {

                    Toasty.error(QuiteReActivity.this,"短信注册或登录失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n",Toasty.LENGTH_SHORT).show();
                }
            }
        });

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
