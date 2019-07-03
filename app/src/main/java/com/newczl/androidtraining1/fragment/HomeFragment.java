package com.newczl.androidtraining1.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.v3.BottomMenu;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.MainActivity;
import com.newczl.androidtraining1.adapter.HomeAdapter;
import com.newczl.androidtraining1.bean.NewsBean;
import com.newczl.androidtraining1.utils.ConstantUtils;
import com.newczl.androidtraining1.utils.JsonParseUtils;
import com.newczl.androidtraining1.utils.PrefUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * 首页面的Fragment
 * author:czl
 */
public class HomeFragment extends BaseFragment {

    private MyHandle myHandle;//定义消息处理
    private HomeAdapter homeAdapter;//主页RecycleView的适配器
    private AVLoadingIndicatorView avi;//刷新动画
    public static RefreshLayout refreshLayout;//上拉刷新布局
    //private TextView tintNet;//网络错误提示


    class MyHandle extends Handler {//消息处理
        public static final int NEWS_OK=1;//新闻列表处理的类型
        public static final int NET_FAIL=2;//联网失败
        public static final int REF_FAIL=3;//刷新失败
        public static final int REF_OK=4;//刷新成功
        @Override
        public void handleMessage(Message msg) {//接收到消息怎么处理
            super.handleMessage(msg);
            switch (msg.what){
                case NEWS_OK:
                    List<NewsBean> newslist= JsonParseUtils.getNewsList((String) msg.obj);//将消息中的字符串转为列表数组
                    homeAdapter.setNewData(newslist);//将得到的数组列表存入适配器中
                    avi.hide();//隐藏
//                    tintNet.setVisibility(View.GONE);//隐藏文字
                    break;
                case NET_FAIL:
                    Toasty.error(activity, "请检查网络！", Toast.LENGTH_SHORT, true).show();//联网失败
                    avi.hide();//隐藏加载点
//                    tintNet.setVisibility(View.VISIBLE);//显示网络错误
                    View emptyView = LayoutInflater.from(activity).inflate(R.layout.emptyview, null, false);
                    //实例化一个空布局
                    homeAdapter.setEmptyView(emptyView);//设置空布局
                    break;
                case REF_FAIL:
                    Toasty.error(activity, "刷新失败，检查网络！", Toast.LENGTH_SHORT, true).show();//联网失败
                    refreshLayout.finishRefresh(false);//刷新失败
                    avi.hide();//隐藏点
                    break;
                case REF_OK:
                    List<NewsBean> newlist= JsonParseUtils.getNewsList((String) msg.obj);//将消息中的字符串转为列表数组
                    homeAdapter.setNewData(newlist);//将得到的数组列表存入适配器中
                    avi.hide();//隐藏
                    refreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                    Toasty.info(activity, "刷新成功!", Toast.LENGTH_SHORT, true).show();//吐司
//                    tintNet.setVisibility(View.GONE);//隐藏文字
                    break;
            }
        }
    }




    @Override
    protected void initData() {//初始化数据
        super.initData();
        getADData();//得到广告数据
        getNewsData(true);//得到新闻列表数据


    }

    private void getADData() {//得到广告数据
    }

    private void getNewsData(boolean first) {//得到新闻列表数据

        OkHttpClient client=new OkHttpClient();//新建类
        final Request request=new Request.Builder()
                .url(ConstantUtils.REQUEST_NEWS_URL).build();//建立请求
        Call call=client.newCall(request);//发起请求
        final Message message=Message.obtain();//借用消息
        if(!first){//不是第一次

            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Toasty.error(activity, "刷新成功!", Toast.LENGTH_SHORT, true).show();//吐司
                    //数据池借用消息
                    message.what=MyHandle.REF_FAIL;//发送失败消息
                    myHandle.sendMessage(message);//发送消息

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    // Log.i(TAG, response.body().string());
                    ResponseBody body = response.body();//获取请求的身体

                    if(body!=null){//如果不为空
                        String str=body.string();//将转换为字符串
//                    Log.i(TAG, str);
                        message.what=MyHandle.REF_OK;//告诉消息自己是做什么
                        message.obj=str;//将刷新视图挂到obj上
                        //message.arg1=refreshLayout;
                        Timer timer=new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                myHandle.sendMessage(message);//发送消息，给myhandle处理
                            }
                        },1000);

                    }
                }
            });


        }else{//是第一次


        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Toasty.error(activity, "刷新成功!", Toast.LENGTH_SHORT, true).show();//吐司
              //数据池借用消息
                message.what=MyHandle.NET_FAIL;//发送失败消息
                myHandle.sendMessage(message);//发送消息
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               // Log.i(TAG, response.body().string());
                ResponseBody body = response.body();//获取请求的身体

                if(body!=null){//如果不为空
                    String str=body.string();//将转换为字符串
//                    Log.i(TAG, str);
                    message.what=MyHandle.NEWS_OK;//告诉消息自己是做什么
                    message.obj=str;//将数据挂到obj上
                    Timer timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            myHandle.sendMessage(message);//发送消息，给myhandle处理
                        }
                    },1000);

                }
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

//        tintNet=view.findViewById(R.id.tintNet);//网络错误提示
//        tintNet.bringToFront();//设置顶层

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
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {//加载更多回调
            @Override
            public void onLoadMore(@NotNull RefreshLayout refreshlayout) {//加载更多回调
              refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                //loadMoad();//像服务器加载更多数据
            }
        });

        RecyclerView recyclerView=view.findViewById(R.id.recyclerView);//找到recycleView
        homeAdapter=new HomeAdapter(null);//传入数据，暂时先不传，等到初始化数据时写入
        homeAdapter.openLoadAnimation();//加载动画
        homeAdapter.isFirstOnly(false);//是否只执行一次

        recyclerView.setAdapter(homeAdapter);//给RecycleView设置适配器
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));//设置线性布局管理器
        myHandle=new MyHandle();//初始化消息处理类

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
