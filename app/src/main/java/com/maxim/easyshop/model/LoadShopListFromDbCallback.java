package com.maxim.easyshop.model;

import java.util.List;

public interface LoadShopListFromDbCallback {
    void setShopList(List<Shop> shopList);
    void errorLoadShopsFromDb(Exception e);
}
