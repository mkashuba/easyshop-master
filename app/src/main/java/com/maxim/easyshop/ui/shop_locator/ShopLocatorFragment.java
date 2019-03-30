package com.maxim.easyshop.ui.shop_locator;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Shop;
import com.maxim.easyshop.ui.catalogue.AdapterItems;
import com.maxim.easyshop.ui.shop_locator.shop_locator_list.AdapterListShopLocator;

import java.util.ArrayList;
import java.util.List;

import static com.maxim.easyshop.model.Constants.MAPVIEW_BUNDLE_KEY;

public class ShopLocatorFragment extends Fragment implements OnMapReadyCallback {

    private ScrollView scrollView;
    private ImageView imageView;
    private SeekBar seekBar;
    private TextView distanceTxt;
    private int progressValue = 0;
    private RecyclerView recyclerView;
    private AdapterListShopLocator adapterShop;
    private MapView mapView;


    public ShopLocatorFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_locator, container, false);
        scrollView = view.findViewById(R.id.scroll_container_shop_locator);
        imageView = view.findViewById(R.id.transparent_image);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
        distanceTxt = view.findViewById(R.id.distance_txt);
        mapView = view.findViewById(R.id.map);
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

        initGoogleMap(savedInstanceState);
        return view;
    }

    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
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

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
