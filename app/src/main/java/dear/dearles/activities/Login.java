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

public class Login extends AppCompatActivity {

    DearApp app;

    TextInputLayout usernameTil, passwordTil;
    TextView SignUptv;
    Button Loginbtn;
    CoordinatorLayout Coordinator;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

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

    // TODO - Comprobar que el email no este ya en uso
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

    @Override
    public void onBackPressed()
    {
        app.ExitIfTwiceBack(Coordinator);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase, R.attr.customFont));
    }

}