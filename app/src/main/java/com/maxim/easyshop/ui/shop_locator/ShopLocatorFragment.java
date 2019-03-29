package com.maxim.easyshop.ui.shop_locator;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Shop;
import com.maxim.easyshop.ui.catalogue.AdapterItems;
import com.maxim.easyshop.ui.shop_locator.shop_locator_list.AdapterListShopLocator;

import java.util.ArrayList;
import java.util.List;

public class ShopLocatorFragment extends Fragment {

    private SeekBar seekBar;
    private TextView distanceTxt;
    private int progressValue = 0;
    private RecyclerView recyclerView;
    private AdapterListShopLocator adapterShop;


    public ShopLocatorFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_locator, container, false);
        distanceTxt = view.findViewById(R.id.distance_txt);
        recyclerView = view.findViewById(R.id.recycler_view_list_shop);
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                distanceTxt.setText(getDistance(progressValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        List<Shop> tmpList = new ArrayList<>();
        tmpList.add(new Shop("Victory", "Ashqelon", "Bar Kochba 24", 452.24));
        tmpList.add(new Shop("Tiv-Taam", "Ashqelon", "Eli-Cohen 15", 500.00));
        tmpList.add(new Shop("Ramy Levy", "Ashqelon", "Bar Kochba 40", 600.00));
        tmpList.add(new Shop("Victory", "Ashqelon", "Street 24", 1025.22));
        tmpList.add(new Shop("Shufersal", "Ashqelon", "Bar Kochba 24", 2366.02));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapterShop = new AdapterListShopLocator(tmpList);
        recyclerView.setAdapter(adapterShop);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    public String getDistance(int progress) {
        String distance = "";
        switch (progress) {
            case 0:
                distance = "500 m";
                break;
            case 1:
                distance = "1 km";
                break;
            case 2:
                distance = "2 km";
                break;
            case 3:
                distance = "3.5 km";
                break;
            case 4:
                distance = "5 km";
                break;
            default:
                distance = "unknown distance";
        }
        return distance;
    }

}
