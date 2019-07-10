package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.bean.User;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends BaseWebViewActivity {

    private static final int REQUEST_CODE2 = 0x2001;
    private static final int REQUEST_CODE3 = 0x2003;//快速注册

    private EditText editText;
    private EditText editText2;
    private LinearLayout linearLayout;//qq登录
    private Tencent tencent;//回调

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tencent=Tencent.createInstance("101711405", this);

        TextView titleView = findViewById(R.id.title);

        titleView.setText("登录");
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        linearLayout=findViewById(R.id.qqLogin);//找到qq登录

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qqLogin();
            }
        });

    }

    private void qqLogin() {//qq登录
        tencent.login(LoginActivity.this, "get_simple_userinfo", new IUiListener() {
            @Override
            public void onComplete(Object o) {
                //在这里可以获取登录账户的个人信息
                Log.i("haha", (String)o);
            }

            @Override
            public void onError(UiError uiError) {
                Toasty.error(LoginActivity.this,"发生了错误。", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toasty.error(LoginActivity.this,"取消了。", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void login(final View view){
        String userName = editText.getText().toString();
        String password = editText2.getText().toString();
        if(TextUtils.isEmpty(userName)){
            editText.setError("用户名不能为空！");
            return;
        }
        if(TextUtils.isEmpty(password)){
            editText2.setError("密码不能为空！");
            return;
        }
        final User user = new User();
        //此处替换为你的用户名
        user.setUsername(userName);
        //此处替换为你的密码
        user.setPassword(password);
        user.login(new SaveListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    User user1 = BmobUser.getCurrentUser(User.class);
                    Intent data = new Intent();
                    data.putExtra("isLogin",true);
                    data.putExtra("userName",user.getUsername());
                    setResult(RESULT_OK,data);
                    LoginActivity.this.finish();
                    Toasty.success(LoginActivity.this,"登录成功",Toasty.LENGTH_SHORT).show();
                }else {
                    Toasty.error(LoginActivity.this,"登录失败",Toasty.LENGTH_SHORT).show();
//                    Snackbar.make(view,"登录失败："+e.getMessage(),
//                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
    public void register(final View view){
        Intent intent =new Intent(this,RegisterActivity.class);
        startActivityForResult(intent,REQUEST_CODE2);
    }
    public void forgetPassword(final View view){
        Intent intent =new Intent(this, FindPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE2){
            if (data !=null){
                String userName = data.getStringExtra("userName");
                if (!TextUtils.isEmpty(userName)){
                    editText.setText(userName);
                    editText2.setText("");
                    editText.setSelectAllOnFocus(true);
                }
            }
        }
        if(requestCode==REQUEST_CODE3){
            if (data !=null){
                //Intent stq=data;
                //并结束自身
                setResult(RESULT_OK,data);
                LoginActivity.this.finish();

            }
        }
    }




    @Override
    protected void menuHandle() {//菜单实例化

    }

    public void quickRe(View view) {//快速注册
        Intent intent =new Intent(this,QuiteReActivity.class);
        startActivityForResult(intent,REQUEST_CODE3);
    }
}
