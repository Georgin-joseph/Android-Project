package com.example.foodapp;
import com.google.firebase.Timestamp;

import java.util.List;

public class OrderModel {
    private List<OrderItem> items;
    private String userId;
    private String status;
    private int totalPrice;
    private Timestamp orderTimestamp;

    public OrderModel(List<OrderItem> items, String userId,String status,int totalPrice) {
        this.items = items;
        this.userId = userId;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Timestamp getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(Timestamp orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }
}

