package dear.dearles;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

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

    int backButtonCount;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        backButtonCount = 0;

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

    public void SignInUser(User user, CoordinatorLayout Coordinator) {
        DB.SignInUser(user, Coordinator);
    }

    public void LogOutUser() {
        DB.LogOutUser();
    }

    public Boolean isUserLoggedIn () {
        return DB.isUserLoggedIn();
    }

    public User ParseUsertoUser (ParseUser pUser) {
        return DB.ParseUserToUser(pUser, GetLastKnownLoc());
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






    public void LaunchSingleSearchHashtag (String Tag) {
        DB.LaunchSingleSearchHashtag(Tag);
    }

    public ArrayList<ParseObject> getSingleSearchHashtag () {
        return DB.getSingleSearchHashtag();
    }

    public Boolean isRdySingleSearchHashtag () {
        return DB.isReadySingleSearchHashtag();
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

    // Exit App if use back button twice
    public void ExitIfTwiceBack (CoordinatorLayout Coordinator) {
        if(backButtonCount >= 1)
        {
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Snackbar snackbar = Snackbar.make(Coordinator, "Pulsa de nuevo 'back' para salir de la aplicación", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor((ContextCompat.getColor(Coordinator.getContext(), R.color.primary_dark)));
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(this, R.color.icons));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.f);
            snackbar.show();
            backButtonCount++;
        }
    }


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