package dear.dearles.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import dear.dearles.Constants;


public class PreferencesHelper {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public PreferencesHelper (Context context) {
        this.context = context;
        pref = context.getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void InitializeUserPersonalData () {
        // User info Preferences
        editor.putString("Username", null);
        editor.putString("Password", null);
        editor.putString("Age", null);
        editor.putString("Email", null);
        editor.putString("ProfilePicture", null);

        editor.putString("Description", null);

        // Settings Preferences
        // editor.putBoolean("Notifications", false);
        // editor.putBoolean("Sincronization", true);
        // editor.putInt("Sincronizacion_interval", 30);

        editor.apply();
    }

    public void setUserData (String Username, String Password, String Age, String Email, String ProfileStringUri) {
        editor.putString("Username", Username);
        editor.putString("Password", Password);
        editor.putString("Age", Age);
        editor.putString("Email", Email);
        editor.putString("ProfilePicture", ProfileStringUri);
        editor.apply();
    }

    public String[] getUserData () {
        String[] UserData = new String[5];

        UserData[0] = pref.getString("Username", null);
        UserData[1] = pref.getString("Password", null);
        UserData[2] = pref.getString("Age", null);
        UserData[3] = pref.getString("Email", null);
        UserData[4] = pref.getString("ProfilePicture", null);

        return UserData;
    }

    public void setUserDescription (String Description) {
        editor.putString("Description", Description);
        editor.apply();
    }

    public String getUserDescription () {
        return pref.getString("Description", null);
    }
}