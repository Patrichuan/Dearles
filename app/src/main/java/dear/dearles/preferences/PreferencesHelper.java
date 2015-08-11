package dear.dearles.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import dear.dearles.Constants;
import dear.dearles.customclasses.User;


public class PreferencesHelper {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    User user;

    public PreferencesHelper (Context context) {
        this.context = context;
        user = new User();
        pref = context.getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void InitializeUserFromSharedpref () {

        // User info Preferences
        editor.putString("Username", user.getUsername());
        editor.putString("Password", user.getPassword());
        editor.putString("Age", user.getAge());
        editor.putString("Email", user.getEmail());
        editor.putString("ProfilePicture", user.getProfilePicture());
        editor.putString("Description", user.getDescription());
        editor.putString("Geopoint", user.getGeopoint());

        // Settings Preferences
        // editor.putBoolean("Notifications", false);
        // editor.putBoolean("Sincronization", true);
        // editor.putInt("Sincronizacion_interval", 30);

        editor.apply();
    }


    public void saveUserToSharedpref (User user) {
        editor.putString("Username", user.getUsername());
        editor.putString("Password", user.getPassword());
        editor.putString("Age", user.getAge());
        editor.putString("Email", user.getEmail());
        editor.putString("ProfilePicture", user.getProfilePicture());
        editor.putString("Description", user.getDescription());
        editor.putString("Geopoint", user.getGeopoint());

        editor.apply();
    }



    public User getUserFromSharedpref () {
        User aux = new User();

        aux.setUsername(pref.getString("Username", null));
        aux.setPassword(pref.getString("Password", null));
        aux.setAge(pref.getString("Age", null));
        aux.setEmail(pref.getString("Email", null));
        aux.setProfilePicture(pref.getString("ProfilePicture", null));
        aux.setDescription(pref.getString("Description", null));
        aux.setGeopoint(pref.getString("Geopoint", null));

        return aux;
    }

}