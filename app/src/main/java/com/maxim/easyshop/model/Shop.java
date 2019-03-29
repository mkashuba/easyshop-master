package com.maxim.easyshop.model;

public class Shop {
    private String title;
    private String city;
    private String address;
    private double latitude;
    private double longitude;
    private double distanceToYou;

    public Shop() {
    }

    // temp constructor
    public Shop(String title, String city, String address, double distanceToYou) {
        this.title = title;
        this.city = city;
        this.address = address;
        this.distanceToYou = distanceToYou;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public double getDistanceToYou() {
        return distanceToYou;
    }

    public void setDistanceToYou(double distanceToYou) {
        this.distanceToYou = distanceToYou;
    }


}
