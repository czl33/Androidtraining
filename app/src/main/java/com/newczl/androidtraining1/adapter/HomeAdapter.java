package com.newczl.androidtraining1.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.bean.NewsBean;
import com.newczl.androidtraining1.utils.ConstantUtils;

import java.util.List;

public class HomeAdapter extends BaseMultiItemQuickAdapter<NewsBean,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HomeAdapter(List<NewsBean> data) {
        super(data);
        addItemType(1,R.layout.item_news1);//第一种类型
        addItemType(2,R.layout.item_news2);//第二种类型

    }

    @Override
    protected void convert(BaseViewHolder helper, NewsBean item) {
        switch (helper.getItemViewType()){
            case 1:
                helper.setText(R.id.textView,item.getNewsName());//设置标题名字
                helper.setText(R.id.textView2,item.getNewsTypeName());//设置标题文本
                ImageView imageView=helper.getView(R.id.imageView);//得到图片视图
                Glide.with(helper.itemView.getContext())//得到父容器的上下文
                        .load(ConstantUtils.WEB_SITE+item.getImg1()).into(imageView);//将图片绑定到容器上

                break;
            case 2:
                break;
        }



    }
}
