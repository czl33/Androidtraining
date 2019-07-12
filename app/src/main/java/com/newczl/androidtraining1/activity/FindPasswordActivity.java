package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.BaseWebViewActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import es.dmoral.toasty.Toasty;

public class FindPasswordActivity extends BaseWebViewActivity {

    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        TextView titleView = findViewById(R.id.title);
        titleView.setText("找回密码");
        editText = findViewById(R.id.editText);
    }
    public void register(final View view){
        final String email = editText.getText().toString();
        if (TextUtils.isEmpty(email)){
            editText.setError("邮箱不能为空！");
            return;
        }
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
//                    Snackbar.make(view ,"重置密码请求成功，请到" + email
//                            + "邮箱进行密码重置操作",Snackbar.LENGTH_LONG).show();
                    Intent intent=new Intent(FindPasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                    Toasty.info(FindPasswordActivity.this,"发送成功，注意查收",Toasty.LENGTH_SHORT).show();

                }else {
                    Log.e("BMOB",e.toString());
                    Snackbar.make(view ,e.getMessage(),Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void menuHandle() {

    }
}
