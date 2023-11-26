package com.example.foodapp;

public class manageitemsveg {

    public manageitemsveg(String itemName, String itemId, String itemQuantity) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.itemQuantity = itemQuantity;
    }

    private String itemName,itemId,itemQuantity;

    public String getItemName() {
        return itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }
}
