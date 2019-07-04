package com.newczl.androidtraining1.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.v3.BottomMenu;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.MainActivity;
import com.newczl.androidtraining1.activity.NewsDetailActivity;
import com.newczl.androidtraining1.adapter.HomeAdapter;
import com.newczl.androidtraining1.bean.NewsBean;
import com.newczl.androidtraining1.utils.ConstantUtils;
import com.newczl.androidtraining1.utils.ImgUtils;
import com.newczl.androidtraining1.utils.JsonParseUtils;
import com.newczl.androidtraining1.utils.NetUtils;
import com.newczl.androidtraining1.utils.PrefUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;



/**
 * 首页面的Fragment
 * author:czl
 */
public class HomeFragment extends BaseFragment {

    private MyHandle myHandle;//定义消息处理
    private HomeAdapter homeAdapter;//主页RecycleView的适配器
    private AVLoadingIndicatorView avi;//刷新动画
    public static RefreshLayout refreshLayout;//上拉刷新布局
    private List<NewsBean> newslist;//全局的新闻列表信息
    private Banner banner;//轮播图
    private List<NewsBean> list_AD;//全局的广告新闻列表信息;

    class  MyHandle extends Handler {//消息处理
        public static final int NEWS_OK=1;//新闻列表处理的类型
        public static final int NET_FAIL=2;//联网失败
        public static final int REF_FAIL=3;//刷新失败
        public static final int REF_OK=4;//刷新成功
        @Override
        public void handleMessage(Message msg) {//接收到消息怎么处理
            super.handleMessage(msg);
            switch (msg.what){
                case NEWS_OK:
                    newslist= JsonParseUtils.getList(NewsBean.class,(String) msg.obj);//将消息中的字符串转为列表数组
                    homeAdapter.setNewData(newslist);//将得到的数组列表存入适配器中
                    avi.hide();//隐藏
                    break;
                case NET_FAIL:
                    Toasty.error(activity, "请检查网络！", Toast.LENGTH_SHORT, true).show();//联网失败
                    avi.hide();//隐藏加载点
                    View emptyView = LayoutInflater.from(activity).inflate(R.layout.emptyview, null);
                    //实例化一个空布局
                    homeAdapter.setHeaderAndEmpty(true);//适配器中没数据时展示头部
                    homeAdapter.setEmptyView(emptyView);//设置空布局
                    break;
                case REF_FAIL:
                    Toasty.error(activity, "刷新失败，检查网络！", Toast.LENGTH_SHORT, true).show();//联网失败
                    refreshLayout.finishRefresh(false);//刷新失败
                    avi.hide();//隐藏点
                    break;
                case REF_OK:
                    newslist= JsonParseUtils.getList(NewsBean.class,(String) msg.obj);//将消息中的字符串转为列表数组
                    homeAdapter.setNewData(newslist);//将得到的数组列表存入适配器中
                    avi.hide();//隐藏
                    refreshLayout.finishRefresh(1500/*,false*/);//传入false表示刷新失败
                    Toasty.info(activity, "刷新成功!", Toast.LENGTH_SHORT, true).show();//吐司
//                    tintNet.setVisibility(View.GONE);//隐藏文字
                    break;

            }
        }
    }




    @Override
    protected void initData() {//初始化数据
        super.initData();

        getNewsData(true);//得到新闻列表数据
        getADData();//得到广告数据

    }

