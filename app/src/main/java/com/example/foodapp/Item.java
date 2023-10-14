package com.example.foodapp;

public class Item {
    private String itemId;
    private String itemName;
    private String category;
    private String itemPrice;
    private String itemQuantity;
    private String itemDescription;
    private String imageUrl; // Add imageUrl field

    public Item() {
    }

    public Item(String itemId, String itemName, String category, String itemPrice, String itemQuantity, String itemDescription, String imageUrl) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemDescription = itemDescription;
        this.imageUrl = imageUrl; // Set imageUrl
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategory() {
        return category;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
