package com.maxim.easyshop.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Calculator {
    public static double getLowestPriceInItem(Item item){
        double price;
        price = Collections.min(item.getPrices().values());
        return price;
    }

    public static Map<String, Double> calculateOptimalMode(List<Item> list) {
        Map<String, Double> result = new HashMap<>();
        Set<String> shops = new HashSet<>();
        for(Item i : list){
            shops.addAll(i.getPrices().keySet());
        }


        return result;
    }

}
