package dear.dearles.activities;

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
            /* Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            */
        }

        @Override
        protected Void doInBackground(Void... params) {
            app.UpdateUserLoc(app.GetLastKnownLoc());
            // Create the array
            UserList = new ArrayList<User>();

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            try {
                ob = query.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            for (ParseObject userObject : ob) {
                User user = new User();

                user.setUsername(userObject.getString("username").toUpperCase());
                user.setAge(userObject.getString("age") + " años");
                ParseFile image = (ParseFile) userObject.get("profilePicture");
                user.setDescription(userObject.getString("description"));
                user.setGeopoint(userObject.getParseGeoPoint("geopoint"));
                user.setProfilePicture(image.getUrl());
                UserList.add(user);
            }

            return null;
        }

        // Todo - Vigilar que getActivity no devuelva null cuando el fragment esta detached
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