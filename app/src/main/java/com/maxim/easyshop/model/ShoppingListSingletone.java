package com.maxim.easyshop.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListSingletone {
    private List<Item> shoppingList;
    private static final ShoppingListSingletone ourInstance = new ShoppingListSingletone();
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public static ShoppingListSingletone getInstance() {
        return ourInstance;
    }

    public List<Item> getShoppingList() {
        return shoppingList;
    }

    private ShoppingListSingletone() {
        shoppingList = new ArrayList<>();
    }

    public void addItem(Item item) {
        shoppingList.add(0,item);
    }
    public void deleteItem(int position) {
        shoppingList.remove(position);
    }
}
