package com.maxim.easyshop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Calculator {

    public static double getLowestPriceInItem(Item item) {
        double minPrice = Double.MAX_VALUE;
        for(Map.Entry<String, Double> e : item.getPrices().entrySet()){
            if(e.getValue() < minPrice && e.getValue() != -1 && e.getValue() != 0){
                minPrice = e.getValue();
            }
        }
        return minPrice;
    }

    private static List<Map.Entry<String, Double>> getLowestPairFromPrices(Item item) {
        List<Map.Entry<String, Double>> result = new ArrayList<>();
        Set<Map.Entry<String, Double>> entry = item.getPrices().entrySet();
        double minPrice = Double.MAX_VALUE;
        for (Map.Entry<String, Double> e : entry) {
            if (e.getValue() < minPrice && e.getValue() != -1 && e.getValue() != 0) {
                result.clear();
                result.add(e);
                minPrice = e.getValue();
            }
        }
        return result;
    }

    public static List<EconomyObj> calculateEconomyMode(List<Item> list) {
        //create List of result
        List<EconomyObj> resultList = new ArrayList<>();
        //create listItemInShop
        List<Item> listItemInShop = new ArrayList<>();
        //create list nameShop
        Set<String> shops = new HashSet<>();
        for (Item i : list) {
            shops.addAll(i.getPrices().keySet());
        }

        for(String nameShop : shops){
            for(Item i : list){
                String nameShopInItem = getLowestPairFromPrices(i).get(0).getKey();
                if(nameShop.equals(nameShopInItem)){
                    listItemInShop.add(i);
                }
            }
            EconomyObj economyObj = new EconomyObj(nameShop, listItemInShop);
            resultList.add(economyObj);

            listItemInShop.clear();
        }
        Collections.sort(resultList);
        return resultList;
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
