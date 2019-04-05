package com.maxim.easyshop.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.maxim.easyshop.App;
import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Shop;
import com.maxim.easyshop.model.ShoppingListSingletone;
import com.maxim.easyshop.presentation.presenter.MainActivityPresenter;
import com.maxim.easyshop.presentation.view.MainActivityView;
import com.maxim.easyshop.ui.catalogue.CatalogueFragment;
import com.maxim.easyshop.ui.authorization.LoginActivity;
import com.maxim.easyshop.ui.shop_locator.ShopLocatorFragment;
import com.maxim.easyshop.ui.shopping_card.ShoppingCardFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;

import static com.maxim.easyshop.model.Constants.ERROR_DIALOG_REQUEST;
import static com.maxim.easyshop.model.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.maxim.easyshop.model.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

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
//    @BindView(R.id.progressFrameInMain)
//    FrameLayout progressFrame;

    private DrawerLayout drawer;

    private Unbinder unbinder;
    private FirebaseAuth auth;

    private boolean mLocationPermissionGranted = false;


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


    //===========================================================================
//
//    private void initData(){
//        ArrayList<String> listJson = new ArrayList<>();
//        listJson.add("{\"latitude\": \"31.7551905\",\"longitude\": \"34.9897848\",\"city\": \"בית שמש\",\"address\": \"1 העליה\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.008056\",\"longitude\": \"34.791036\",\"city\": \"חולון\",\"address\": \"קרן היסוד 1 ק.שר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.753328\",\"longitude\": \"35.213609\",\"city\": \"ירושלים\",\"address\": \"35 פייר קניג\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.2726540\",\"longitude\": \"34.8105380\",\"city\": \"באר שבע\",\"address\": \"5  הר בוקר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.174437\",\"longitude\": \"34.89074\",\"city\": \"כפר סבא\",\"address\": \"3 נתיב האבות\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.2540557\",\"longitude\": \"34.8149347\",\"city\": \"באר שבע\",\"address\": \"1 רח' האורגים\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.322861\",\"longitude\": \"34.870534\",\"city\": \"נתניה\",\"address\": \"2 הקדר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.7876803\",\"longitude\": \"35.1869273\",\"city\": \"ירושלים\",\"address\": \"24 כנפי נשרים\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.257398\",\"longitude\": \"34.76719\",\"city\": \"באר שבע\",\"address\": \"קניון שאול המלך\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.193247\",\"longitude\": \"34.880094\",\"city\": \"רעננה\",\"address\": \"8 החרושת\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.925565\",\"longitude\": \"34.863521\",\"city\": \"רמלה\",\"address\": \"23 שמשון הגיבור\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.5014022\",\"longitude\": \"34.927109\",\"city\": \"אור עקיבא\",\"address\": \"קניון אורות\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.415318\",\"longitude\": \"34.9519259\",\"city\": \"חדרה\",\"address\": \"7 יקותיאל אדם\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"33.1925231\",\"longitude\": \"35.571111\",\"city\": \"קרית שמונה\",\"address\": \"מתחם ביג\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.710119\",\"longitude\": \"35.143138\",\"city\": \"טבעון\",\"address\": \"חוצות אלונים\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.080949\",\"longitude\": \"34.901436\",\"city\": \"פתח תקווה\",\"address\": \"9 אליעזר פרידמן\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.893301\",\"longitude\": \"34.806883\",\"city\": \"רחובות\",\"address\": \"2 רח' בילו\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.966817\",\"longitude\": \"34.785049\",\"city\": \"ראשון לציון\",\"address\": \"1 גולדה מאיר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.9059985\",\"longitude\": \"35.0192993\",\"city\": \"מודיעין\",\"address\": \"21   צאלון\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.259669\",\"longitude\": \"35.208341\",\"city\": \"ערד\",\"address\": \"29 הקנאים\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"30.6127950\",\"longitude\": \"34.8026040\",\"city\": \"מצפה רמון\",\"address\": \"2 שד' בן גוריון\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.840073\",\"longitude\": \"35.079994\",\"city\": \"קרית מוצקין\",\"address\": \"84 שד' בן גוריון\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.667683\",\"longitude\": \"34.581803\",\"city\": \"אשקלון\",\"address\": \"39 דוד רמז\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.074239\",\"longitude\": \"35.031423\",\"city\": \"דימונה\",\"address\": \"1 גולדה מאיר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.793049\",\"longitude\": \"34.640119\",\"city\": \"אשדוד\",\"address\": \"1 רח' הרצל\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.961768\",\"longitude\": \"34.799993\",\"city\": \"ראשון לציון\",\"address\": \"16 זבוטינסקי\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.7714759\",\"longitude\": \"35.3006227\",\"city\": \"מעלה אדומים\",\"address\": \"5 דרך קדם\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"30.987638\",\"longitude\": \"34.927942\",\"city\": \"ירוחם\",\"address\": \"צבי בורנשטין\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.4775767\",\"longitude\": \"34.9772593\",\"city\": \"פרדס חנה\",\"address\": \"17  רחוב הבנים\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.981331\",\"longitude\": \"35.556246\",\"city\": \"חצור הגלילית\",\"address\": \"קניון גליל עליון\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.028582\",\"longitude\": \"34.744495\",\"city\": \"בת ים\",\"address\": \"25 בלפור\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.008401\",\"longitude\": \"34.745649\",\"city\": \"בת ים\",\"address\": \"25 אורט ישראל\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.16001\",\"longitude\": \"34.806925\",\"city\": \"הרצליה\",\"address\": \"9 המנופים\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.785272\",\"longitude\": \"34.962508\",\"city\": \"טירת הכרמל\",\"address\": \"5 נחום חת\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.6947556\",\"longitude\": \"35.4207373\",\"city\": \"כפר תבור\",\"address\": \"4 רחוב הבזלת\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.825495\",\"longitude\": \"34.9830057\",\"city\": \"חיפה\",\"address\": \"9   ככר מאירהוף\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"33.0009766\",\"longitude\": \"35.0971756\",\"city\": \"נהריה\",\"address\": \"36 לוחמי הגטאות\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.932421\",\"longitude\": \"34.880757\",\"city\": \"רמלה\",\"address\": \"1 היצירה\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.884666\",\"longitude\": \"34.737795\",\"city\": \"יבנה\",\"address\": \"3 דרך הים\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"29.567833\",\"longitude\": \"34.959124\",\"city\": \"אילת\",\"address\": \"הסתת 15 א תעשיה\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.074316\",\"longitude\": \"34.790904\",\"city\": \"תל אביב\",\"address\": \"132דרך מנחם בגין\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.011686\",\"longitude\": \"34.778473\",\"city\": \"חולון\",\"address\": \"7  גולדה מאיר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.6424026\",\"longitude\": \"35.0946188\",\"city\": \"יוקנעם\",\"address\": \"9 שדרות רבין\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.1922595\",\"longitude\": \"34.8924675\",\"city\": \"כפר סבא\",\"address\": \"3 רפפורט\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.247654\",\"longitude\": \"35.196142\",\"city\": \"ערד\",\"address\": \"7 המנוף\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.6758060\",\"longitude\": \"34.6066860\",\"city\": \"אשקלון\",\"address\": \"פאואר סנטר סילבר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.3074738\",\"longitude\": \"34.8759781\",\"city\": \"נתניה\",\"address\": \"1 קלאוזנר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.219654\",\"longitude\": \"34.980848\",\"city\": \"צור יגאל\",\"address\": \"מרכז מסחרי\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.3179929\",\"longitude\": \"34.6247938\",\"city\": \"אופקים\",\"address\": \"25 זבוטינסקי\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.14454\",\"longitude\": \"34.802798\",\"city\": \"רמת השרון\",\"address\": \"צומת גלילות\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.946748\",\"longitude\": \"34.8423917\",\"city\": \"באר יעקב\",\"address\": \"2 היוצר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.824091\",\"longitude\": \"35.23737\",\"city\": \"ירושלים\",\"address\": \"106 שד' משה דיין\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.5693022\",\"longitude\": \"34.9573326\",\"city\": \"זכרון יעקב\",\"address\": \"2 הדגן\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.811393\",\"longitude\": \"34.960228\",\"city\": \"חיפה\",\"address\": \"55 שלמה המלך\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.9575524\",\"longitude\": \"35.4984901\",\"city\": \"צפת\",\"address\": \"20 ויצמן\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.196998\",\"longitude\": \"34.877237\",\"city\": \"רעננה\",\"address\": \"2 רח.המלאכה\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"33.2231978\",\"longitude\": \"35.5768526\",\"city\": \"קרית שמונה\",\"address\": \"צומת המצודות\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.730963\",\"longitude\": \"35.186597\",\"city\": \"ירושלים\",\"address\": \"17 ביה ויצחק\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.2658812\",\"longitude\": \"34.7638392\",\"city\": \"באר שבע\",\"address\": \"15 החשמונאים\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.6098289\",\"longitude\": \"35.2960997\",\"city\": \"עפולה\",\"address\": \"5 כורש\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.276264\",\"longitude\": \"34.860994\",\"city\": \"נתניה\",\"address\": \"5 גיבורי ישראל\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.065527\",\"longitude\": \"34.792465\",\"city\": \"תל אביב\",\"address\": \"76 יגאל אלון\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.986408\",\"longitude\": \"34.765041\",\"city\": \"ראשון לציון\",\"address\": \"8 הלחי\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.9336026\",\"longitude\": \"34.8882347\",\"city\": \"רמלה\",\"address\": \"40 הצופית\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.9051648\",\"longitude\": \"35.2841909\",\"city\": \"כרמיאל\",\"address\": \"2 ההגנה\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.806591\",\"longitude\": \"35.077555\",\"city\": \"קרית אתא\",\"address\": \"52 דרך חיפה\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.890305\",\"longitude\": \"34.965217\",\"city\": \"מודיעין\",\"address\": \"שדרות המלאכות\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.5208618\",\"longitude\": \"34.5943774\",\"city\": \"שדרות\",\"address\": \"8 סמטת הפלדה\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.845566\",\"longitude\": \"35.091021\",\"city\": \"קרית ביאליק\",\"address\": \"192  דרך עכו\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.9287241\",\"longitude\": \"35.3275658\",\"city\": \"כרמיאל\",\"address\": \"6 מעלה כמון\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.282656\",\"longitude\": \"34.799092\",\"city\": \"באר שבע\",\"address\": \"28 הערים התאומות\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"29.564252\",\"longitude\": \"34.945209\",\"city\": \"אילת\",\"address\": \"52 ירושלים השלמה\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.622285\",\"longitude\": \"35.314951\",\"city\": \"עפולה\",\"address\": \"5 שד' יצחק רבין\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.4009666\",\"longitude\": \"34.9044028\",\"city\": \"עמק חפר\",\"address\": \"א.ת. עמק חפר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.172327\",\"longitude\": \"34.928496\",\"city\": \"כפר סבא\",\"address\": \"g ויצמן 300 מתחם\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.976056\",\"longitude\": \"34.811256\",\"city\": \"ראשון לציון\",\"address\": \"30 שמוטקין\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.7786000\",\"longitude\": \"34.6360660\",\"city\": \"אשדוד\",\"address\": \"3 שבט ראובן\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.785234\",\"longitude\": \"35.335053\",\"city\": \"מעלה אדומים\",\"address\": \"2 נביעות\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.025667\",\"longitude\": \"34.89922\",\"city\": \"יהוד\",\"address\": \"אלטלף 4 א.ת יהוד\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.6875112\",\"longitude\": \"34.5793385\",\"city\": \"אשקלון\",\"address\": \"8 אדמונד רוטשילד\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.9414022\",\"longitude\": \"34.832154\",\"city\": \"באר יעקב\",\"address\": \"17 שא נס\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.3965405\",\"longitude\": \"34.7582123\",\"city\": \"רהט\",\"address\": \"עומר אלמוכתאר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.103036\",\"longitude\": \"34.834797\",\"city\": \"בני ברק\",\"address\": \"28 הלחי\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.088314\",\"longitude\": \"34.859683\",\"city\": \"פתח תקווה\",\"address\": \"17 יצחק רבין\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.6038987\",\"longitude\": \"34.7578512\",\"city\": \"קרית גת\",\"address\": \"178 מלכי ישראל\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.986924\",\"longitude\": \"35.705609\",\"city\": \"קצרין\",\"address\": \".חרמון 7 א ת\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.7897224\",\"longitude\": \"35.0105696\",\"city\": \"חיפה\",\"address\": \"54 שמחה גולן\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.4395829\",\"longitude\": \"34.8821498\",\"city\": \"גבעת אולגה\",\"address\": \"1 ברוך בוארון\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.666227\",\"longitude\": \"35.238762\",\"city\": \"מגדל העמק\",\"address\": \"אזור התעשייה ב\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.924095\",\"longitude\": \"35.082676\",\"city\": \"עכו\",\"address\": \"67 רמז בן-עמי\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.677714\",\"longitude\": \"35.241813\",\"city\": \"מגדל העמק\",\"address\": \"77 שד' מדע\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.892041\",\"longitude\": \"34.789416\",\"city\": \"רחובות\",\"address\": \"46  רח משה יתום\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.7889304\",\"longitude\": \"35.5352045\",\"city\": \"טבריה\",\"address\": \"1 העמקים\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.9080719\",\"longitude\": \"35.2933929\",\"city\": \"כרמיאל\",\"address\": \"100 מורד הגיא\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"33.0192556\",\"longitude\": \"35.2830142\",\"city\": \"מעלות\",\"address\": \"קניון כוכב הצפון\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.8160629\",\"longitude\": \"35.0534201\",\"city\": \"חיפה\",\"address\": \"4 יהודה איתין\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.750423\",\"longitude\": \"35.208121\",\"city\": \"ירושלים\",\"address\": \"17 האומן\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.435181\",\"longitude\": \"34.9069223\",\"city\": \"חדרה\",\"address\": \"1 ארבע אגודות\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.8609790\",\"longitude\": \"34.8168840\",\"city\": \"קרית עקרון\",\"address\": \"בילו סנטר\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.495162\",\"longitude\": \"35.496458\",\"city\": \"בית שאן\",\"address\": \"ירושלים1פינת שחם\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.7256490\",\"longitude\": \"34.7484830\",\"city\": \"באר טוביה\",\"address\": \"ניר 1 אזהת\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"32.440147\",\"longitude\": \"34.931571\",\"city\": \"חדרה\",\"address\": \"32 צהל\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//        listJson.add("{\"latitude\": \"31.7669888\",\"longitude\": \"35.1961546\",\"city\": \"ירושלים\",\"address\": \"שחל 79 צומת בייט\",\"title\":\"שופרסל דיל (Shufersal)\"}");
//
//        ArrayList<Shop> listShops = new ArrayList<>();
//        Gson gson = new Gson();
//        for (int i = 0; i < listJson.size(); i++) {
//            listShops.add(gson.fromJson(listJson.get(i), Shop.class));
//            writeDataToCloud(listShops.get(i));
//        }
//    }
//
//    public void writeDataToCloud(Shop shop) {
//        // Create a new user with a first and last name
//        Map<String, Object> shopMap = new HashMap<>();
//        shopMap.put("title", shop.getTitle());
//        shopMap.put("city", shop.getCity());
//        shopMap.put("address", shop.getAddress());
//        shopMap.put("latitude", shop.getLatitude());
//        shopMap.put("longitude", shop.getLongitude());
////
////
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//// Add a new document with a generated ID
//        db.collection("shops")
//                .add(shopMap)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("MY_TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("MY_TAG", "Error adding document", e);
//                    }
//                });
//
//
//    }

    //===========================================================================

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
//        mainActivityPresenter.showShopLocatorView();

        if (checkMapServices()) {
            if (mLocationPermissionGranted) {
                mainActivityPresenter.showShopLocatorView();
            } else {
                getLocationPermission();
            }
        }
    }

    @Override
    public void showCatalogueView() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showShoppingCardView() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showShopLocatorView() {
        drawer.closeDrawer(GravityCompat.START);
    }

//    @Override
//    public void showProgress(){
//        progressFrame.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void hideProgress(){
//        progressFrame.setVisibility(View.GONE);
//    }

    @Override
    public void logout() {
        auth.signOut();

        //TODO clear list in presenter
        ShoppingListSingletone.getInstance().getShoppingList().clear();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    //check permissions for google maps

    private boolean checkMapServices() {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This part of application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes, sure", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                })
        .setNegativeButton("No, I'm paranoid!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mainActivityPresenter.showShopLocatorView();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK() {
        Log.d("MY_TAG", "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d("MY_TAG", "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d("MY_TAG", "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    mainActivityPresenter.showShopLocatorView();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MY_TAG", "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    mainActivityPresenter.showShopLocatorView();
                } else {
                    getLocationPermission();
                }
            }
        }

    }
}
