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
import android.widget.Switch;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Calculator;
import com.maxim.easyshop.model.ShoppingListSingletone;
import com.maxim.easyshop.ui.shopping_card.shopping_card_list.ExpandebleAdapter;


public class ShoppingCardFragment extends Fragment {

    private TextView total, yourEconomy;
    private Switch switcher;
    private RecyclerView recyclerView;
    private RecyclerViewExpandableItemManager expMgr;

    public ShoppingCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_card, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("SHOPPING CARD");

        total = view.findViewById(R.id.total_numb_txt);
        yourEconomy = view.findViewById(R.id.your_economy_numb_txt);
        switcher = view.findViewById(R.id.switch1);

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcher.isChecked()){
                    expMgr = new RecyclerViewExpandableItemManager(null);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(expMgr.createWrappedAdapter(new ExpandebleAdapter(true)));
                    // NOTE: need to disable change animations to ripple effect work properly
                    ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                    expMgr.expandAll();
                    expMgr.attachRecyclerView(recyclerView);

                } else {
                    expMgr = new RecyclerViewExpandableItemManager(null);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(expMgr.createWrappedAdapter(new ExpandebleAdapter(false)));
                    // NOTE: need to disable change animations to ripple effect work properly
                    ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                    expMgr.expandAll();
                    expMgr.attachRecyclerView(recyclerView);

                }
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        expMgr = new RecyclerViewExpandableItemManager(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expMgr.createWrappedAdapter(new ExpandebleAdapter(false)));
        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        expMgr.expandAll();
        expMgr.attachRecyclerView(recyclerView);

        double res = Calculator.calculateOptimalMode(ShoppingListSingletone.getInstance().getShoppingList()).get(0).getTotalCoast();
        String s = String.format("%.2f", res);
        total.setText(s);

        double minEcomon = Calculator.calculateOptimalMode(ShoppingListSingletone.getInstance().getShoppingList()).get(1).getTotalCoast() - res;
        double maxEconom = Calculator.calculateOptimalMode(ShoppingListSingletone.getInstance().getShoppingList()).get(3).getTotalCoast() - res;
        String resEconomy = String.format("%.2f" + " - " + "%.2f", minEcomon, maxEconom);
        yourEconomy.setText(resEconomy);
        return view;
    }


}
