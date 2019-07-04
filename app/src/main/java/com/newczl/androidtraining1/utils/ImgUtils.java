package com.newczl.androidtraining1.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 图片工具封装
 */
public class ImgUtils {
    public static void setImage(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }
}
