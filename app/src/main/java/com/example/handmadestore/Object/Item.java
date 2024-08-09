package com.example.handmadestore.Object;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private String id;
    private String title;
    private String categoryId;
    private String description;
    private  ArrayList<String> picUrl;
    private int sold;
    private long price;
    private long oldPrice;

    public Item() {
    }

    public Item(String title, String categoryId, long oldPrice, long price, String description) {
        this.title = title;
        this.categoryId = categoryId;
        this.oldPrice = oldPrice;
        this.price = price;
        this.description = description;
        this.sold = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = this.sold + sold;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(long oldPrice) {
        this.oldPrice = oldPrice;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getTitle();
    }
}
