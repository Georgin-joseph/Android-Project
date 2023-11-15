package com.example.foodapp;

public class mycartdomain {


    public mycartdomain(String itemName, String itemPrice,String imageUrl,String itemId,String userId) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.imageUrl = imageUrl;
        this.itemId = itemId;
        this.userId=userId;

    }

    String itemName,itemPrice,imageUrl,itemId,userId;


    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getItemId() {
        return itemId;
    }

    public String getUserId() {
        return userId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public  void setUserId(String userId){
        this.userId=userId;
    }

}
