package dear.dearles.parse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import dear.dearles.activities.Main;
import dear.dearles.customclasses.Imagecompressor;
import dear.dearles.customclasses.User;

public class ParseHelper {

    Context context;
    double LastLat, LastLong;

    public ParseHelper (Context context) {
        this.context = context;
    }

    public void Initialize () {
        // Inicializamos PARSE
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "lDDd2LJ3svpj9w0OmjTWH5pKuOHduHPkOVwssvis", "TxamZobM8yshdA1QvmaM96ICxTCpV06iE8FVEBNC");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        LastLat = 0;
        LastLong = 0;
    }

    public void Test () {
        // Parse Test
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }

    public void SignUpUser(final User user, Context context) {
        final ParseFile pFile;
        final ParseUser aux = new ParseUser();

        Imagecompressor Ic = new Imagecompressor(context);
        byte[] data;

        // Aplico los valores al nuevo usuario
        aux.setUsername(user.getUsername());
        aux.setPassword(user.getPassword());
        aux.put("age", user.getAge());
        aux.setEmail(user.getEmail());
        aux.put("description", user.getDescription());
        aux.put("geopoint", new ParseGeoPoint());

        if (user.getProfilePicture()!=null) {
            data = Ic.compressImage(user.getProfilePicture());
            pFile = new ParseFile("profile_thumbnail.jpg", data);
            pFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    ParseUser.enableRevocableSessionInBackground();
                    // If successful -> add file to user and signUpInBackground
                    aux.put("profilePicture", pFile);
                    SignUp(aux);
                }
            });
        } else {
            SignUp(aux);
        }
    }

    public void SignUp (ParseUser user) {
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    System.out.println("DONE");
                } else {
                    // Sign up didn't succeed. Look at the ParseException to figure out what went wrong
                    System.out.println("FAIL -> " + e);
                }
            }
        });
    }



    public void SignInUser (User user, final Context LoginContext) {
        System.out.println("ESTOY EN SIGNIN USER !!");
        ParseUser.logInInBackground(user.getUsername(), user.getPassword(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    System.out.println("CREDENCIALES CORRECTAS");
                    //Toast.makeText(context, "ESTAS DENTRO !!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginContext, Main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    System.out.println("FUCKING IMPOSTOR, NOOOO PASARAAS!!");
                    Toast.makeText(context, "FUCKING IMPOSTOR, NOOOO PASARAAS!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void LogOutUser () {
        ParseUser currentuser = ParseUser.getCurrentUser();
        if ((currentuser!=null)&&(currentuser.getUsername()!=null)) {
            System.out.println("Successfully Logged out");
            ParseUser.logOut();
        }
    }


    public Boolean isUserLoggedIn () {
        Boolean UserInside = false;
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            UserInside = true;
        } else {
            // show the signup or login screen
        }
        return UserInside;
    }


    // Metodo que actualiza en parse la posicion del usuario (solo en caso de ser diferente a la ultima almacenada)
    public void UpdateUserLoc(double latitude, double longitude) {
        final ParseGeoPoint geoPoint = new ParseGeoPoint(latitude, longitude);
        final ParseUser currentUser = ParseUser.getCurrentUser();
        boolean UpdateLocNeeded = false;

        System.out.println("El geopoint recien recogido es: " + latitude + " , " + longitude);

        // Si es la primera vez que voy a actualizar la posicion del usuario
        if ((LastLat == 0)&&(LastLong == 0)) {
            System.out.println("El ultimo geopoint conocido era 0,0");
            LastLat = geoPoint.getLatitude();
            LastLong = geoPoint.getLongitude();
            UpdateLocNeeded = true;
        // En caso de no serlo
        } else {
            int y = Double.compare(LastLat, geoPoint.getLatitude());
            int x = Double.compare(LastLong, geoPoint.getLongitude());
            // Si la ultima posicion conocida es diferente (0 es que son iguales ambos double)
            if ((x!=0)||(y!=0)) {
                System.out.println("El ultimo geopoint conocido era: " + LastLat + " , " + LastLong);
                System.out.println("Por lo tanto he de subir a parse el nuevo geopoint");
                LastLat = geoPoint.getLatitude();
                LastLong = geoPoint.getLongitude();
                UpdateLocNeeded = true;
            // Si la ultima posicion conocida es la misma (no hay necesidad de subirla a parse)
            } else {

            }
        }

        if (UpdateLocNeeded) {
            // En caso de
            currentUser.put("geopoint", geoPoint);
            currentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                    } else {
                        System.out.println("He subido el geopoint a Parse");
                    }
                }
            });
        } else {
            System.out.println("No hubo necesidad de subir el geopoint a Parse");
        }
    }

}