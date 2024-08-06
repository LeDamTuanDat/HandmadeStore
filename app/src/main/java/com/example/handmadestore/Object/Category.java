package com.example.handmadestore.Object;

import java.io.Serializable;

public class Category implements Serializable {
    private String title;
    private String id;
    private String picUrl;

    public Category(String title, String picUrl) {
        this.title = title;
        this.picUrl = picUrl;
    }

    public Category(String id, String title, String picUrl){
        this.id = id;
        this.title = title;
        this.picUrl = picUrl;
    }

    public Category() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return title;
    }
}

