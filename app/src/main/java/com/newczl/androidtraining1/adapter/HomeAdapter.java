package com.newczl.androidtraining1.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.bean.NewsBean;
import com.newczl.androidtraining1.utils.ConstantUtils;
import com.newczl.androidtraining1.utils.ImgUtils;

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
                ImgUtils.setImage(helper.itemView.getContext(),//得到父容器的上下文
                        ConstantUtils.WEB_SITE+item.getImg1()//将图片绑定到容器上
                         ,imageView);
                break;
            case 2:
                helper.setText(R.id.textView,item.getNewsName());//设置标题名字
                helper.setText(R.id.textView2,item.getNewsTypeName());//设置新闻类型
                ImageView imageView1=helper.getView(R.id.imageView);//得到图片1视图
                ImageView imageView2=helper.getView(R.id.imageView2);//得到图片2视图
                ImageView imageView3=helper.getView(R.id.imageView3);//得到图片3视图
                ImgUtils.setImage(helper.itemView.getContext(),//得到父容器的上下文
                        ConstantUtils.WEB_SITE+item.getImg1()//将图片绑定到容器上
                        ,imageView1);
                ImgUtils.setImage(helper.itemView.getContext(),//得到父容器的上下文
                        ConstantUtils.WEB_SITE+item.getImg2()//将图片绑定到容器上
                        ,imageView2);
                ImgUtils.setImage(helper.itemView.getContext(),//得到父容器的上下文
                        ConstantUtils.WEB_SITE+item.getImg3()//将图片绑定到容器上
                        ,imageView3);


            break;
        }



    }
}
