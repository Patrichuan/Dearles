package dear.dearles.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dear.dearles.R;
import dear.dearles.DearApp;
import dear.dearles.customclasses.User;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

// ActionBarActivity esta deprecated a partir del API22 (Se sustituyó por AppCompatActivity)
public class Login extends AppCompatActivity {

    DearApp app;
    TextInputLayout usernameTil, passwordTil;
    TextView SignUptv;
    Button Loginbtn;
    User user;

    int backButtonCount;

    CoordinatorLayout Coordinator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        backButtonCount = 0;

        app = (DearApp) getApplication();
        Coordinator = (CoordinatorLayout) findViewById(R.id.Coordinator);

        user = new User();
        user = app.getUserFromSharedpref();

        usernameTil = (TextInputLayout) findViewById(R.id.usernameTil);
        usernameTil.setErrorEnabled(true);

        passwordTil = (TextInputLayout) findViewById(R.id.passwordTil);
        passwordTil.setErrorEnabled(true);

        // For let bg behind status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        SignUptv = (TextView) findViewById(R.id.SignUptv);
        SignUptv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp1.class);
                startActivity(intent);
            }
        });


        Loginbtn = (Button) findViewById(R.id.Loginbtn);
        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllCorrect()) {
                    app.SignInUser(user, Coordinator);
                }
            }
        });
    }


    // No deja validar hasta rellenar los 2 campos (usuario y password)
    private Boolean isAllCorrect () {

        String Username = usernameTil.getEditText().getText().toString();
        String Password = passwordTil.getEditText().getText().toString();

        Boolean Correct = false;
        Boolean UsernameCorrect = false;
        Boolean PasswordCorrect = false;

        if (Username.equals("")) {
            usernameTil.setError(getString(R.string.username_required));
        } else {
            usernameTil.setError("");
            UsernameCorrect = true;
        }

        if (Password.equals("")) {
            passwordTil.setError(getString(R.string.password_required));
        } else {
            passwordTil.setError("");
            PasswordCorrect = true;
        }

        if (UsernameCorrect && PasswordCorrect) {
            user.setUsername(usernameTil.getEditText().getText().toString());
            user.setPassword(passwordTil.getEditText().getText().toString());
            Correct = true;
        }

        return Correct;
    }


    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Snackbar snackbar = Snackbar.make(Coordinator, "Pulsa de nuevo 'back' para salir de la aplicación", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor((ContextCompat.getColor(Coordinator.getContext(), R.color.primary_dark)));
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(this, R.color.icons));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.f);
            snackbar.show();
            //Toast.makeText(this, "Pulsa de nuevo 'back' para salir de la aplicación.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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