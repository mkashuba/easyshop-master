package com.maxim.easyshop.model;

import java.util.List;

public interface IDbProvider {
    //Add new user in db
    void addUserInDB(User user);

    //Load all items from DB
    void loadAllItems(LoadAllItemsFromDbCallback callback);

    //Save listItems in Db
    void saveListItemInDB(List<Item> list);

    //Load listItems from Db
    void loadListItemFromDB();
}
