package dear.dearles.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemClickListener;

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

    List parseUserList;
    ArrayList<User> usuarios;

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
        */

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
