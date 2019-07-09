package com.newczl.androidtraining1.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.bean.VideoBean;
import com.newczl.androidtraining1.utils.ConstantUtils;
import com.newczl.androidtraining1.utils.ImgUtils;

public class VideoAdapter extends BaseQuickAdapter<VideoBean, BaseViewHolder> {

    public VideoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoBean item) {
        ImgUtils.setImage(helper.itemView.getContext(), ConstantUtils.WEB_SITE+item.getImg(),
                (ImageView) helper.getView(R.id.imageView));

    }
}
