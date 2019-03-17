package com.maxim.easyshop.ui.authorization;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.firebase.auth.FirebaseUser;
import com.maxim.easyshop.ui.MainActivity;
import com.maxim.easyshop.R;
import com.maxim.easyshop.presentation.presenter.authorization.MainLoginPresenter;
import com.maxim.easyshop.presentation.presenter.authorization.RegistrationPresenter;
import com.maxim.easyshop.presentation.view.authorization.RegistrationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegistrationFragment extends MvpAppCompatFragment implements RegistrationView {

    @InjectPresenter
    RegistrationPresenter registrationPresenter;

    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_username)
    EditText inputUsername;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressFrame)
    FrameLayout progressFrame;
    @BindView(R.id.registration_btn)
    Button createAccBtn;

    private Unbinder unbinder;


    public RegistrationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        unbinder = ButterKnife.bind(this, view);
        registrationPresenter.initFirebase();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.registration_btn)
    public void createNewAccount(){
        String email = inputEmail.getText().toString();
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();
        if(email != null && username != null && password != null &&
        !email.isEmpty() && !username.isEmpty() && !password.isEmpty()){
            registrationPresenter.registered(email, username, password, getActivity());
        }
    }

    @Override
    public void showProgressbar() {
        createAccBtn.setEnabled(false);
        progressFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        createAccBtn.setEnabled(true);
        progressFrame.setVisibility(View.GONE);
    }

    @Override
    public void showError(String error) {
        new AlertDialog.Builder(getContext())
                .setMessage(error)
                .setTitle("Ooops! Something went wrong...")
                .setPositiveButton("Ok", null)
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void startMainActivity(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            MainLoginPresenter.currentView = MainLoginPresenter.AUTHORIZATION_FRAGMENT;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
