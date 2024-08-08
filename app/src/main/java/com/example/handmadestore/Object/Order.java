package com.example.handmadestore.Object;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private String keyId,idUser,name,phone,address;
    private String status;
    private ArrayList<Cart> carts = new ArrayList<>();

    public Order(String keyId,String idUser, String name, String phone, String address, ArrayList<Cart> carts) {
        this.keyId = keyId;
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.carts = carts;
        this.status = "Chờ xác nhận";
    }

    public Order(String idUser, String name, String phone, String address, ArrayList<Cart> carts) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.carts = carts;
        this.status = "Chờ xác nhận";
    }

    public Order() {
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Cart> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Cart> carts) {
        this.carts = carts;
    }
}