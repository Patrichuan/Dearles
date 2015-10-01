package dear.dearles;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.Status;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;

import dear.dearles.customclasses.LocationAwareness;
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
    private LocationAwareness Loc;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        InitializeScreenMeasurement();
        InitializeParse();
        InitializePreferences();
        InitializeLoc();

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


    // GOOGLE LOC METHODS---------------------------------------------------------------------------
    public void InitializeLoc () {
        Loc = new LocationAwareness(getContext());
    }

    public Location GetLastKnownLoc () {
        return Loc.GetLastKnownLoc();
    }

    public Status getLocationStatus () {
        return Loc.getLocationSettingsStatus();
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

    public ParseGeoPoint UpdateUserLoc (Location loc) {
        return DB.UpdateUserLoc(loc.getLatitude(), loc.getLongitude());
    }

    public void UpdateTopTenHashtags () {
        DB.UpdateTopTenHashtags();
    }

    public ArrayList<ParseObject> getTopTenHashtags () {
        return DB.getTopTenHashtag();
    }

    public Boolean isRdyTopTenHashtag () {
        return DB.isRdyTopTenHashtag();
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

    /*
    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }
    */

}