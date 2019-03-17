package com.maxim.easyshop.ui.shopping_card;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxim.easyshop.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCardFragment extends Fragment {


    public ShoppingCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_card, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("SHOPPING CARD");
        return view;
    }

}
