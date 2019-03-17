package com.maxim.easyshop;

import android.app.Application;

import com.maxim.easyshop.model.DbProvider;
import com.maxim.easyshop.model.ShoppingListSingletone;

import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

public class App extends Application {
    public static App INSTANCE;
    private Cicerone<Router> ciceroneLogin;
    private Cicerone<Router> ciceroneMain;



    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        ciceroneLogin = Cicerone.create();
        ciceroneMain = Cicerone.create();
        DbProvider.getInstance().setContext(this);
        ShoppingListSingletone.getInstance().setContext(this);
    }

    public NavigatorHolder getLoginNavigatorHolder() {
        return ciceroneLogin.getNavigatorHolder();
    }

    public Router getLoginRouter() {
        return ciceroneLogin.getRouter();
    }

    public NavigatorHolder getMainNavigatorHolder() {
        return ciceroneMain.getNavigatorHolder();
    }

    public Router getMainRouter() {
        return ciceroneMain.getRouter();
    }
}
