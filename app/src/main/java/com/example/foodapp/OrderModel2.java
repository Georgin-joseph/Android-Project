package com.example.foodapp;

public class OrderModel2 {
    private String itemName;
    private String status;
    private int count;
    private double totalPrice;

    public OrderModel2(String itemName, String status, int count, double totalPrice) {
        this.itemName = itemName;
        this.status = status;
        this.count = count;
        this.totalPrice = totalPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public String getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

