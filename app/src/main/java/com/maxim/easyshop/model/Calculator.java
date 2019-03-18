package com.maxim.easyshop.model;

import java.util.Collections;

public final class Calculator {
    public static double getLowestPriceInItem(Item item){
        double price;
        price = Collections.min(item.getPrices().values());
        return price;
    }

}
