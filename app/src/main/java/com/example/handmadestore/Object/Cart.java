package com.example.handmadestore.Object;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Cart implements Serializable {
    private Item item;
    private int number;

    public Cart() {
    }

    public Cart(Item item) {
        this.item = item;
        this.number = 1;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void increase() {
        this.number++;
    }

    public void decrease(){
        this.number--;
    }

    public long calculatePrice(){
        return this.item.getPrice() * this.number;
    }

    @NonNull
    @Override
    public String toString() {
        return this.item.getTitle();
    }
}
