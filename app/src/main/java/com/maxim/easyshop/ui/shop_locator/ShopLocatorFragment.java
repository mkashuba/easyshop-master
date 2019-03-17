package com.maxim.easyshop.ui.shop_locator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxim.easyshop.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopLocatorFragment extends Fragment {


    public ShopLocatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_locator, container, false);
    }

}
