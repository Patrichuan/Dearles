package dear.dearles.fragments;

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
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.customclasses.User_ListViewAdapter;
import dear.dearles.customclasses.User;


public class AmistadTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // Todo - Particularizar consulta para sacar solo usuari@s que busquen Amistad
    int NORMAL_USER_RESULT_LIMIT = 50;
    DearApp app;
    // Declare Variables
    ListView listview;
    List<ParseUser> ParseUserList;
    User_ListViewAdapter adapter;
    List<User> UserList;
    ParseQuery<ParseUser> query;

    String Hashtag;
    ArrayList<String> UsersUsingHashtag;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.amistadtab_fragment, container, false);
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
            //
            app.UpdateUserLastKnownLocIfNeeded();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Inicializo el ArrayList de Users
            UserList = new ArrayList<User>();

            // Si el bundle devolvio Null en cada uno es porque estoy en Main (no vengo de Search)
            if ((Hashtag==null)||(UsersUsingHashtag==null)) {
                try {
                    // TODO - Meterle un whereequal tirando de localidad para reducir el peso de la query
                    query = ParseUser.getQuery();
                    ParseUserList = query.find();
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                for (ParseUser pUser : ParseUserList) {
                    if (UserList.size() > NORMAL_USER_RESULT_LIMIT) {
                        break;
                    }
                    UserList.add(app.ParseUsertoUser(pUser));
                }
                return null;
            }

            // Si el bundle devolvio valores distintos de null es porque estoy en SearchResult (vengo de Search)
            else {
                for (String UserName : UsersUsingHashtag) {
                    if (UserList.size() > NORMAL_USER_RESULT_LIMIT) {
                        break;
                    }
                    try {
                        query = ParseUser.getQuery();
                        query.whereEqualTo("username", UserName);
                        ParseUserList = query.find();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // Añadir primero el usuario con username = currentuser
                    UserList.add(app.ParseUsertoUser(ParseUserList.get(0)));
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