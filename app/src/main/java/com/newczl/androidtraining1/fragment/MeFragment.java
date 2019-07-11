package com.newczl.androidtraining1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.TipDialog;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.CalendarActivity;
import com.newczl.androidtraining1.activity.LoginActivity;

import com.newczl.androidtraining1.activity.MainActivity;
import com.newczl.androidtraining1.activity.StarActivity;
import com.newczl.androidtraining1.activity.UserInfoActivity;
import com.newczl.androidtraining1.bean.User;
import com.newczl.androidtraining1.utils.ImgUtils;
import com.newczl.androidtraining1.utils.PrefUtil;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

/**
 * 我的页面的Fragment
 * author:czl
 */
public class MeFragment extends BaseFragment {

    private static final int REQUEST_CODE1=0x1001;//返回结果码
    private static final int REQUEST_CODE2 =0x5001;//修改设置返回结果码
    private boolean isLogin;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Button dlogin;//退出界面
    private CircleImageView  circleImageView;//头像文件
    private LinearLayout star;//收藏页面

    @Override
    protected int setLayoutID() {//返回布局
        return R.layout.fragment_me;
    }

    @Override
    protected void initData() {

        super.initData();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        dlogin=view.findViewById(R.id.bLogin);//找到退出按钮
        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbarLayout);
        circleImageView = view.findViewById(R.id.circleImageView);
        //主题设置
        LinearLayout themeSetting = view.findViewById(R.id.theme_setting);
        LinearLayout calendar=view.findViewById(R.id.calendar);//日历
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//日历跳转
                Intent intent=new Intent(activity, CalendarActivity.class);
                activity.startActivity(intent);
            }
        });
        star=view.findViewById(R.id.star);//收藏
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogin) {//登录后才能去收藏
                    Intent intent = new Intent(activity, StarActivity.class);
                    activity.startActivity(intent);
                }else{//
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(login , REQUEST_CODE1);
                    Toasty.error(activity,"请先登录，在来看收藏夹吧！",Toasty.LENGTH_SHORT).show();
                }
            }
        });

        //Toolbar toolbar=view.findViewById(R.id.toolbar);//查找工具栏
        //toolbar.inflateMenu(R.menu.webviewmenu);

        if(BmobUser.isLogin()){//如果已经登录
            User user = BmobUser.getCurrentUser(User.class);
            collapsingToolbarLayout.setTitle(user.getUsername());
            dlogin.setVisibility(View.VISIBLE);//显示退出
            isLogin=true;
            if(user.getHeadImage()!=null) {
                ImgUtils.setImage(activity, user.getHeadImage().getUrl(), circleImageView);
            }
        }
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin){
                        //跳转至设置页面

                    Intent userInfo=new Intent(getActivity(), UserInfoActivity.class);
                    startActivityForResult(userInfo,REQUEST_CODE2);//跳转

                }else {
                    //跳转到登录界面
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(login , REQUEST_CODE1);
                }
            }
        });
        themeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themeset();
            }
        });

        dlogin.setOnClickListener(new View.OnClickListener() {//退出的点击事件
            @Override
            public void onClick(View v) {
//                if (BmobUser.isLogin()) {
//                    User user = BmobUser.getCurrentUser(User.class);
//                    Snackbar.make(v, "当前用户：" + user.getUsername() + "-" + user.getEmail(), Snackbar.LENGTH_LONG).show();
//                } else {
//                    Snackbar.make(v, "尚未登录，请先登录", Snackbar.LENGTH_LONG).show();
//                }
                Animation animBottomOut = AnimationUtils.loadAnimation(activity,
                        R.anim.anim_fade_out);
                BmobUser.logOut();
                isLogin=false;//退出登录
                collapsingToolbarLayout.setTitle("点击登录");
                circleImageView.setImageResource(R.drawable.default_head);//设置默认头像
                dlogin.startAnimation(animBottomOut);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dlogin.setVisibility(View.GONE);
                    }
                }, 500);
                Toasty.success(activity,"退出成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void themeset() {
        BottomMenu.show((MainActivity) activity, new String[]{"蓝色", "橙色", "绿色"}, new OnMenuItemClickListener(){
            @Override
            public void onClick(String text, int index) {
                //返回参数 text 即菜单名称，index 即菜单索引
                switch (index){
                    case 0:
                        PrefUtil.setint(activity,"theme",R.style.AppTheme);
                        reCreateSelf((MainActivity) activity);//重新创建
                        break;
                    case 1:
                        PrefUtil.setint(activity,"theme",R.style.theme_org);
                        reCreateSelf((MainActivity) activity);//重新创建
                        break;
                    case 2:
                        PrefUtil.setint(activity,"theme",R.style.theme_grass);
                        reCreateSelf((MainActivity) activity);//重新创建
                        break;
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE1){
            if (data != null){
                Boolean isLogin = data.getBooleanExtra("isLogin",false);
                MeFragment.this.isLogin = isLogin;
                if (isLogin){
                    User user = BmobUser.getCurrentUser(User.class);
                    String userName = data.getStringExtra("userName");
                    collapsingToolbarLayout.setTitle(userName);
                    ImgUtils.setImage(activity,user.getHeadImage().getUrl(),circleImageView);//设置图片地址
                    dlogin.setVisibility(View.VISIBLE);
                }

            }
        }
        if(requestCode == REQUEST_CODE2){//同步设置中
            User user = BmobUser.getCurrentUser(User.class);
            collapsingToolbarLayout.setTitle(user.getUsername());
            ImgUtils.setImage(activity,user.getHeadImage().getUrl(),circleImageView);//设置图片地址

        }
    }


    public void reCreateSelf(AppCompatActivity activity){//不闪屏重建自身
        Intent intent=new Intent(activity,activity.getClass());
        intent.putExtra("toggle",true);
        activity.startActivity(intent);
        activity.finish();
    }


}
