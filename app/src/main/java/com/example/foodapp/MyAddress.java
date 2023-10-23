package com.example.foodapp;

public class MyAddress {
    private String Building;
    private String Location;
    private String Landmark;
    private String Receiver_mobile;

    public MyAddress(String building, String location, String landmark, String receiver_mobile) {
        Building = building;
        Location = location;
        Landmark = landmark;
        Receiver_mobile = receiver_mobile;
    }

    public String getBuilding(){
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




}
