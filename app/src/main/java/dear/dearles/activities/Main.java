package dear.dearles.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.customclasses.User;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Main extends AppCompatActivity {

    protected DearApp app;

    Button LogOutbtn;

    // Declare Variables
    ListView listview;
    List<ParseUser> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // For let bg behind status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        app = (DearApp) getApplication();

        // Setups
        setupToolbar();

        /*
        LogOutbtn = (Button) findViewById(R.id.LogOut);
        LogOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.LogOutUser();
                Intent intent = new Intent(Main.this, Login.class);
                startActivity(intent);
            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userObjects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    if (userObjects != null) {
                        //mUserAdapter.clear();
                        for (int i = 0; i < userObjects.size(); i++) {
                            // cada item de la lista es del tipo ParseUser
                            //mUserAdapter.add(userObjects.get(i));
                            System.out.println("USUARIO -> " + userObjects.get(i).getString("username"));
                        }
                    }
                } else {
                    // Something went wrong.
                }
            }
        });
        */
        new RemoteDataTask().execute();
    }



    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(Main.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Parse.com Simple ListView Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the class table named "Country" in Parse.com
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            try {
                ob = query.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
            // Pass the results into an ArrayAdapter
            adapter = new ArrayAdapter<String>(Main.this, R.layout.listview_item);
            // Retrieve object "username" from Parse.com database
            for (ParseObject userObject : ob) {
                System.out.println(userObject.getString("username"));
                adapter.add(userObject.getString("username"));
            }
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Todo - Si pilla bien la info porque no la muestra???
            // Close the progressdialog
            mProgressDialog.dismiss();
            // Capture button clicks on ListView items
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Send single item click data to SingleItemView Class
                    //Intent i = new Intent(Main.this, SingleItemView.class);
                    // Pass data "name" followed by the position
                    //i.putExtra("name", ob.get(position).getString("name").toString());
                    // Open SingleItemView.java Activity
                    //startActivity(i);
                }
            });
        }
    }



    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Arrow menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase, R.attr.customFont));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
