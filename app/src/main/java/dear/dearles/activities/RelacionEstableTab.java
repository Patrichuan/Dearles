package dear.dearles.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import dear.dearles.R;
import dear.dearles.customclasses.ListViewAdapter;
import dear.dearles.customclasses.User;


public class RelacionEstableTab extends Fragment {

    // Todo - Particularizar consulta para sacar solo usuari@s que busquen Relación Estable
    // Declare Variables
    ListView listview;
    List<ParseUser> ob;
    ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    private List<User> UserList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.relacionestabletab_fragment, container, false);

        listview = (ListView) rootView.findViewById(R.id.listview);
        new RemoteDataTask().execute();

        return rootView;
    }



    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
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
                user.setGeopoint(userObject.getString("geopoint"));
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
            System.out.println("Las dimensiones del listview son " + listview.getWidth() + "x" + listview.getHeight());

            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

}
