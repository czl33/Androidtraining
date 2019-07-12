package com.newczl.androidtraining1.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.newczl.androidtraining1.DB.Bean.Star;
import com.newczl.androidtraining1.R;

import java.util.List;

public class starAdapter extends BaseItemDraggableAdapter<Star, BaseViewHolder> {


    public starAdapter(List<Star> data) {
        super(data);

        setMultiTypeDelegate(new MultiTypeDelegate<Star>() {
            @Override
            protected int getItemType(Star entity) {
                //根据你的实体类来判断布局类型
                return entity.itemType;
            }
        });
        //Step.2
        getMultiTypeDelegate()
                .registerItemType(Star.NEWS, R.layout.star_item)
                .registerItemType(Star.VIDEO, R.layout.star_video_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, Star item) {
        helper.setText(R.id.textView,item.getName());//获取名字并存入
    }
}
