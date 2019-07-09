package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.adapter.MyFragmentStatePagerAdapter;
import com.newczl.androidtraining1.bean.mSurfaceView;
import com.newczl.androidtraining1.bean.mText;
import com.newczl.androidtraining1.fragment.BaseFragment;
import com.newczl.androidtraining1.fragment.VideoIntroFragment;
import com.newczl.androidtraining1.fragment.VideoListFragment;
import com.newczl.androidtraining1.utils.ConstantUtils;
import com.newczl.androidtraining1.utils.ImgUtils;
import com.newczl.androidtraining1.videoplayer.SampleCoverVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import java.util.ArrayList;

import java.util.Random;

import es.dmoral.toasty.Toasty;

public class VideoDetailActivity extends BaseWebViewActivity {
    SampleCoverVideo detailPlayer;//播放器

    private boolean isPlay;//是否播放
    private boolean isPause;//是否暂停

    private OrientationUtils orientationUtils;
    private ImageView loveimg;//爱心图片

    private boolean iscollect;//是否收藏
    private EditText edit_danmu;//弹幕输入框
    private ImageView send;//发送按钮
    private GSYVideoOptionBuilder gsyVideoOption;//配置选项
    private Random random=new Random();

    private mSurfaceView msurfaceView;//弹幕屏幕
    private String[] strings={"6666","77777"};
    private int[] colors={Color.WHITE,Color.MAGENTA,Color.CYAN,Color.RED,Color.BLUE,Color.GREEN};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_video_detail);
        isShow=true;//禁止使用toolbar


        edit_danmu=findViewById(R.id.edit_danmu);//弹幕输入框
        send=findViewById(R.id.send);//发送按钮
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDanmu();
            }
        });


        detailPlayer =findViewById(R.id.detail_player);
        loveimg = detailPlayer.imageView;//找到爱心图片
        msurfaceView=detailPlayer.mSurfaceView;//赋值传递一下
        msurfaceView.bringToFront();//置顶




        String url = "http://video.7k.cn/app_video/20171202/6c8cf3ea/v.m3u8.mp4";

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Intent intent=getIntent();//得到意图
        String videoImage=intent.getStringExtra("videoImage");
        ImgUtils.setImage(this, ConstantUtils.WEB_SITE+videoImage,imageView);

        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        String titleName=intent.getStringExtra("videoName");//找到名字

        detailPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        initDetail();//初始化子页面

        gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(url)
                .setCacheWithPlay(false)
                .setVideoTitle(titleName)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                        startBarrage();//开始

                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                }).setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        }).build(detailPlayer);
        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(VideoDetailActivity.this, true, true);
            }
        });


        detailPlayer.loveclcik=new SampleCoverVideo.loveclcik() {//爱心的事件
            @Override
            public void click() {
                if(!iscollect){
                    Toasty.info(detailPlayer.getContext(),"收藏成功", Toast.LENGTH_SHORT).show();
                    loveimg.setImageResource(R.drawable.ic_loveed);//替换图片
                    iscollect=true;
                }else{
                    Toasty.info(detailPlayer.getContext(),"取消收藏", Toast.LENGTH_SHORT).show();
                    loveimg.setImageResource(R.drawable.ic_love);//替换图片
                    iscollect=false;
                }

            }
        };

    }

    private void sendDanmu() {//发送弹幕
        edit_danmu.setText("");
       // mDanmakuView.addItem(new DanmakuItem(this, edit_danmu.getText().toString(), mDanmakuView.getWidth()));
//        mText t=new mText();
//        t.setText(edit_danmu.getText().toString());
//        msurfaceView.add(t);
        Toasty.info(detailPlayer.getContext(),"发送成功",Toast.LENGTH_SHORT).show();

        //msurfaceView.clear();
    }


    @Override
    protected void menuHandle() {//实例化菜单选项

    }
    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        detailPlayer.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        detailPlayer.getCurrentPlayer().onVideoResume(false);
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            detailPlayer.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }


    private void initDetail() {
        ViewPager viewPager=findViewById(R.id.viewPager);
        ArrayList<BaseFragment> data=new ArrayList<>();
        data.add(new VideoIntroFragment());
        data.add(new VideoListFragment());
        viewPager.setAdapter(new MyFragmentStatePagerAdapter(getSupportFragmentManager(),data){
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "视频简介";
                    case 1:
                        return "视频目录";
                }
                return super.getPageTitle(position);
            }
        });
        TabLayout tabLayout=findViewById(R.id.tabLayout);//找到
        tabLayout.setupWithViewPager(viewPager);//注册viewpager
    }
    public void  playNewUrl(String url){
        gsyVideoOption.setUrl(url).build(detailPlayer);
        startBarrage();
        detailPlayer.startPlayLogic();
    }

    //生成弹幕
    private void startBarrage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    mText mText=new mText();
                    mText.setText(strings[random.nextInt(strings.length)]);
                    mText.setSpeed(3);
                    mText.setColor(colors[random.nextInt(colors.length)]);
                    mText.setSize(40);
                    msurfaceView.add(mText);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

}
