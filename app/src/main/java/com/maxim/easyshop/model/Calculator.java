package com.maxim.easyshop.model;

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

}
