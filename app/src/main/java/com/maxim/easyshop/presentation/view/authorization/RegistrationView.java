package com.maxim.easyshop.presentation.view.authorization;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.google.firebase.auth.FirebaseUser;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface RegistrationView extends MvpView {
    void showProgressbar();
    void hideProgressbar();
    void showError(String error);
    void startMainActivity(FirebaseUser firebaseUser);
}
