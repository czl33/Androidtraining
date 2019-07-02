package com.newczl.androidtraining1.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.adapter.HomeAdapter;
import com.newczl.androidtraining1.bean.NewsBean;
import com.newczl.androidtraining1.utils.ConstantUtils;
import com.newczl.androidtraining1.utils.JsonParseUtils;
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


    class MyHandle extends Handler {//消息处理
        public static final int NEWS_OK=1;//新闻列表处理的类型

        @Override
        public void handleMessage(Message msg) {//接收到消息怎么处理
            super.handleMessage(msg);
            switch (msg.what){
                case NEWS_OK:{
                    List<NewsBean> newslist= JsonParseUtils.getNewsList((String) msg.obj);//将消息中的字符串转为列表数组
                    homeAdapter.setNewData(newslist);//将得到的数组列表存入适配器中
                    avi.hide();//隐藏
                }
            }
        }
    }




    @Override
    protected void initData() {//初始化数据
        super.initData();
        getADData();//得到广告数据
        getNewsData();//得到新闻列表数据



    }

    private void getADData() {//得到广告数据
    }

    private void getNewsData() {//得到新闻列表数据

        OkHttpClient client=new OkHttpClient();//新建类
        final Request request=new Request.Builder()
                .url(ConstantUtils.REQUEST_NEWS_URL).build();//建立请求
        Call call=client.newCall(request);//发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               // Log.i(TAG, response.body().string());
                ResponseBody body = response.body();//获取请求的身体
                if(body!=null){//如果不为空
                    String str=body.string();//将转换为字符串
//                    Log.i(TAG, str);
                    final Message message=Message.obtain();//从数据池借一个消息
                    message.what=MyHandle.NEWS_OK;//告诉消息自己是做什么
                    message.obj=str;//将数据挂到obj上
                    Timer timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            myHandle.sendMessage(message);//发送消息，给myhandle处理
                        }
                    },2000);

                }
            }
        });



    }

    @Override
    protected void initView(View view) {//初始化视图
        super.initView(view);//找到传过来的视图
        TextView textView=view.findViewById(R.id.title);//找到工具栏标题
        textView.setText("首页");//设置工具栏标题

        avi = view.findViewById(R.id.avi);//找到全局的点点
        avi.bringToFront();//设置最顶层

        RefreshLayout refreshLayout =view.findViewById(R.id.refreshLayout);//找到刷新布局
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {//刷新回调
            @Override
            public void onRefresh(@NotNull RefreshLayout refreshlayout) {

                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                Toasty.info(activity, "刷新成功!", Toast.LENGTH_SHORT, true).show();//吐司
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {//加载更多回调
            @Override
            public void onLoadMore(@NotNull RefreshLayout refreshlayout) {//加载更多回调
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
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

    @Override
    protected int setLayoutID() {//返回主页的fragment布局
        return R.layout.fragment_home;//重写方法，返回主页的fragment布局
    }
}
