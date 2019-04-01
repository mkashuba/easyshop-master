package com.maxim.easyshop.model;

public class MyLastLocation {
    private static final MyLastLocation ourInstance = new MyLastLocation();
    private double latitude;
    private double longitude;

    public static MyLastLocation getInstance() {
        return ourInstance;
    }

    private MyLastLocation() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
