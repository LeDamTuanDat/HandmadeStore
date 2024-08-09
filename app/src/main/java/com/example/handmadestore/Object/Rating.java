package com.example.handmadestore.Object;

import java.io.Serializable;

public class Rating implements Serializable {
    private String id,itemId,userId,review;
    private float rating;

    public Rating(String itemId, String userId, String review, float rating) {
        this.itemId = itemId;
        this.userId = userId;
        this.review = review;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
