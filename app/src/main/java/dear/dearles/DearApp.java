package dear.dearles;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import dear.dearles.customclasses.User;
import dear.dearles.parse.ParseHelper;
import dear.dearles.preferences.PreferencesHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class DearApp extends Application {

    private static DearApp instance;

    private ParseHelper DB;
    private PreferencesHelper Preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        InitializeParse();
        InitializePreferences();

        // Default Font: Roboto-Regular.ttf
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
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