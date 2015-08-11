package dear.dearles.parse;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import dear.dearles.activities.Main;
import dear.dearles.customclasses.Imagecompressor;
import dear.dearles.customclasses.User;

public class ParseHelper {

    Context context;

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
    }

    public void Test () {
        // Parse Test
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }

    // Todo - Crear caso en que no haya foto de perfil
    public void SignUpUser(final User user, Context context) {

        Imagecompressor Ic = new Imagecompressor(context);
        byte[] data;

        if (user.getProfilePicture() != null) {
            data = Ic.compressImage(user.getProfilePicture());
            if (data != null) {
                final ParseFile pFile = new ParseFile("profile_thumbnail.jpg", data);
                System.out.println("HOLAAAA, VOY AL SAVEINBG");
                pFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        // If successful -> add file to user and signUpInBackground
                        if (null == e) {
                            System.out.println("HE ENTRADO AL SAVEUSERTOPARSE");
                            ParseUser aux = new ParseUser();

                            // Para evitar el bug del invalid token
                            ParseUser.enableRevocableSessionInBackground();

                            aux.setUsername(user.getUsername());
                            aux.setPassword(user.getPassword());
                            aux.put("age", user.getAge());
                            aux.setEmail(user.getEmail());
                            aux.put("profilePicture", pFile);
                            aux.put("description", user.getDescription());
                            // Todo - Habilitar geopoints
                            //aux.put("geopoint", user.getGeopoint());

                            aux.signUpInBackground(new SignUpCallback() {
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
                        } else {
                            System.out.println("e VALE: " + e);
                        }
                    }
                });
            // Lo mismo pero con una foto estandar de profile
            } else {




            }
        }
    }


    public void SignInUser (User user, final Context LoginContext) {
        // Todo - Cada vez que logees actualizar un campo Geopoint para poder poner las distancias
        System.out.println("ESTOY EN SIGNIN USER !!");
        ParseUser.logInInBackground(user.getUsername(), user.getPassword(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    System.out.println("CREDENCIALES CORRECTAS");
                    Toast.makeText(context, "ESTAS DENTRO !!", Toast.LENGTH_SHORT).show();
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


}