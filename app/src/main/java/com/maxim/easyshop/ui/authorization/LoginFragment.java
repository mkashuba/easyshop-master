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
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.firebase.auth.FirebaseUser;
import com.maxim.easyshop.ui.MainActivity;
import com.maxim.easyshop.R;
import com.maxim.easyshop.presentation.presenter.authorization.LoginPresenter;
import com.maxim.easyshop.presentation.presenter.authorization.MainLoginPresenter;
import com.maxim.easyshop.presentation.view.authorization.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginFragment extends MvpAppCompatFragment implements LoginView {

    @InjectPresenter
    LoginPresenter loginPresenter;

    @BindView(R.id.sign_in_btn)
    Button signInBtn;
    @BindView(R.id.forgot_btn)
    TextView forgotPassBtn;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressFrame)
    FrameLayout progressFrame;

    private Unbinder unbinder;

    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        loginPresenter.initFirebase();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.sign_in_btn)
    public void signIn(){
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if(email != null && password != null && !email.isEmpty() && !password.isEmpty()){
            loginPresenter.signIn(email, password, getActivity());
        }
    }

    @OnClick(R.id.forgot_btn)
    public void forgotPass(){
        //TODO dialog forgot password
    }

    @Override
    public void showProgressbar() {
        signInBtn.setEnabled(false);
        progressFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        signInBtn.setEnabled(true);
        progressFrame.setVisibility(View.GONE);
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

    @Override
    public void showForgotPassDialog() {
        //TODO show forgot pass dialog
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
}
