package dear.dearles.activities;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.customclasses.User_ListViewAdapter;
import dear.dearles.customclasses.User;


public class RelacionEstableTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // Todo - Particularizar consulta para sacar solo usuari@s que busquen Relación Estable
    protected DearApp app;
    // Declare Variables
    ListView listview;
    List<ParseUser> ob;
    User_ListViewAdapter adapter;
    private List<User> UserList = null;

    String Hashtag;
    ArrayList<String> UsersUsingHashtag;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.relacionestabletab_fragment, container, false);
        app = (DearApp) getActivity().getApplication();

        try {
            Bundle bundle = getArguments();
            // Lo usare para dar el title a la pantalla
            Hashtag = bundle.getString("Hashtag");
            // Lista de usuarios que he de listar en caso de provenir de una busqueda
            UsersUsingHashtag = bundle.getStringArrayList("UsersUsingHashtag");

        } catch (Exception e) {
            System.out.println("Excepcion: " + e.getMessage());
        }

        listview = (ListView) rootView.findViewById(R.id.listview_R);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_R);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        new RemoteDataTask().execute();
                                    }
                                }
        );

        return rootView;
    }

    @Override
    public void onRefresh() {
        new RemoteDataTask().execute();
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            // Actualizo la loc del usuario y la subo a parse si el cambio es significativo
            // En caso de serlo guardo la posicion para calcular posteriormente todas las distancias respecto a este
            ParseGeoPoint ActualGeopoint = app.UpdateUserLoc(app.GetLastKnownLoc());
            // Create the array
            UserList = new ArrayList<User>();

            Uri placeholderimageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + getResources().getResourcePackageName(R.drawable.bglogin)
                    + '/' + getResources().getResourceTypeName(R.drawable.bglogin)
                    + '/' + getResources().getResourceEntryName(R.drawable.bglogin));

            // No tienen un valor almacenado y por lo tanto estoy en Main
            if ((Hashtag==null)||(UsersUsingHashtag==null)) {
                try {
                    // Query para devolver los usuarios
                    ParseQuery<ParseUser> query;
                    query = ParseUser.getQuery();
                    ob = query.find();
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                for (ParseObject userObject : ob) {
                    if (UserList.size() > 50) {
                        break;
                    }
                    User user = new User();
                    user.setUsername(userObject.getString("username"));
                    user.setAge(userObject.getString("age"));
                    ParseFile image = (ParseFile) userObject.get("profilePicture");
                    if (image==null) {
                        user.setProfilePicture(placeholderimageUri.toString());
                    } else {
                        user.setProfilePicture(image.getUrl());
                    }
                    user.setDescription(userObject.getString("description"));
                    user.setGeopoint(userObject.getParseGeoPoint("geopoint"));

                    // Saco la distancia de mi punto al de todos los usuarios y almaceno en cada uno dicha distancia a mi
                    user.setDistance(ActualGeopoint.distanceInKilometersTo(userObject.getParseGeoPoint("geopoint")));
                    UserList.add(user);
                }
                return null;
            }
            // Tienen un valor almacenado y por lo tanto vengo de Search
            else {

                for (String UserName : UsersUsingHashtag) {
                    // TODO - Si pagas tener acceso a 75
                    // Solo muestro 50 resultados para usuarios normales
                    if (UserList.size() > 50) {
                        break;
                    }
                    try {
                        // Query para devolver los usuarios
                        ParseQuery<ParseUser> query;
                        query = ParseUser.getQuery();
                        query.whereEqualTo("username", UserName);
                        ob = query.find();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    User user = new User();

                    user.setUsername(ob.get(0).getString("username"));
                    System.out.println("USUARIO LEIDO DE LA BUSQUEDA EN PARSE: " + ob.get(0).getString("username"));

                    user.setAge(ob.get(0).getString("age"));
                    ParseFile image = (ParseFile) ob.get(0).get("profilePicture");
                    if (image==null) {
                        user.setProfilePicture(placeholderimageUri.toString());
                    } else {
                        user.setProfilePicture(image.getUrl());
                    }
                    user.setDescription(ob.get(0).getString("description"));
                    user.setGeopoint(ob.get(0).getParseGeoPoint("geopoint"));

                    // Saco la distancia de mi punto al de todos los usuarios y almaceno en cada uno dicha distancia a mi
                    user.setDistance(ActualGeopoint.distanceInKilometersTo(ob.get(0).getParseGeoPoint("geopoint")));
                    UserList.add(user);
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            // Pass the results into an ArrayAdapter
            adapter = new User_ListViewAdapter(getActivity(), UserList);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}