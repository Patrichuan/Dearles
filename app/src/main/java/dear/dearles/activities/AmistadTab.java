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
import dear.dearles.customclasses.ListViewAdapter;
import dear.dearles.customclasses.User;


public class AmistadTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // Todo - Particularizar consulta para sacar solo usuari@s que busquen Amistad
    protected DearApp app;
    // Declare Variables
    ListView listview;
    List<ParseUser> ob;
    ListViewAdapter adapter;
    private List<User> UserList = null;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.amistadtab_fragment, container, false);
        app = (DearApp) getActivity().getApplication();

        listview = (ListView) rootView.findViewById(R.id.listview_A);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_A);
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

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            try {
                ob = query.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            Uri placeholderimageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + getResources().getResourcePackageName(R.drawable.bglogin)
                    + '/' + getResources().getResourceTypeName(R.drawable.bglogin)
                    + '/' + getResources().getResourceEntryName(R.drawable.bglogin));

            for (ParseObject userObject : ob) {
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
                System.out.println("RELACION: La distancia de (" + ActualGeopoint.getLatitude() + "," + ActualGeopoint.getLongitude() + ") a (" +
                        userObject.getParseGeoPoint("geopoint").getLatitude() + "," + userObject.getParseGeoPoint("geopoint").getLongitude() +
                        ") es de " + ActualGeopoint.distanceInKilometersTo(userObject.getParseGeoPoint("geopoint")));

                UserList.add(user);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Pass the results into an ArrayAdapter
            adapter = new ListViewAdapter(getActivity(), UserList);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}