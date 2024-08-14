package com.example.handmadestore.Object;

public class RevenueOverTime {
    int time;
    long revenue;

    public RevenueOverTime() {
    }

    public RevenueOverTime(int time, long revenue) {
        this.time = time;
        this.revenue = revenue;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }
}
