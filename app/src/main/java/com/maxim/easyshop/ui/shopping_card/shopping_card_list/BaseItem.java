package com.maxim.easyshop.ui.shopping_card.shopping_card_list;

public abstract class BaseItem {
    public final String text;
    public final long id;

    public BaseItem(String text, long id) {
        this.text = text;
        this.id = id;
    }
}
