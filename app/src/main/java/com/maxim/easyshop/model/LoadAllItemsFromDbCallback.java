package com.maxim.easyshop.model;

import java.util.List;

public interface LoadAllItemsFromDbCallback {
    void setShopList(List<Item> itemList);
    void errorLoadAllItemsFromDb(Exception e);
}
