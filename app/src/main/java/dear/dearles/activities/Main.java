package dear.dearles.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.customclasses.ViewPagerAdapter;
import dear.dearles.slidingtab.SlidingTabLayout;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Main extends AppCompatActivity {

    DearApp app;

    Button LogOutbtn;

    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Relación Estable","Amistad"};
    int Numboftabs =2;

    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // For let bg behind status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        app = (DearApp) getApplication();

        // Setups
        setupToolbar();
        //setupTabs();

        status = app.getLocationStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                System.out.println("All location settings are satisfied.");
                app.updateLastKnownLocation();
                setupTabs();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(Main.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    System.out.println("PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                System.out.println("Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                break;
        }

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
        */
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i("PRUEBA1", "User agreed to make required location settings changes.");
                        app.updateLastKnownLocation();
                        setupTabs();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("PRUEBA1", "User chose not to make required location settings changes.");
                        break;
                }
                break;
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

    private void setupTabs() {
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // tab_indicator define esteticamente las pestañas
        tabs.setCustomTabView(R.layout.tabtv_layout, android.R.id.text1);

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
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