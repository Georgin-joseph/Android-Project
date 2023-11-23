package com.example.foodapp;

public class OrderItem {
    private String itemName;
    private String itemPrice;

    private int newPrice;
    private int count;
    private boolean isChecked;

    public OrderItem(String itemName, String itemPrice, int newPrice, int count, boolean isChecked) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.newPrice = newPrice;
        this.count = count;
        this.isChecked = isChecked;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public int getNewPrice() {
        return newPrice;
    }

    public int getCount() {
        return count;
    }

    public boolean isChecked() {
        return isChecked;
    }

}

