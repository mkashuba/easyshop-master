package com.maxim.easyshop.model;

import java.util.ArrayList;
import java.util.List;

public class EconomyObj implements Comparable<EconomyObj> {
    private String nameShop;
    private int count;
    private double totalCoast;


    private List<Item> itemListInThisShop;

    public EconomyObj() {
    }

    public EconomyObj(String nameShop, double totalCoast, List<Item> itemListInThisShop) {
        this.nameShop = nameShop;
        this.totalCoast = totalCoast;
        this.itemListInThisShop = new ArrayList<>();
        this.itemListInThisShop.addAll(itemListInThisShop);
        this.count = this.itemListInThisShop.size();
    }

    public String getNameShop() {
        return nameShop;
    }

    public void setNameShop(String nameShop) {
        this.nameShop = nameShop;
    }

    public List<Item> getItemListInThisShop() {
        return itemListInThisShop;
    }

    public void setItemListInThisShop(List<Item> itemListInThisShop) {
        this.itemListInThisShop = itemListInThisShop;
    }
    public double getTotalCoast() {
        return totalCoast;
    }

    public void setTotalCoast(double totalCoast) {
        this.totalCoast = totalCoast;
    }


    @Override
    public int compareTo(EconomyObj o) {
        return o.count - this.count;
    }
}
