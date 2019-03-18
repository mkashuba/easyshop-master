package com.maxim.easyshop.model;

import java.util.Map;
import java.util.Objects;

public class Item {
    private String title;
    private String barcode;
    private String img_url;
    private Map<String, Double> prices;

    public Item() {
    }

    public Item(String title, String barcode, String img_url, Map<String, Double> prices) {
        this.title = title;
        this.barcode = barcode;
        this.img_url = img_url;
        this.prices = prices;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
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

    public Map<String, Double> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Double> prices) {
        this.prices = prices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(title, item.title) &&
                Objects.equals(barcode, item.barcode) &&
                Objects.equals(img_url, item.img_url) &&
                Objects.equals(prices, item.prices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, barcode, img_url, prices);
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", barcode='" + barcode + '\'' +
                ", img_url='" + img_url + '\'' +
                ", prices=" + prices +
                '}';
    }
}
