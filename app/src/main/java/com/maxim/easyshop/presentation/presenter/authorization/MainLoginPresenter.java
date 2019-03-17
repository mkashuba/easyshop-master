package com.maxim.easyshop.presentation.presenter.authorization;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.maxim.easyshop.App;
import com.maxim.easyshop.presentation.view.authorization.MainLoginView;

@InjectViewState
public class MainLoginPresenter extends MvpPresenter<MainLoginView> {
    public static final String AUTHORIZATION_FRAGMENT = "AuthorizationFragment";
    public static final String LOGIN_FRAGMENT = "LoginFragment";
    public static final String REGISTRATION_FRAGMENT = "RegistrationFragment";

    public static String currentView = AUTHORIZATION_FRAGMENT;

    public void showNecessaryView() {
        switch (currentView) {
            case AUTHORIZATION_FRAGMENT:
                showAuthorizationFragment();
                break;
            case LOGIN_FRAGMENT:
                showLoginFragment();
                break;
            case REGISTRATION_FRAGMENT:
                showRegistrationFragment();
                break;
        }
    }

    public void showAuthorizationFragment() {
        currentView = AUTHORIZATION_FRAGMENT;
        App.INSTANCE.getLoginRouter().newRootScreen(AUTHORIZATION_FRAGMENT);
    }

    public void showLoginFragment() {
        currentView = LOGIN_FRAGMENT;
        App.INSTANCE.getLoginRouter().navigateTo(LOGIN_FRAGMENT);
    }

    public void showRegistrationFragment() {
        currentView = REGISTRATION_FRAGMENT;
        App.INSTANCE.getLoginRouter().navigateTo(REGISTRATION_FRAGMENT);
    }

}
