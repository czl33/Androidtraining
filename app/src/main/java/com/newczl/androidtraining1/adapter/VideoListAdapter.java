package com.newczl.androidtraining1.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.bean.VideoBean;


public class VideoListAdapter extends BaseQuickAdapter<VideoBean.VideoDetailListBean,
        BaseViewHolder> {

    public VideoListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoBean.VideoDetailListBean item) {
        helper.setText(R.id.textView,item.getVideo_name());
    }
}
