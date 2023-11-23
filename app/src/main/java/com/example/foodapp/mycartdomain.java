package com.example.foodapp;

public class mycartdomain {


    public mycartdomain(String itemName, String itemPrice,String imageUrl,String itemId,String userId,int newPrice,int count) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.imageUrl = imageUrl;
        this.itemId = itemId;
        this.userId=userId;
        this.count=count;
        this.newPrice=0;
        this.totalNewPrice=0;

    }

    String itemName,itemPrice,imageUrl,itemId,userId;
    int count,newPrice;
    private int totalNewPrice;



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
    public int getCount() {
        return count;
    }
    public int getNewPrice() {
        return newPrice;
    }

    public int getTotalNewPrice(){
        return totalNewPrice;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public  void setUserId(String userId){
        this.userId=userId;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void setNewPrice(int newPrice) {
        this.newPrice = newPrice;
    }
    public void setTotalNewPrice(int totalNewPrice) {
        this.totalNewPrice = totalNewPrice;
    }
}
