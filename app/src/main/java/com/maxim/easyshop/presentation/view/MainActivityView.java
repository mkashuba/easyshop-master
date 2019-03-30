package com.maxim.easyshop.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface MainActivityView extends MvpView {
    void showCatalogueView();
    void showShoppingCardView();
    void showShopLocatorView();
//    void showProgress();
//    void hideProgress();
    void logout();
}
