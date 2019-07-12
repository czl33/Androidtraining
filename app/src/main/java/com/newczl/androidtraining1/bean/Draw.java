package com.newczl.androidtraining1.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Draw extends BmobObject {
    /**
     * 图片
     */
    private BmobFile img;
    /**
     * 创作的用户
     */
    private User author;

    public BmobFile getImg() {
        return img;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
