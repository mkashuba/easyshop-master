package com.maxim.easyshop.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.maxim.easyshop.App;
import com.maxim.easyshop.R;
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

    private DrawerLayout drawer;

    private Unbinder unbinder;
    private FirebaseAuth auth;

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
        mainActivityPresenter.showShopLocatorView();
    }

    @Override
    public void showCatalogueView() {
//        Toast.makeText(MainActivity.this, "Catalogue", Toast.LENGTH_SHORT).show();
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showShoppingCardView() {
//        Toast.makeText(MainActivity.this, "Shopping Card", Toast.LENGTH_SHORT).show();
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showShopLocatorView() {
        //Toast.makeText(MainActivity.this, "Locator", Toast.LENGTH_SHORT).show();
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void logout() {
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
