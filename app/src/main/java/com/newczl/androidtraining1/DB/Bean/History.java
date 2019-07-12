package com.newczl.androidtraining1.DB.Bean;

public class History {
    private int id;
    private String name;
    private String url;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public History(String name, String url,String time) {
        this.name = name;
        this.url = url;
        this.time=time;
    }

    public History(int id, String name, String url,String time) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.time=time;
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
}
