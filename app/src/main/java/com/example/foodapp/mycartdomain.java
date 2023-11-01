package com.example.foodapp;

public class mycartdomain {


    public mycartdomain(String itemName, String itemPrice,String imageUrl) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.imageUrl = imageUrl;

    }

    String itemName,itemPrice,imageUrl;


    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }


}
