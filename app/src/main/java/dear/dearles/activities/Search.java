package dear.dearles.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;

import org.w3c.dom.Text;

import java.util.ArrayList;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.customclasses.Hashtag_ListViewAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Search extends AppCompatActivity {

    ListView mListview;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<ParseObject> HashtagTop10Rows;
    ArrayList<ParseObject> HashtagSingleSearchRow;

    Hashtag_ListViewAdapter adapter;
    Hashtag_ListViewAdapter adapter_backup;

    private Toolbar toolbar;
    private ActionBar ab;
    private MenuItem mSearchItem;
    private boolean isCloseiconVisible = false;
    private EditText searchEditText;

    TextView TextnotFound;

    protected DearApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        app = (DearApp) getApplication();

        // Setups
        setupToolbar();

        TextnotFound = (TextView) findViewById(R.id.TextNotFound);
        mListview = (ListView) findViewById(R.id.listview);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RemoteTop10DataTask().execute();
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipeRefreshLayout.setRefreshing(true);
                                         new RemoteTop10DataTask().execute();
                                     }
                                 }
        );

        searchEditText = (EditText) findViewById(R.id.edtSearch);
        searchEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(22)});
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 1) {
                    mSearchItem.setVisible(true);
                    mSearchItem.setEnabled(true);
                    mSearchItem.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_close_white_24dp, null));
                    isCloseiconVisible = true;
                } else {
                    mSearchItem.setVisible(false);
                    mSearchItem.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Listener to do a search when the user clicks on search button
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchEditText.length() >= 1) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        new RemoteSingleSearchDataTask().execute();
                    }
                }
                return false;
            }
        });
    }








    // RemoteTop10DataTask AsyncTask
    private class RemoteTop10DataTask extends AsyncTask<Void, Void, Void> {
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
            HashtagTop10Rows = app.getTopTenHashtags();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Pass the results into an ArrayAdapter
            adapter = new Hashtag_ListViewAdapter(Search.this, HashtagTop10Rows);
            // Lo guardo para que cuando se borre el edittext no tenga que consultar a Parse el Top10 de nuevo
            // a menos que el usuario haga un refresh
            adapter_backup = adapter;
            // Binds the Adapter to the ListView
            mListview.setAdapter(adapter);
            // stopping swipe refresh
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    // RemoteSingleSearchDataTask AsyncTask
    private class RemoteSingleSearchDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            app.LaunchSingleSearchHashtag(searchEditText.getText().toString());
            while (!app.isRdySingleSearchHashtag()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            HashtagSingleSearchRow = app.getSingleSearchHashtag();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Pass the results into an ArrayAdapter
            adapter = new Hashtag_ListViewAdapter(Search.this, HashtagSingleSearchRow);
            if (adapter.getCount()==0) {
                if (!TextnotFound.isShown()) TextnotFound.setVisibility(View.VISIBLE);
            } else {
                if (TextnotFound.isShown()) TextnotFound.setVisibility(View.INVISIBLE);
            }
            hideKeyboard(searchEditText);
            // Binds the Adapter to the ListView
            mListview.setAdapter(adapter);
            // stopping swipe refresh
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }







    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Arrow menu icon
        ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Override al onPrepare para poder instanciar el icono
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchItem = menu.findItem(R.id.clear);
        mSearchItem.setVisible(false);
        mSearchItem.setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
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
        // Associate searchable configuration with the SearchView
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                return true;
            case R.id.clear:
                if (isCloseiconVisible) {
                    searchEditText.setText("");
                    if (TextnotFound.isShown()) TextnotFound.setVisibility(View.INVISIBLE);
                    hideKeyboard(searchEditText);
                    mListview.setAdapter(adapter_backup);
                    //showKeyboard(searchEditText);
                }
                return true;
            case R.id.logout:
                if (app.isUserLoggedIn()) {
                    app.LogOutUser();
                    intent = new Intent(this, Login.class);
                    startActivity(intent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase, R.attr.customFont));
    }

}