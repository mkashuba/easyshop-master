package com.maxim.easyshop.ui.shopping_card.shopping_card_list;

import java.util.ArrayList;
import java.util.List;

public class GroupItem extends BaseItem {
    public final List<ChildItem> children;

    public GroupItem(String text, long id) {
        super(text, id);
        children = new ArrayList<>();
    }
}
