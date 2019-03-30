package com.maxim.easyshop.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.maxim.easyshop.App;
import com.maxim.easyshop.R;
import com.maxim.easyshop.model.ShoppingListSingletone;
import com.maxim.easyshop.presentation.presenter.MainActivityPresenter;
import com.maxim.easyshop.presentation.view.MainActivityView;
import com.maxim.easyshop.ui.catalogue.CatalogueFragment;
import com.maxim.easyshop.ui.authorization.LoginActivity;
import com.maxim.easyshop.ui.shop_locator.ShopLocatorFragment;
import com.maxim.easyshop.ui.shopping_card.ShoppingCardFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;

import static com.maxim.easyshop.model.Constants.ERROR_DIALOG_REQUEST;
import static com.maxim.easyshop.model.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.maxim.easyshop.model.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class MainActivity extends MvpAppCompatActivity implements MainActivityView {

    @InjectPresenter
    MainActivityPresenter mainActivityPresenter;

    @BindView(R.id.nav_logout_btn)
    Button logoutBtn;
    @BindView(R.id.catalogue_btn)
    TextView catalogueBtn;
    @BindView(R.id.shopping_card_btn)
    TextView shoppingCardBtn;
    @BindView(R.id.shop_locator_btn)
    TextView shopLocatorBtn;
//    @BindView(R.id.progressFrameInMain)
//    FrameLayout progressFrame;

    private DrawerLayout drawer;

    private Unbinder unbinder;
    private FirebaseAuth auth;

    private boolean mLocationPermissionGranted = false;


    private Navigator navigator = new SupportFragmentNavigator(getSupportFragmentManager(),
            R.id.fragment_container) {
        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            switch (screenKey) {
                case "CatalogueFragment":
                    return new CatalogueFragment();

                case "ShoppingCardFragment":
                    return new ShoppingCardFragment();

                case "ShopLocatorFragment":
                    return new ShopLocatorFragment();

                default:
                    throw new RuntimeException("Unknown key!");
            }
        }

        @Override
        protected void showSystemMessage(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void exit() {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mainActivityPresenter.showNecessaryView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        App.INSTANCE.getMainNavigatorHolder().setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.INSTANCE.getMainNavigatorHolder().removeNavigator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        MainActivityPresenter.currentView = MainActivityPresenter.CATALOGUE_FRAGMENT;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @OnClick(R.id.nav_logout_btn)
    public void logoutClicked() {
        mainActivityPresenter.logout();
    }

    @OnClick(R.id.catalogue_btn)
    public void catalogueClicked() {
        mainActivityPresenter.showCatalogView();
    }

    @OnClick(R.id.shopping_card_btn)
    public void shoppingCardClicked() {
        mainActivityPresenter.showShoppingCardView();
    }

    @OnClick(R.id.shop_locator_btn)
    public void shopLocatorClicked() {
//        mainActivityPresenter.showShopLocatorView();

        if (checkMapServices()) {
            if (mLocationPermissionGranted) {
                mainActivityPresenter.showShopLocatorView();
            } else {
                getLocationPermission();
            }
        }
    }

    @Override
    public void showCatalogueView() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showShoppingCardView() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showShopLocatorView() {
        drawer.closeDrawer(GravityCompat.START);
    }

//    @Override
//    public void showProgress(){
//        progressFrame.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void hideProgress(){
//        progressFrame.setVisibility(View.GONE);
//    }

    @Override
    public void logout() {
        auth.signOut();

        //TODO clear list in presenter
        ShoppingListSingletone.getInstance().getShoppingList().clear();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    //check permissions for google maps

    private boolean checkMapServices() {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This part of application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes, sure", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                })
        .setNegativeButton("No, I'm paranoid!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mainActivityPresenter.showShopLocatorView();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK() {
        Log.d("MY_TAG", "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d("MY_TAG", "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d("MY_TAG", "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    mainActivityPresenter.showShopLocatorView();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MY_TAG", "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    mainActivityPresenter.showShopLocatorView();
                } else {
                    getLocationPermission();
                }
            }
        }

    }
}
