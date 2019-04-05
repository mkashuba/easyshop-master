package com.maxim.easyshop.ui.shop_locator;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Calculator;
import com.maxim.easyshop.model.DbProvider;
import com.maxim.easyshop.model.LoadShopListFromDbCallback;
import com.maxim.easyshop.model.MyLastLocation;
import com.maxim.easyshop.model.Shop;
import com.maxim.easyshop.ui.catalogue.AdapterItems;
import com.maxim.easyshop.ui.shop_locator.shop_locator_list.AdapterListShopLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.maxim.easyshop.model.Constants.MAPVIEW_BUNDLE_KEY;

public class ShopLocatorFragment extends Fragment implements OnMapReadyCallback,
        View.OnTouchListener,
        SeekBar.OnSeekBarChangeListener {

    private NestedScrollView scrollView;
    private ImageView imageView;
    private SeekBar seekBar;
    private TextView distanceTxt;
    private int progressValue = 0;
    private RecyclerView recyclerView;
    private AdapterListShopLocator adapterShop;
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private int startRadius = 3000;

    public ShopLocatorFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_locator, container, false);
        scrollView = view.findViewById(R.id.scroll_container_shop_locator);
        imageView = view.findViewById(R.id.transparent_image);
        distanceTxt = view.findViewById(R.id.distance_txt);
        mapView = view.findViewById(R.id.map);
        recyclerView = view.findViewById(R.id.recycler_view_list_shop);
        seekBar = view.findViewById(R.id.seekBar);

        imageView.setOnTouchListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("SHOP LOCATOR");
        recyclerView.setNestedScrollingEnabled(false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        initGoogleMap(savedInstanceState);
        initListShop(startRadius);
        String distTxt = startRadius/1000 + " km";
        distanceTxt.setText(distTxt);

        return view;
    }

    private void initListShop(final int radius) {
        final List<Shop> tmpList = new ArrayList<>();
        double lat = MyLastLocation.getInstance().getLatitude();
        double lon = MyLastLocation.getInstance().getLongitude();

        DbProvider.getInstance().loadListShopFromDB(lat, lon, radius, new LoadShopListFromDbCallback() {
            @Override
            public void setShopList(List<Shop> shopList) {
                tmpList.addAll(Calculator.getListShopInRadius(shopList, radius));
                Collections.sort(tmpList);
                initMarkersOnMap(tmpList);
                initAdapter(tmpList);
            }

            @Override
            public void errorLoadShopsFromDb(Exception e) {

            }
        });

    }

    private void initAdapter(List<Shop> list) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapterShop = new AdapterListShopLocator(list);
        recyclerView.setAdapter(adapterShop);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void initGoogleMap(Bundle savedInstanceState) {
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

    public int getDistance(int progress) {
        int distance = 0;
        switch (progress) {
            case 0:
                distance = 1000;
                break;
            case 1:
                distance = 3000;
                break;
            case 2:
                distance = 5000;
                break;
            case 3:
                distance = 10000;
                break;
            case 4:
                distance = 100000;
                break;
            default:
                distance = -1;
        }
        return distance;
    }

    //    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        double lat = 0;
                        double lon = 0;

                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            MyLastLocation.getInstance().setLatitude(lat);
                            MyLastLocation.getInstance().setLongitude(lon);
                        }

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 12.5f));
                    }
                });

    }

    public void initMarkersOnMap(List<Shop> shopList){
        googleMap.clear();
        for(Shop shop : shopList){
            LatLng latLng = new LatLng(shop.getLatitude(), shop.getLongitude());
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(shop.getTitle())
                    .snippet("Distance: " + String.format("%.0f", shop.getDistanceToYou()) + " m"));

            String nameShop = shop.getTitle();
            if(nameShop.contains("Shufersal")){
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.shufersal_logo2));
            } else if(nameShop.contains("Tiv-Taam")){
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.tivtaam_logo2));
            } else if(nameShop.contains("Victory")){
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.victory_logo2));
            } else if(nameShop.contains("Rami-Levy")){
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ramilevy_logo2));
            }

        }





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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progressValue = progress;
        int radius = getDistance(progressValue);
        String str = String.valueOf(radius/1000) + " km";
        distanceTxt.setText(str);
        double lat = MyLastLocation.getInstance().getLatitude();
        double lng = MyLastLocation.getInstance().getLongitude();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), getZoomValue(radius)));
        initListShop(radius);
    }

    private float getZoomValue(int radius){
        float zoom = 1;
        switch(radius){
            case 1000 : zoom = 14;
            break;
            case 3000 : zoom = 12.5f;
            break;
            case 5000 : zoom = 12;
            break;
            case 10000 : zoom = 11;
            break;
            case 100000 : zoom = 7;
            break;
            default: zoom = 1;
        }
        return zoom;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
