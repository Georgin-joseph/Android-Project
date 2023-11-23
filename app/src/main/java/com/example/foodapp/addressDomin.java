package com.example.foodapp;
public class addressDomin {

    private boolean isChecked;
    public addressDomin(String building, String location, String landmark, String receiver_mobile, String receiver_name,boolean isChecked) {
        Building = building;
        Location = location;
        Landmark = landmark;
        Receiver_mobile = receiver_mobile;
        Receiver_name = receiver_name;
        this.isChecked = isChecked;
    }


    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    private String itemId;

    String Building;
    String Location;

    String Landmark;

    public String getBuilding() {
        return Building;
    }

    public String getLocation() {
        return Location;
    }

    public String getLandmark() {
        return Landmark;
    }

    public String getReceiver_mobile() {
        return Receiver_mobile;
    }

    public String getReceiver_name() {
        return Receiver_name;
    }
    public String getItemId() {
        return itemId;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    String Receiver_mobile;
    String Receiver_name;
}
