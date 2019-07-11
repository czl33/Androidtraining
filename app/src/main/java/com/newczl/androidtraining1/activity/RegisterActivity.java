package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.bean.User;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseWebViewActivity {

    private EditText editText;
    private EditText editText2;
    private EditText editText3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView titleView = findViewById(R.id.title);
        titleView.setText("注册");
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
    }
    public void register(final View view){
        final String userName = editText.getText().toString();
        String password = editText2.getText().toString();
        String email = editText3.getText().toString();
        if(TextUtils.isEmpty(userName)){
            editText.setError("用户名不能为空！");
            return;
        }
        if(TextUtils.isEmpty(password)){
            editText2.setError("用户名不能为空！");
            return;
        }
        if(TextUtils.isEmpty(email)){
            editText3.setError("邮箱不能为空！");
            return;
        }
        final User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(email);

        user.setNickName("无").setSex(false).setInfo("这家伙很懒什么都没有留下。");
        BmobFile bf=new BmobFile("timg.jpg","","http://cc.newczl.cn/2019/07/10/e8179c9b40d278cf809e917e75096b75.jpg");

        //bf.setUrl("http://cc.newczl.cn/2019/07/10/e8179c9b40d278cf809e917e75096b75.jpg");
        user.setHeadImage(bf);

        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    Intent data = new Intent();
                    data.putExtra("userName",userName);
                    setResult(RESULT_OK,data);
                    RegisterActivity.this.finish();
                }else {
                    Snackbar.make(view,"注册失败："+e.getMessage(),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void menuHandle() {//菜单实例化

    }
}
