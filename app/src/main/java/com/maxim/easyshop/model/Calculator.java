package com.maxim.easyshop.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Calculator {

    public static List<Item> getGlobalListItem() {
        return ShoppingListSingletone.getInstance().getShoppingList();
    }

    public static double getLowestPriceInItem(Item item) {
        double minPrice = Double.MAX_VALUE;
        for (Map.Entry<String, Double> e : item.getPrices().entrySet()) {
            if (e.getValue() < minPrice && e.getValue() != -1 && e.getValue() != 0) {
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

        double totalCoast = 0;
        double price = 0;
        for (String nameShop : shops) {
            for (Item i : list) {
                String nameShopInItem = getLowestPairFromPrices(i).get(0).getKey();
                if (nameShop.equals(nameShopInItem)) {
                    listItemInShop.add(i);
                    price = i.getPrices().get(nameShop);
                    totalCoast += price;
                }
            }
            EconomyObj economyObj = new EconomyObj(nameShop, totalCoast, listItemInShop);
            resultList.add(economyObj);
            totalCoast = 0;
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

    public static List<String> getTotalAndEconomyAmountOptimalMode() {
        List<String> result = new ArrayList<>();
        int size = calculateOptimalMode(getGlobalListItem()).size();
        double total = 0;
        double minEconomy = 0;
        double maxEconomy = 0;

        if (size > 0) {
            total = calculateOptimalMode(getGlobalListItem()).get(0).getTotalCoast();
            minEconomy = calculateOptimalMode(getGlobalListItem()).get(1).getTotalCoast() - total;
            maxEconomy = calculateOptimalMode(getGlobalListItem()).get(size - 1).getTotalCoast() - total;
        }

        result.add(String.format("%.2f", total));
        result.add(String.format("%.2f", minEconomy));
        result.add(String.format("%.2f", maxEconomy));

        return result;
    }

    public static List<String> getTotalAndEconomyAmountEconomyMode() {
        List<String> result = new ArrayList<>();
        List<EconomyObj> economyObjs = calculateEconomyMode(getGlobalListItem());
        int size = calculateEconomyMode(getGlobalListItem()).size();
        double total = 0;
        double minEconomy = 0;
        double maxEconomy = 0;

        if (size > 0) {
            for (EconomyObj o : economyObjs) {
                total = total + o.getTotalCoast();
            }

            minEconomy = calculateOptimalMode(getGlobalListItem()).get(1).getTotalCoast() - total;
            maxEconomy = calculateOptimalMode(getGlobalListItem()).get(size - 1).getTotalCoast() - total;
        }

        result.add(String.format("%.2f", total));
        result.add(String.format("%.2f", minEconomy));
        result.add(String.format("%.2f", maxEconomy));

        return result;
    }

    public static List<Shop> getListShopInRadius(List<Shop> listShop, int radius){
        List<Shop> listShopInRadius = new ArrayList<>();

        for(Shop shop : listShop){
            double dist = distance(MyLastLocation.getInstance(), shop);
            if(dist <= radius){
                shop.setDistanceToYou(dist);
                listShopInRadius.add(shop);
            }
        }

        return listShopInRadius;
    }

    private static double distance(MyLastLocation myLastLocation, Shop shop){
        final int EARTH_RADIUS = 6371000;
        double myLat = myLastLocation.getLatitude();
        double myLong = myLastLocation.getLongitude();
        double shopLat = shop.getLatitude();
        double shopLong = shop.getLongitude();
//
//        double lat1 = myLat * Math.PI / 180;
//        double lat2 = shopLat * Math.PI / 180;
//        double long1 = myLong * Math.PI / 180;
//        double long2 = shopLong * Math.PI / 180;

//        double cl1 = Math.cos(lat1);
//        double cl2 = Math.cos(lat2);
//        double sl1 = Math.sin(lat1);
//        double sl2 = Math.sin(lat2);
//        double delta = long2 - long1;
//        double cDelta = Math.cos(delta);
//        double sDelta = Math.sin(delta);
//
//        double y = Math.sqrt(Math.pow(cl2 * sDelta, 2) + Math.pow(cl1 * sl2 - sl1 * cl2 * cDelta, 2));
//        double x = sl1 * sl2 + cl1 * cl2 * cDelta;
//
//        double ad = Math.atan2(y, x);
//        double dist = ad * EARTH_RADIUS;

        double latDistance = Math.toRadians(myLat - shopLat);
        double lngDistance = Math.toRadians(myLong - shopLong);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(myLat)) * Math.cos(Math.toRadians(shopLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = Math.round(EARTH_RADIUS * c);

        return dist;
    }

}
