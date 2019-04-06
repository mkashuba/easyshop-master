package com.maxim.easyshop.ui.shop_locator.shop_locator_list;

import com.maxim.easyshop.model.Shop;

public interface AdapterShopsCheckboxChangeCallback {
    void checkboxIsChecked(Shop shop);
    void checkboxIsUnChecked(Shop shop);
}
