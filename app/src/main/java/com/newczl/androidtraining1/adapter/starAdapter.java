package com.newczl.androidtraining1.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newczl.androidtraining1.DB.Bean.Star;
import com.newczl.androidtraining1.R;

import java.util.List;

public class starAdapter extends BaseItemDraggableAdapter<Star, BaseViewHolder> {


    public starAdapter(int layoutResId, List<Star> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Star item) {
        helper.setText(R.id.textView,item.getName());//获取名字并存入
    }
}
