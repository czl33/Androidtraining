package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.QQListener.BaseUiListener;
import com.newczl.androidtraining1.bean.QQBean;
import com.newczl.androidtraining1.bean.QQLoginBean;
import com.newczl.androidtraining1.bean.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends BaseWebViewActivity {

    private static final int REQUEST_CODE2 = 0x2001;
    private static final int REQUEST_CODE3 = 0x2003;//快速注册

    private EditText editText;
    private EditText editText2;
    private LinearLayout linearLayout;//qq登录
    private Tencent tencent;//回调
    private BaseUiListener qqLogin;//授权登录监听器
    private IUiListener userInfoListener; //获取用户信息监听器
    private UserInfo userInfo; //qq用户信息（json）
    private QQBean qqBean;//qq用户实体

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tencent = Tencent.createInstance("101711405", this);

        TextView titleView = findViewById(R.id.title);

        titleView.setText("登录");
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        linearLayout = findViewById(R.id.qqLogin);//找到qq登录
        qqLogin=new BaseUiListener();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qqLogin();
            }
        });
        userInfoListener=new IUiListener(){//实例化用户信息监听器

            @Override
            public void onComplete(Object o) {
                if(o == null){
                    return;
                }
                try {
                         JSONObject jo = (JSONObject) o;
////                    int ret = jo.getInt("ret");
////                    System.out.println("json=" + String.valueOf(jo));
////                    String nickName = jo.getString("nickname");
////                    String gender = jo.getString("gender");
////
////                    Toast.makeText(LoginActivity.this, "你好，" + nickName,
////                            Toast.LENGTH_LONG).show();
                    Gson gson=new Gson();
                    qqBean = gson.fromJson(jo.toString(), QQBean.class);
                    QQLoginBean qqLoginBean = qqLogin.getQqLoginBean();
                    BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth("qq",
                            qqLoginBean.getAccess_token(),
                            qqLoginBean.getExpires_in(),
                            qqLoginBean.getOpenid());
                    BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {
                        @Override
                        public void done(JSONObject user, BmobException e) {
                            if (e == null) {//成功了啊
                                User currentUser = BmobUser.getCurrentUser(User.class);
                                currentUser.setNickName(qqBean.getNickname());
                                BmobFile bf=new BmobFile("qq","",qqBean.getFigureurl_qq());//存入头像图片
                                currentUser.setInfo("这家伙很懒什么都没有留下。");
                                currentUser.setHeadImage(bf);
                                currentUser.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            //dlogin.setVisibility(View.GONE);
                                            User user1 = BmobUser.getCurrentUser(User.class);
                                            Intent data = new Intent();
                                            data.putExtra("isLogin", true);
                                            data.putExtra("userName", user1.getUsername());
                                            setResult(RESULT_OK, data);
                                            LoginActivity.this.finish();
                                            Toasty.success(LoginActivity.this, "登录成功", Toasty.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            } else {
                                Log.e("BMOB", e.toString());
                                TipDialog.show(LoginActivity.this, "回调信息错误，请联系作者！", TipDialog.TYPE.ERROR)
                                        .setTipTime(5000);
                            }
                        }
                    });

                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        };


    }

    private void qqLogin() {//qq登录
//        IUiListener loginListener = new BaseUiListener() {
//            @Override
//            protected void doComplete(JSONObject values) {
//                Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
//
//            }
        tencent.login(LoginActivity.this, "all", qqLogin);

    }

    public void login(final View view) {
        String userName = editText.getText().toString();
        String password = editText2.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            editText.setError("用户名不能为空！");
            return;
        }
        if (TextUtils.isEmpty(password)) {
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
                if (e == null) {
                    User user1 = BmobUser.getCurrentUser(User.class);
                    Intent data = new Intent();
                    data.putExtra("isLogin", true);
                    data.putExtra("userName", user.getUsername());
                    setResult(RESULT_OK, data);
                    LoginActivity.this.finish();
                    Toasty.success(LoginActivity.this, "登录成功", Toasty.LENGTH_SHORT).show();
                } else {
                    Toasty.error(LoginActivity.this, "失败,请检查用户名或密码。", Toasty.LENGTH_SHORT).show();
//                    Snackbar.make(view,"登录失败："+e.getMessage(),
//                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void register(final View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_CODE2);
    }

    public void forgetPassword(final View view) {
        Intent intent = new Intent(this, FindPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {  //得到qq的信息做的回调
            Tencent.onActivityResultData(requestCode, resultCode, data, qqLogin);
            QQLoginBean qqLoginBean = qqLogin.getQqLoginBean();
            //Log.i("得到的信息", "qqLogin: "+qqLoginBean.getOpenid());

            tencent.setOpenId(qqLoginBean.getOpenid());//存入全局
            tencent.setAccessToken(qqLoginBean.getAccess_token(),qqLoginBean.getExpires_in());//存入

            userInfo=new UserInfo(LoginActivity.this,tencent.getQQToken());
            userInfo.getUserInfo(userInfoListener);//传入回调函数（见73行）


        }

        if (requestCode == REQUEST_CODE2) {//这个注册回来的回调
            if (data != null) {
                String userName = data.getStringExtra("userName");
                if (!TextUtils.isEmpty(userName)) {
                    editText.setText(userName);
                    editText2.setText("");
                    editText.setSelectAllOnFocus(true);
                }
            }
        }
        if (requestCode == REQUEST_CODE3) {//这个是电话注册回来的回调
            if (data != null) {
                //Intent stq=data;
                //并结束自身
                setResult(RESULT_OK, data);
                LoginActivity.this.finish();

            }
        }
    }


    @Override
    protected void menuHandle() {//菜单实例化

    }

    public void quickRe(View view) {//快速注册
        Intent intent = new Intent(this, QuiteReActivity.class);
        startActivityForResult(intent, REQUEST_CODE3);
    }


}

