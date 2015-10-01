package dear.dearles.parse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dear.dearles.R;
import dear.dearles.activities.Main;
import dear.dearles.customclasses.Imagecompressor;
import dear.dearles.customclasses.User;

public class ParseHelper {

    private Context context;

    // UpdateUserLoc
    private double LastLat, LastLong;

    // SaveUserHashtags and CreateorUpdateHashtag variables
    int pos;
    ArrayList<String> UserHashtags;
    String HashTag, Username;

    // UpdateTopTenHashtags
    private int skip = 0;
    private static ArrayList<ParseObject> MostUsedHashtags;
    private Boolean HashtagFeched;






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

        ParseACL roleACL = new ParseACL();
        roleACL.setPublicWriteAccess(true);
        roleACL.setPublicReadAccess(true);
        ParseRole role = new ParseRole("UpdateableByAnyone", roleACL);
        role.saveInBackground();

        MostUsedHashtags = new ArrayList<ParseObject>();

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
                    aux.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                                System.out.println("DONE");
                                SaveUserHashtags(user);
                            } else {
                                // Sign up didn't succeed. Look at the ParseException to figure out what went wrong
                                System.out.println("FAIL -> " + e);
                            }
                        }
                    });

                }
            });
        } else {
            aux.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Hooray! Let them use the app now.
                        System.out.println("DONE");
                        SaveUserHashtags(user);
                    } else {
                        // Sign up didn't succeed. Look at the ParseException to figure out what went wrong
                        System.out.println("FAIL -> " + e);
                    }
                }
            });
        }
    }



    public void SaveUserHashtags (User user) {
        Pattern MY_PATTERN = Pattern.compile("#(\\w+)");
        UserHashtags = new ArrayList<String>();
        Username = user.getUsername();
        pos = 0;
        Matcher mat = MY_PATTERN.matcher(user.getDescription());
        while (mat.find()) {
            UserHashtags.add(mat.group(1));
        }
        CreateorUpdateHashtag();
    }

    // Metodo recursivo que da de alta todos los hashtags de la descripcion de un usuario recien dado de alta
    public void CreateorUpdateHashtag () {
        // En cada iteración leo uno de los Hashtags de la descripcion
        HashTag = UserHashtags.get(pos);
        System.out.println("EL HASHTAG DE LA POSICION " + pos + " ES: " + HashTag);
        // Query que devuelve la row perteneciente al Hashtag recien leido
        ParseQuery<ParseObject> query = new ParseQuery<>("Hashtag");
        query.whereEqualTo("Tag", HashTag);
        // En esta lista guardare el resultado de la query
        List<ParseObject> queryList;
        ParseObject object;

        // Creo un postACL para que el hashtag pueda ser readeable/writable por cualquier usuario y no solo
        // por el creador de dicho hashtag
        ParseACL postACL = new ParseACL();
        postACL.setPublicReadAccess(true);
        postACL.setPublicWriteAccess(true);

        try {
            queryList = query.find();
            // En caso de NO haber devuelto ningun elemento he de crear un hashtag nuevo
            if (queryList.size()==0) {
                object = new ParseObject("Hashtag");
                object.setACL(postACL);
                object.put("Tag", HashTag);
                object.addUnique("Users", Arrays.asList(Username));
                object.put("Ocurrencies", 1);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        System.out.println("No existia ya el hashtag: " + HashTag + " (CREADO para usuario " + Username + ")");
                        if (++pos < UserHashtags.size()) {
                            CreateorUpdateHashtag();
                        }
                    }
                });
            // En caso de haber devuelto algun elemento entonces he de actualizar el hashtag para añadir el nombre del nuevo usuario
            // que ahora hace uso tambien de el
            } else {
                object = queryList.get(0);
                object.addUnique("Users", Arrays.asList(Username));
                object.put("Ocurrencies", ((int)object.get("Ocurrencies"))+1);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){
                            System.out.println("Existia el hashtag: " + HashTag + " (ACTUALIZADO para usuario " + Username + ")");
                            if (++pos < UserHashtags.size()) {
                                CreateorUpdateHashtag();
                            }
                        } else {
                            System.out.println("EXCEPTION: " + e.getMessage());
                        }
                    }
                });

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        UpdateTopTenHashtags();
    }


    // TODO - SEGUIR AQUI
    // Hace consultas de 1000 en 1000 a Parse (es el limite de rows que tiene Parse por defecto)
    public void UpdateTopTenHashtags () {
        // Reseteo la lista de todos los hashtags descargados
        MostUsedHashtags = new ArrayList<ParseObject>();
        // Reseteo el numero de rows a saltar en cada consulta
        skip = 0;
        // Reseteo la variable que indica si se han leido ya los 10 hashtag con mas ocurrencias
        HashtagFeched = false;
        // Creo mi query recursiva que trae rows de 1000 en 1000
        ParseQuery<ParseObject> queryMostUsedHashtags = ParseQuery.getQuery("Hashtag");
        queryMostUsedHashtags.orderByDescending("Ocurrencies");
        queryMostUsedHashtags.setLimit(1000);
        queryMostUsedHashtags.findInBackground(getAllObjects());
    }

    private FindCallback<ParseObject> getAllObjects(){
        return new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    MostUsedHashtags.addAll(objects);
                    int limit = 1000;
                    if (objects.size() == limit) {
                        skip = skip + limit;
                        ParseQuery query = new ParseQuery("Objects");
                        query.setSkip(skip);
                        query.setLimit(limit);
                        query.findInBackground(getAllObjects());
                    }
                    // Si no he alcanzado 1000 rows es porque ya no hay mas y por lo tanto puedo ya actualizar
                    // el Top10 de Hashtags
                    else {
                        // Reduzco mi lista a 10 elementos (los 10 hashtags con mas ocurrencias)
                        if (MostUsedHashtags.size()>10) MostUsedHashtags.subList(10,MostUsedHashtags.size()).clear();
                        HashtagFeched = true;

                        // Comprobacion rutinaria
                        System.out.println("EL TOP10 DE HASHTAGS LO CONFORMAN: ");
                        for (int i = 0; i<MostUsedHashtags.size(); i++) {
                            System.out.println(i + ". " + MostUsedHashtags.get(i));
                        }
                    }
                }
            }
        };
    }

    public ArrayList<ParseObject> getTopTenHashtag () {
        return MostUsedHashtags;
    }

    public Boolean isRdyTopTenHashtag () {
        return HashtagFeched;
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
    // y devuelve latitud y longitud como un ParseGeoPoint
    public ParseGeoPoint UpdateUserLoc(double latitude, double longitude) {
        // AL LOGEAR SE INICIALIZA !!
        final ParseGeoPoint geoPoint = new ParseGeoPoint(latitude, longitude);
        final ParseUser currentUser = ParseUser.getCurrentUser();
        boolean UpdateLocNeeded = false;

        System.out.println("El geopoint recien recogido es: " + latitude + " , " + longitude);

        // Si es la primera vez que voy a actualizar la posicion del usuario
        if ((LastLat == 0)&&(LastLong == 0)) {
            System.out.println("El ultimo geopoint conocido era 0,0 y por lo tanto he de actualizarlo si o si con el LastKnownLocation del dispositivo");
            LastLat = geoPoint.getLatitude();
            LastLong = geoPoint.getLongitude();
            UpdateLocNeeded = true;
        // En caso de no serlo
        } else {
            int y = Double.compare(LastLat, geoPoint.getLatitude());
            int x = Double.compare(LastLong, geoPoint.getLongitude());
            // Si la ultima posicion conocida es diferente (0 es que son iguales ambos double)
            if ((x!=0)||(y!=0)) {
                System.out.println("El ultimo geopoint conocido es: " + LastLat + " , " + LastLong);
                System.out.println("Y como las coordenadas recogidas son " + latitude + " , " + longitude + " he de actualizar el geopoint de parse si o si");
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
                        System.out.println("Hay necesidad de actualizar por lo tanto he de subir el geopoint a Parse");
                    }
                }
            });
        } else {
            System.out.println("No hubo necesidad de subir el geopoint a Parse");
        }

        return geoPoint;
    }

}