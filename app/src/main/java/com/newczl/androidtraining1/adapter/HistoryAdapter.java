package com.newczl.androidtraining1.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newczl.androidtraining1.DB.Bean.History;
import com.newczl.androidtraining1.R;

import java.util.List;

public class HistoryAdapter extends BaseItemDraggableAdapter<History, BaseViewHolder> {


    public HistoryAdapter(int layoutResId, List<History> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, History item) {
        helper.setText(R.id.textView,item.getTime()+" : "+item.getName());//获取名字并存入
    }
}
