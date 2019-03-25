package com.maxim.easyshop.ui.shopping_card.shopping_card_list;

public class ChildItem extends BaseItem {
    public final String priceInRow;

    public ChildItem(String text, long id, String priceInRow) {
        super(text, id);
        this.priceInRow = priceInRow;
    }
}
