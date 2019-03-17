package com.maxim.easyshop.presentation.presenter.authorization;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maxim.easyshop.model.DbProvider;
import com.maxim.easyshop.model.User;
import com.maxim.easyshop.presentation.view.authorization.RegistrationView;

@InjectViewState
public class RegistrationPresenter extends MvpPresenter<RegistrationView> {

    private FirebaseAuth auth;

    public FirebaseAuth initFirebase(){
        auth = FirebaseAuth.getInstance();
        return auth;
    }

    public void registered(final String email, final String username, final String password, Activity activity){
        getViewState().showProgressbar();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            getViewState().hideProgressbar();
                            getViewState().startMainActivity(user);
                            DbProvider.getInstance().addUserInDB(new User(email, username, password, user.getUid()));
                        } else {
                            getViewState().showError(task.getException().getMessage());
                            getViewState().hideProgressbar();
                        }
                    }
                });
    }
}
