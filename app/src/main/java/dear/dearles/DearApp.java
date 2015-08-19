package dear.dearles;

import android.app.Application;
import android.content.Context;

import dear.dearles.customclasses.ScreenMeasurement;
import dear.dearles.customclasses.User;
import dear.dearles.parse.ParseHelper;
import dear.dearles.preferences.PreferencesHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class DearApp extends Application {

    private static DearApp instance;

    private ScreenMeasurement SP;
    private ParseHelper DB;
    private PreferencesHelper Preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        InitializeScreenMeasurement();
        InitializeParse();
        InitializePreferences();

        // Default Font: Roboto-Regular.ttf
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }


    // SCREENPROPORTION METHODS---------------------------------------------------------------------
    public void InitializeScreenMeasurement () {
        SP = new ScreenMeasurement(getContext());
    }

    public int getScreenHeight () {
        return SP.getScreenHeight();
    }

    public int getScreenWidth () {
        return SP.getScreenWidth();
    }

    public int getToolbarHeight () {
        return SP.getToolbarHeight();
    }

    public int getStatusBarHeight () {
        return SP.getStatusBarHeight();
    }




    // PARSE.com METHODS----------------------------------------------------------------------------
    public void InitializeParse () {
        DB = new ParseHelper(getContext());
        DB.Initialize();
    }

    public void SignUpUser (User user) {
        DB.SignUpUser(user, this);
    }

    public void SignInUser(User user, Context LoginContext) {
        DB.SignInUser(user, LoginContext);
    }

    public void LogOutUser() {
        DB.LogOutUser();
    }

    public Boolean isUserLoggedIn () {
        return DB.isUserLoggedIn();
    }





    // SHAREDPREFERENCES METHODS--------------------------------------------------------------------
    public void InitializePreferences () {
        Preferences = new PreferencesHelper(getContext());
    }

    public void InitializeUserFromSharedpref () {
        Preferences.InitializeUserFromSharedpref();
    }

    public void saveUserToSharedpref (User user) {
        Preferences.saveUserToSharedpref(user);
    }

    public User getUserFromSharedpref () {
        return Preferences.getUserFromSharedpref();
    }





    // ---------------------------------------------------------------------------------------------
    public static Context getContext() {
        return instance.getApplicationContext();
    }
}