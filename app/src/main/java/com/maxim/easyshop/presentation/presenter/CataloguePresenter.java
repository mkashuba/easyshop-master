package com.maxim.easyshop.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.maxim.easyshop.model.DbProvider;
import com.maxim.easyshop.model.Item;
import com.maxim.easyshop.model.LoadAllItemsFromDbCallback;
import com.maxim.easyshop.model.ShoppingListSingletone;
import com.maxim.easyshop.presentation.view.CatalogueView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@InjectViewState
public class CataloguePresenter extends MvpPresenter<CatalogueView> {

    private List<Item> listItemsAC = new ArrayList<>();
    private Item item;


    public void addItemInList(Item itemToAdd) {
        item = itemToAdd;
        ShoppingListSingletone.getInstance().addItem(item);
        getViewState().addItemInList(item);
    }


    public void initViewPager() {
        getViewState().initViewPagerView();
    }

    public void initRecyclerView() {
        getViewState().initRecyclerView(ShoppingListSingletone.getInstance().getShoppingList());
    }

    public void initAutoCompleteAdapter() {
        DbProvider.getInstance().loadAllItems(new LoadAllItemsFromDbCallback() {
            @Override
            public void setShopList(List<Item> list) {
                listItemsAC.addAll(list);
                getViewState().initAutoCompleteAdapter(listItemsAC);
            }

            @Override
            public void errorLoadAllItemsFromDb(Exception e) {

            }
        });
    }
}
