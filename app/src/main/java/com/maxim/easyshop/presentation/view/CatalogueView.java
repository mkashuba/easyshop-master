package com.maxim.easyshop.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.maxim.easyshop.model.Item;

import java.util.List;
import java.util.Set;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface CatalogueView extends MvpView {

    void hideKeyboard();
    void addItemInList(Item item);
    void initViewPagerView();
    void initRecyclerView(List<Item> list);
    void initAutoCompleteAdapter(List<Item> list);
}
