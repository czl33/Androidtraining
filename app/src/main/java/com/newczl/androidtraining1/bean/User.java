package com.newczl.androidtraining1.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
    private BmobFile headImage;//头像图片
    private boolean sex;
    private String nickName;
    private String info;//

    public BmobFile getHeadImage() {
        return headImage;
    }

    public User setHeadImage(BmobFile headImage) {
        this.headImage = headImage;
        return this;
    }

    public boolean getSex() {
        return sex;
    }

    public User setSex(boolean sex) {
        this.sex = sex;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public User setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public User setInfo(String info) {
        this.info = info;
        return this;
    }
}
