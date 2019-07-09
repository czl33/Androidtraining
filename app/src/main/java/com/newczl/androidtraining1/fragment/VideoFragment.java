package com.newczl.androidtraining1.fragment;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.VideoDetailActivity;
import com.newczl.androidtraining1.adapter.VideoAdapter;
import com.newczl.androidtraining1.bean.VideoBean;
import com.newczl.androidtraining1.utils.ConstantUtils;
import com.newczl.androidtraining1.utils.JsonParseUtils;
import com.newczl.androidtraining1.utils.NetUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频页面的Fragment
 * author:czl
 */
public class VideoFragment extends BaseFragment {
    private VideoAdapter videoAdapter;//视频适配器
    private List<VideoBean> list_video;//视频数据
    private AVLoadingIndicatorView avi;//找到加载样式
    private RefreshLayout refreshLayout;//刷新布局

    @Override
    protected int setLayoutID() {
        return R.layout.fragment_video;//视频的布局
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        TextView title=view.findViewById(R.id.title);//找到标题
        avi=view.findViewById(R.id.avi);//找到加载样式
        title.setText("视频");

        avi.bringToFront();//设置最顶层


        refreshLayout =view.findViewById(R.id.refreshLayout);//找到刷新布局
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {//刷新回调
            @Override
            public void onRefresh(@NotNull RefreshLayout refreshlayout) {

              refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
//              Toasty.info(activity, "刷新成功!", Toast.LENGTH_SHORT, true).show();//吐司
                avi.show();//显示小球
                getVideoData();
//                getNewsData(false);//重新请求一次服务器
////                getADData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {//加载更多回调
            @Override
            public void onLoadMore(@NotNull RefreshLayout refreshlayout) {//加载更多回调
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                //loadMoad();//像服务器加载更多数据
            }
        });

        RecyclerView recyclerView=view.findViewById(R.id.recyclerView);//找到列表
        videoAdapter=new VideoAdapter(R.layout.item_video);//添加布局
        recyclerView.setAdapter(videoAdapter);//设置适配器
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));//设置线性布局管理器
        videoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {//添加点击事件
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(activity, VideoDetailActivity.class);
                intent.putExtra("videoName",list_video.get(position).getName());//传送视频名字
                intent.putExtra("videoImage",list_video.get(position).getImg());//传送视频图片地址
                intent.putExtra("videoIntro",list_video.get(position).getIntro());//传送视频详情
               intent.putParcelableArrayListExtra("videoDetailList",
                       (ArrayList<VideoBean.VideoDetailListBean>) list_video.get(position).getVideoDetailList());
               startActivity(intent);

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        getVideoData();//得到视频数据
    }

    private void getVideoData() {//视频数据
        NetUtils.getDataAsyn(ConstantUtils.REQUEST_VIDEO_URL, new NetUtils.MyCallBack() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onResponse(final String json) {
                activity.runOnUiThread(new Runnable() {//运行在ui线程
                    @Override
                    public void run() {
                        list_video= JsonParseUtils.getList(VideoBean.class,json);
                        videoAdapter.setNewData(list_video);//设置数据
                        avi.hide();//关闭
                    }
                });
            }
        });

    }


}
