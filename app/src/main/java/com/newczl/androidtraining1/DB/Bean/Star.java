package com.newczl.androidtraining1.DB.Bean;


public class Star {
    public static final int NEWS=0;
    public static final int VIDEO=1;


    public Star(String name, String url, String createP,int videoId,int itemType) {
        this.name = name;
        this.url = url;
        this.createP = createP;
        this.itemType=itemType;
        this.videoId=videoId;
    }

    public Star(int id, String name, String url, String createP,int videoId,int itemType) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.createP = createP;
        this.videoId=videoId;
        this.itemType=itemType;
    }

    private int id;//id
    private String name;
    private String url;
    private String createP;
    private int videoId;//视频id
    public int itemType;//类型

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateP() {
        return createP;
    }

    public void setCreateP(String createP) {
        this.createP = createP;
    }

}
