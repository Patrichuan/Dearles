package dear.dearles.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.ArrayList;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.customclasses.Hashtag_ListViewAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Search extends AppCompatActivity {

    ListView mListview;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<ParseObject> HashtagRows;

    protected DearApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        app = (DearApp) getApplication();

        mListview = (ListView) findViewById(R.id.listview);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RemoteDataTask().execute();
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipeRefreshLayout.setRefreshing(true);
                                         new RemoteDataTask().execute();
                                     }
                                 }
        );

        // Setups
        setupToolbar();
    }


    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            app.UpdateTopTenHashtags();
            while (!app.isRdyTopTenHashtag()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            HashtagRows = app.getTopTenHashtags();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Pass the results into an ArrayAdapter
            Hashtag_ListViewAdapter adapter = new Hashtag_ListViewAdapter(Search.this, HashtagRows);
            // Binds the Adapter to the ListView
            mListview.setAdapter(adapter);
            // stopping swipe refresh
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Arrow menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase, R.attr.customFont));
    }

}