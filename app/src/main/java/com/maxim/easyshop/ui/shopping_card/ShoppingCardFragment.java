package com.maxim.easyshop.ui.shopping_card;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Calculator;
import com.maxim.easyshop.model.ShoppingListSingletone;
import com.maxim.easyshop.ui.shopping_card.shopping_card_list.ExpandebleAdapter;

import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCardFragment extends Fragment {

    private TextView total;


    public ShoppingCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_card, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("SHOPPING CARD");

        total = view.findViewById(R.id.total_numb_txt);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expMgr.createWrappedAdapter(new ExpandebleAdapter()));
        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        expMgr.expandAll();
        expMgr.attachRecyclerView(recyclerView);

        double res = Calculator.calculateOptimalMode(ShoppingListSingletone.getInstance().getShoppingList()).get(0).getTotalCoast();
        String s = String.format("%.2f", res);
        total.setText(s);

        return view;
    }


}
