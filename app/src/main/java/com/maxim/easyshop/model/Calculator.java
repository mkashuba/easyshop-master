package com.maxim.easyshop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Calculator {

    public static double getLowestPriceInItem(Item item) {
        double price;
        price = Collections.min(item.getPrices().values());
        return price;
    }

    public static List<OptimalObj> calculateOptimalMode(List<Item> list) {
        //create List of result
        List<OptimalObj> resultList = new ArrayList<>();

        //create listItemInShop
        List<Item> listItemInShop = new ArrayList<>();

        //create list nameShop
        Set<String> shops = new HashSet<>();
        for (Item i : list) {
            shops.addAll(i.getPrices().keySet());
        }

        double sum = 0;
        double price = 0;
        for (String nameShop : shops) {
            for (Item i : list) {
                price = i.getPrices().get(nameShop);
                if (price > 0) {
                    sum += price;
                    listItemInShop.add(i);
                }
            }
            OptimalObj tmpOptimal = new OptimalObj(nameShop, sum, listItemInShop);
            resultList.add(tmpOptimal);
            Collections.sort(resultList);
            sum = 0;
            listItemInShop.clear();
        }
        return resultList;
    }

}
