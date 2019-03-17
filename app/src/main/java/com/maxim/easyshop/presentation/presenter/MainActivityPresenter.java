package com.maxim.easyshop.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.maxim.easyshop.App;
import com.maxim.easyshop.presentation.view.MainActivityView;

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> {

    public static final String CATALOGUE_FRAGMENT = "CatalogueFragment";
    public static final String SHOPPING_CARD_FRAGMENT = "ShoppingCardFragment";
    public static final String SHOP_LOCATOR_FRAGMENT = "ShopLocatorFragment";

    public static String currentView = CATALOGUE_FRAGMENT;

    public void showNecessaryView() {
        switch (currentView) {
            case CATALOGUE_FRAGMENT:
                showCatalogView();
                break;
            case SHOPPING_CARD_FRAGMENT:
                showShoppingCardView();
                break;
            case SHOP_LOCATOR_FRAGMENT:
                showShopLocatorView();
                break;
        }
    }

    public void showCatalogView() {
        currentView = CATALOGUE_FRAGMENT;
        App.INSTANCE.getMainRouter().newRootScreen(CATALOGUE_FRAGMENT);
        getViewState().showCatalogueView();
    }

    public void showShoppingCardView() {
        currentView = SHOPPING_CARD_FRAGMENT;
        App.INSTANCE.getMainRouter().navigateTo(SHOPPING_CARD_FRAGMENT);
        getViewState().showShoppingCardView();
    }

    public void showShopLocatorView() {
        currentView = SHOP_LOCATOR_FRAGMENT;
        App.INSTANCE.getMainRouter().navigateTo(SHOP_LOCATOR_FRAGMENT);
        getViewState().showCatalogueView();
    }

    public void logout() {
        getViewState().logout();
    }

}
