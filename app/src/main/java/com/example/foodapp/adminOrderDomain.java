package com.example.foodapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class adminOrderDomain {

    public adminOrderDomain(String itemName, String status,int count, int totalPrice,Date date) {
        this.itemName = itemName;
        this.status = status;
        this.count = count;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    String itemName,status;
    int count,totalPrice;
    private Date date;
    private String orderId;

    public String getItemName() {
        return itemName;
    }

    public String getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getFormattedDate() {
        // Implement date formatting logic here
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(date);
        } else {
            return ""; // Or return some default value
        }
    }
}
