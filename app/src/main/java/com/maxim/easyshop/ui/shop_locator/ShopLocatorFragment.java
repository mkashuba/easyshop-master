package com.maxim.easyshop.ui.shop_locator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.maxim.easyshop.R;

public class ShopLocatorFragment extends Fragment {

    private SeekBar seekBar;
    private TextView distanceTxt;
    private int progressValue = 0;


    public ShopLocatorFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_locator, container, false);
        distanceTxt = view.findViewById(R.id.distance_txt);
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
