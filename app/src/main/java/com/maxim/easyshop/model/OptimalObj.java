package com.maxim.easyshop.model;

import java.util.ArrayList;
import java.util.List;

public class OptimalObj implements Comparable<OptimalObj> {
    private String nameShop;
    private double totalCoast;
    private List<Item> itemListInThisShop;

    public OptimalObj(String nameShop, double totalCoast, List<Item> itemListInThisShop) {
        this.nameShop = nameShop;
        this.totalCoast = totalCoast;
        this.itemListInThisShop = new ArrayList<>();
        this.itemListInThisShop.addAll(itemListInThisShop);
    }

    public String getNameShop() {
        return nameShop;
    }

    public void setNameShop(String nameShop) {
        this.nameShop = nameShop;
    }

    public double getTotalCoast() {
        return totalCoast;
    }

    public void setTotalCoast(double totalCoast) {
        this.totalCoast = totalCoast;
    }

    public List<Item> getItemListInThisShop() {
        return itemListInThisShop;
    }

    public void setItemListInThisShop(List<Item> itemListInThisShop) {
        this.itemListInThisShop = itemListInThisShop;
    }

    @Override
    public int compareTo(OptimalObj o) {
        return Double.compare(totalCoast, o.getTotalCoast());
    }
}
