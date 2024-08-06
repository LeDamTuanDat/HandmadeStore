package com.example.handmadestore.Object;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private String title;
    private String description;
    private  ArrayList<String> picUrl;
    private double price;
    private double oldPrice;
    private int review;
    private double rating;
    private int NumberInCart;

    public Item() {
    }

    public Item(String title, String description, ArrayList<String> picUrl, double rating, int review, double oldPrice, double price) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.rating = rating;
        this.review = review;
        this.oldPrice = oldPrice;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public float getRating() {
        return (float) rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberInCart() {
        return NumberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.NumberInCart = numberInCart;
    }
}
