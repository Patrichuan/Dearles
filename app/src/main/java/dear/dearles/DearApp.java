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

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.Status;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

import dear.dearles.customclasses.LocationAwareness;
import dear.dearles.customclasses.ScreenMeasurement;
import dear.dearles.customclasses.User;
import dear.dearles.firebase.FirebaseHelper;
import dear.dearles.parse.ParseHelper;
import dear.dearles.preferences.PreferencesHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class DearApp extends Application {

    private static DearApp instance;

    private ScreenMeasurement SP;
    private ParseHelper DB;
    private PreferencesHelper Preferences;
    private LocationAwareness Loc;
    private FirebaseHelper FireB;

    int backButtonCount;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        backButtonCount = 0;

        InitializeScreenMeasurement();
        InitializePreferences();
        InitializeParse();
        InitializeLoc();
        InitializeFirebase();

        // Default Font: Roboto-Regular.ttf
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }



    public void SignUpUser (User user) {
        backButtonCount = 0;
        DB.SignUpUser(user, this);
        FireB.SignUpUser(user);
    }

    // Trata de logear un usuario, en caso de fallar lanza un snackbar en el CoordinatorLayout pasado como parametro informando de que el logeo ha fallado
    public void SignInUser(User user, CoordinatorLayout Coordinator) {
        backButtonCount = 0;
        DB.SignInUser(user, Coordinator);
    }

    public void LogOutUser() {
        backButtonCount = 0;
        DB.LogOutUser();
        FireB.SignOut();
    }





    // SCREENPROPORTION RELATED METHODS---------------------------------------------------------------------
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





    // GOOGLE LOC RELATED METHODS---------------------------------------------------------------------------
    public void InitializeLoc () {
        Loc = new LocationAwareness(getContext());
    }

    public Location GetLastKnownLoc () {
        return Loc.GetLastKnownLoc();
    }

    public Status getLocationStatus () {
        return Loc.getLocationSettingsStatus();
    }






    // PARSE.com RELATED METHODS----------------------------------------------------------------------------
    public void InitializeParse () {
        DB = new ParseHelper(getContext(), Preferences);
        DB.Initialize();
    }



    public Boolean isUserLoggedIn () {
        return DB.isUserLoggedIn();
    }



    // Convierte un ParseUser en User y añade a este
    // - Posicion actual
    // - Distancia en Km entre Posicion actual y la ultima conocida de dicho usuario
    public User ParseUsertoUser (ParseUser pUser) {
        //DB.UpdateUserLastKnownLocIfNeeded(GetLastKnownLoc());
        return DB.ParseUserToUser(pUser, GetLastKnownLoc());
    }

    public void UpdateUserLastKnownLocIfNeeded () {
        DB.UpdateUserLastKnownLocIfNeeded(GetLastKnownLoc());

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






    // SHAREDPREFERENCES RELATED METHODS--------------------------------------------------------------------
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

    public String getCurrentUserName () {
        return Preferences.getUserFromSharedpref().getUsername();
    }

    public String getCurrentUserProfilePicture () {
        return Preferences.getUserFromSharedpref().getProfilePicture();
    }





    // FIREBASE RELATED METHODS-----------------------------------------------------------------------------
    public void InitializeFirebase () {
        FireB = new FirebaseHelper(getContext());
        FireB.setFireBase();
    }

    public void SignUpFireUser (User user) {
        FireB.SignUpUser(user);
    }

    public void SignInFireUser (User user) {
        FireB.SignInUser(user);
    }

    public void SignOut () {
        FireB.SignOut();
    }

    public Firebase getFireChild (String str) {
        return FireB.getFireChild(str);
    }

    public Firebase getFireBaseRef () {
        return FireB.getFireBaseRef();
    }




    // OTHER RELATED METHODS--------------------------------------------------------------------------------

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

    public String CreateChatRoomNumber (String str1, String str2) {
        String ChatRoomName = "";
        String ChatRoomNumber = "";
        // Transformo en minusculas ambos usernames
        int compare = str1.toLowerCase().compareTo(str2.toLowerCase());
        // Ordeno por orden alfabetico los usernames de los 2 participantes
        //str1 is smaller
        if (compare < 0) {
            ChatRoomName += str1 + "_" + str2;
        }
        else {
            //str1 is larger
            if (compare > 0) {
                ChatRoomName += str2 + "_" + str1;
            }
            //str1 is equal to str2
            else {
                ChatRoomName += str1 + "_" + str2;
            }
        }
        // Y cambio caracteres por numeros apoyandome en sus codigos ascii
        for(char c : ChatRoomName.toCharArray())
        {
            int value;
            value = c - 'a' + 1;
            ChatRoomNumber += value;
        }
        return ChatRoomNumber;
    }














    public static Context getContext() {
        return instance.getApplicationContext();
    }

}