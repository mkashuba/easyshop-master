package com.maxim.easyshop.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maxim.easyshop.R;
import com.maxim.easyshop.ui.authorization.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Launcher);
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            //TODO get listItem from DB current user where login
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            //TODO get listItem from DB current user where login
            startActivity(intent);
            finish();
        }
    }
}
