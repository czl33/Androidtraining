package com.newczl.androidtraining1.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.bean.User;
import com.newczl.androidtraining1.utils.ImgUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.bmob.v3.BmobUser;

public class QrActivity extends BaseWebViewActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        TextView title=findViewById(R.id.title);
        title.setText("我的二维码");
        ImageView qrImg = findViewById(R.id.qrImg);//二维码图片
        ImageView head = findViewById(R.id.head);//头像
        User currentUser = BmobUser.getCurrentUser(User.class);
        Bitmap mBitmap = CodeUtils.createImage(currentUser.getUsername(), 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_icon));
        qrImg.setImageBitmap(mBitmap);
        ImgUtils.setImage(this,currentUser.getHeadImage().getUrl(),head);
    }

    @Override
    protected void menuHandle() {

    }
}
