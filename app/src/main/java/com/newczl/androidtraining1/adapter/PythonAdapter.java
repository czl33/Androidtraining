package com.newczl.androidtraining1.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.bean.PythonBean;

public class PythonAdapter extends BaseQuickAdapter<PythonBean,BaseViewHolder> {

    public PythonAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    protected void convert(BaseViewHolder helper, PythonBean item) {
        helper.setText(R.id.textView,item.getAddress());//存地址
        helper.setText(R.id.textView2,item.getContent());//存内容

    }

}
