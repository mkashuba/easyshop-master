package com.maxim.easyshop.ui.authorization;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maxim.easyshop.App;
import com.maxim.easyshop.ui.MainActivity;
import com.maxim.easyshop.R;
import com.maxim.easyshop.presentation.presenter.authorization.MainLoginPresenter;
import com.maxim.easyshop.presentation.view.authorization.MainLoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;

public class LoginActivity extends MvpAppCompatActivity implements MainLoginView {

    @InjectPresenter
    MainLoginPresenter mainLoginPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;

    private FirebaseAuth auth;

    private AuthorizationFragment authorizationFragment = new AuthorizationFragment();
    private LoginFragment loginFragment = new LoginFragment();
    private RegistrationFragment registrationFragment = new RegistrationFragment();

    private Navigator navigator = new SupportFragmentNavigator(getSupportFragmentManager(),
            R.id.activity_login_container) {
        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            switch (screenKey) {
                case "AuthorizationFragment":
                    return authorizationFragment;

                case "LoginFragment":
                    return loginFragment;

                case "RegistrationFragment":
                    return registrationFragment;

                default:
                    throw new RuntimeException("Unknown key!");
            }
        }

        @Override
        protected void showSystemMessage(String message) {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void exit() {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        auth = FirebaseAuth.getInstance();
        mainLoginPresenter.showNecessaryView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        showMainActivity(currentUser);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.INSTANCE.getLoginNavigatorHolder().setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.INSTANCE.getLoginNavigatorHolder().removeNavigator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void showAuthorizationFragment() {
        mainLoginPresenter.showAuthorizationFragment();
    }

    @Override
    public void showLoginFragment() {
        mainLoginPresenter.showLoginFragment();
    }

    @Override
    public void showRegistrationFragment() {
        mainLoginPresenter.showRegistrationFragment();
    }

    @Override
    public void showMainActivity(FirebaseUser fbUser) {
        if (fbUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //TODO get listItem from DB current user where login
            startActivity(intent);
            finish();
        }
    }

}
