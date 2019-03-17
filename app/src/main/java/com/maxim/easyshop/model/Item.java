package com.maxim.easyshop.model;

import java.util.Map;

public class Item {
    private String title;
    private String barcode;
    private String urlImg;
    private Map<String, Double> priceMap;

    public Item() {
    }

    public Item(String title, String barcode, String urlImg, Map<String, Double> priceMap) {
        this.title = title;
        this.barcode = barcode;
        this.urlImg = urlImg;
        this.priceMap = priceMap;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Map<String, Double> getPriceMap() {
        return priceMap;
    }

    public void setPriceMap(Map<String, Double> priceMap) {
        this.priceMap = priceMap;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", barcode='" + barcode + '\'' +
                ", urlImg='" + urlImg + '\'' +
                ", priceMap=" + priceMap +
                '}';
    }
}
