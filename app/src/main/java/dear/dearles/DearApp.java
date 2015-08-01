package dear.dearles;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

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


    // SHAREDPREFERENCES METHODS--------------------------------------------------------------------

    public void InitializePreferences() {
        Preferences = new PreferencesHelper(getContext());
    }

    public void InitializeUserData() {
        Preferences.InitializeUserData();
    }

    public void setUserData (String Username, String Password, String Age, String Email) {
        Preferences.setUserData(Username, Password, Age, Email);
    }

    public String[] getUserData () {
        return Preferences.getUserData();
    }

    public void setUserDescription (String Description) {
        Preferences.setUserDescription(Description);
    }

    public String getUserDescription () {
        return Preferences.getUserDescription();
    }


    // ---------------------------------------------------------------------------------------------

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}