package com.maxim.easyshop.ui.authorization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.maxim.easyshop.App;
import com.maxim.easyshop.R;
import com.maxim.easyshop.presentation.presenter.authorization.MainLoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AuthorizationFragment extends Fragment {

    @BindView(R.id.sign_in_btn)
    Button signInBtn;
    @BindView(R.id.create_acc_btn)
    Button createAccBtn;
    @BindView(R.id.forgot_btn)
    TextView forgotBtn;

    private Unbinder unbinder;

    public AuthorizationFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization, container, false);
        MainLoginPresenter.currentView = MainLoginPresenter.AUTHORIZATION_FRAGMENT;
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick(R.id.sign_in_btn)
    public void login() {
        MainLoginPresenter.currentView = MainLoginPresenter.LOGIN_FRAGMENT;
        App.INSTANCE.getLoginRouter().navigateTo(MainLoginPresenter.LOGIN_FRAGMENT);
    }

    @OnClick(R.id.create_acc_btn)
    public void registration() {
        MainLoginPresenter.currentView = MainLoginPresenter.REGISTRATION_FRAGMENT;
        App.INSTANCE.getLoginRouter().navigateTo(MainLoginPresenter.REGISTRATION_FRAGMENT);
    }


}