    private void getADData() {//得到广告数据
        NetUtils.getDataAsyn(ConstantUtils.REQUEST_AD_URL, new NetUtils.MyCallBack() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onResponse(final String json) {
                activity.runOnUiThread(new Runnable() {//运行在ui线程任务
                    @Override
                    public void run() {
                       list_AD = JsonParseUtils.getList(NewsBean.class, json);//解析数据
                        List<String> images=new ArrayList<>();//得到文字
                        List<String> titles=new ArrayList<>();//得到标题
                        for (NewsBean newsB: list_AD) {
                            images.add(newsB.getImg1());//得到图片地址
                            titles.add(newsB.getNewsName());//得到名字
                        }
                        banner.setImages(images);//存入图片地址数据
                        banner.setBannerTitles(titles);//存入标题
                        banner.setOnBannerListener(new OnBannerListener() {
                            @Override
                            public void OnBannerClick(int position) {
                                Intent intent=new Intent(activity, NewsDetailActivity.class);//跳转链接
                                intent.putExtra("url",list_AD.get(position).getNewsUrl());
                                activity.startActivity(intent);//跳转
                            }
                        });//点击事件
                        banner.start();


                    }
                });//
            }
        });


    }

    private void getNewsData(boolean first) {//得到新闻列表数据
        final Message message=Message.obtain();//借用消息
        if(!first){//不是第一次
            NetUtils.getDataAsyn(ConstantUtils.REQUEST_NEWS_URL, new NetUtils.MyCallBack() {
                @Override
                public void onFailure() {
                    message.what=MyHandle.REF_FAIL;//发送网络错误
                    myHandle.sendMessage(message);//发送
                }
                @Override
                public void onResponse(String json) {
                    message.what=MyHandle.REF_OK;//告诉消息自己是做什么
                    message.obj=json;//将刷新视图挂到obj上
                    //message.arg1=refreshLayout;
                    Timer timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            myHandle.sendMessage(message);//发送消息，给myhandle处理
                        }
                    },800);
                }
            });


        }else{//是第一次
            NetUtils.getDataAsyn(ConstantUtils.REQUEST_NEWS_URL, new NetUtils.MyCallBack() {
                @Override
                public void onFailure() {
                    message.what=MyHandle.NET_FAIL;//发送失败消息
                    myHandle.sendMessage(message);//发送消息
                }

                @Override
                public void onResponse(String json) {
                    message.what=MyHandle.NEWS_OK;//告诉消息自己是做什么
                    message.obj=json;//将数据挂到obj上
                    Timer timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            myHandle.sendMessage(message);//发送消息，给myhandle处理
                        }
                    },800);
                }
            });
        }



    }

    @Override
    protected void initView(View view) {//初始化视图
        super.initView(view);//找到传过来的视图


        Toolbar toolbar=view.findViewById(R.id.toolbars);//找到工具栏
        toolbar.setNavigationIcon(R.drawable.ic_theme);//设置图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTheme();
            }
        });

        TextView textView=view.findViewById(R.id.title);//找到工具栏标题
        textView.setText("首页");//设置工具栏标题


        avi = view.findViewById(R.id.avi);//找到全局的点点
        avi.bringToFront();//设置最顶层


        refreshLayout =view.findViewById(R.id.refreshLayout);//找到刷新布局
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {//刷新回调
            @Override
            public void onRefresh(@NotNull RefreshLayout refreshlayout) {

//              refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
//              Toasty.info(activity, "刷新成功!", Toast.LENGTH_SHORT, true).show();//吐司
                avi.show();//显示小球
                getNewsData(false);//重新请求一次服务器
                getADData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {//加载更多回调
            @Override
            public void onLoadMore(@NotNull RefreshLayout refreshlayout) {//加载更多回调
              refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                //loadMoad();//像服务器加载更多数据
            }
        });

        RecyclerView recyclerView=view.findViewById(R.id.recyclerView);//找到recycleView
        homeAdapter=new HomeAdapter(null);//传入数据，暂时先不传，等到初始化数据时写入
        homeAdapter.openLoadAnimation();//加载动画
        homeAdapter.isFirstOnly(false);//是否只执行一次

        View headview=LayoutInflater.from(activity).inflate(R.layout.head_home,null);//实例化头布局

        homeAdapter.setHeaderView(headview);//设置头部内容

        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {//设置每一项的点击监听事件
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(activity, NewsDetailActivity.class);//跳转链接
                intent.putExtra("url",newslist.get(position).getNewsUrl());
                activity.startActivity(intent);//跳转
            }
        });

        recyclerView.setAdapter(homeAdapter);//给RecycleView设置适配器
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));//设置线性布局管理器
        myHandle=new MyHandle();//初始化消息处理类

        banner=headview.findViewById(R.id.banner);//从头布局中找到轮播图
        banner.setImageLoader(new ImageLoader() {//图片的加载器
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                if(path instanceof String){//传进来的是地址
                    ImgUtils.setImage(context,ConstantUtils.WEB_SITE+(String) path,imageView);
                }
                if(path instanceof Integer){//传进来的是id
                    imageView.setImageResource((Integer) path);

                }
            }
        });

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//添加图片轮播的样式，是否显示文字
        ArrayList<Integer> images=new ArrayList<>();//默认图片地址
        ArrayList<String> titles=new ArrayList<>();//默认带文字

        for (int i = 0; i <3 ; i++) {
            images.add(R.drawable.pic_item_list_default);
            titles.add("第"+i+"个标题");
        }
        banner.setDelayTime(2000);//设置延迟时间
        banner.setImages(images);//设置图片
        banner.setBannerTitles(titles);//设置标题
        banner.setBannerAnimation(Transformer.CubeOut);//设置轮播图动画
        banner.start();//开始轮播




    }

    private void createTheme() {//切换主题
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

    private void loadMoad() {//加载更多数据

    }

    @Override
    protected int setLayoutID() {//返回主页的fragment布局
        return R.layout.fragment_home;//重写方法，返回主页的fragment布局
    }

    public void reCreateSelf(AppCompatActivity activity){//不闪屏重建自身
        Intent intent=new Intent(activity,activity.getClass());
        activity.startActivity(intent);
        activity.finish();
    }

}
