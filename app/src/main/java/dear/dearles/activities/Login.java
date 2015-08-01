package dear.dearles.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import dear.dearles.R;
import dear.dearles.DearApp;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

// ActionBarActivity esta deprecated a partir del API22 (Se sustituyó por AppCompatActivity)
public class Login extends AppCompatActivity {

    protected DearApp app;
    TextView SignUptv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        app = (DearApp) getApplication();

        // Note
        //      FLAG FOR TRANSPARENT COLOR -->  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //

        // For let bg behind status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);



        SignUptv = (TextView) findViewById(R.id.SignUptv);
        SignUptv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esto se ha de lanzar cuando el usuario deslogee
                app.InitializeUserData();

                Intent intent = new Intent(Login.this, SignUp1.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_activity, menu);
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

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/
}