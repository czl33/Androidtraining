package com.newczl.androidtraining1.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.newczl.androidtraining1.R;

import com.newczl.androidtraining1.bean.mSurfaceView;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class SampleCoverVideo extends StandardGSYVideoPlayer {


    public SampleCoverVideo(Context context) {
        super(context);
    }

    public SampleCoverVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SampleCoverVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ImageView imageView;//爱心图片
    public loveclcik loveclcik;//点击事件

    public mSurfaceView mSurfaceView;


    public interface loveclcik{
        void click();
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        imageView=findViewById(R.id.love);//找到爱心图片
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loveclcik.click();
            }
        });
        mSurfaceView=findViewById(R.id.msv);//弹幕

    }

    @Override
    public int getLayoutId() {
        return R.layout.video_layout_defind;
    }
}
