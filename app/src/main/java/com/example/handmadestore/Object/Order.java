package com.example.handmadestore.Object;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Order implements Serializable {
    private String keyId,idUser,name,phone,address;
    private String status;
    private boolean zaloPayment;
    private ArrayList<Cart> carts = new ArrayList<>();
    private Date orderTime;

    public Order(String idUser, String name, String phone, String address, ArrayList<Cart> carts) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.carts = carts;
        this.status = "Chờ xác nhận";
        this.zaloPayment = false;
        this.orderTime = new Date();
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

    public boolean getZaloPayment() {
        return zaloPayment;
    }

    public void setZaloPayment(boolean zaloPayment) {
        this.zaloPayment = zaloPayment;
    }

    @Exclude
    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public void setOrderTimeFormatted(String orderTimeFormatted) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("mm/HH dd/MM/yyyy");
            this.orderTime = sdf.parse(orderTimeFormatted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getOrderTimeFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm/HH dd/MM/yyyy");
        return sdf.format(orderTime);
    }

    public long calTotal(){
        long totalCart = 0;
        for (int i = 0 ; i < this.carts.size() ; i++){
            Cart cart = this.carts.get(i);
            totalCart += cart.calculatePrice();
        }
        return totalCart;
    }
}
