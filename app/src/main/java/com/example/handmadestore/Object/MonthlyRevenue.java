package com.example.handmadestore.Object;

public class MonthlyRevenue {
    int month;
    long revenue;

    public MonthlyRevenue() {
    }

    public MonthlyRevenue(int month, long revenue) {
        this.month = month;
        this.revenue = revenue;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }
}
