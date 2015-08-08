package dear.dearles.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import dear.dearles.R;
import dear.dearles.DearApp;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

// ActionBarActivity esta deprecated a partir del API22 (Se sustituyó por AppCompatActivity)
public class Login extends AppCompatActivity {

    protected DearApp app;

    TextInputLayout usernameTil, passwordTil;
    TextView SignUptv;

    Button Loginbtn;
    String Username, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        app = (DearApp) getApplication();

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
                // Inicializo los datos de usuario para crear una nueva cuenta desde 0
                app.InitializeUserData();
                Intent intent = new Intent(Login.this, SignUp1.class);
                startActivity(intent);
            }
        });


        Loginbtn = (Button) findViewById(R.id.Loginbtn);
        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllCorrect()) {
                    // Todo - Externalizar esto en la clase ParseHelper
                    ParseUser.logInInBackground(Username, Password, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                System.out.println("CREDENCIALES CORRECTAS");
                                Toast.makeText(app.getContext(), "ESTAS DENTRO !!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, Main.class);
                                startActivity(intent);
                            } else {
                                // Signup failed. Look at the ParseException to see what happened.
                                System.out.println("FUCKING IMPOSTOR, NOOOO PASARAAS!!");
                                Toast.makeText(app.getContext(), "FUCKING IMPOSTOR, NOOOO PASARAAS!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


    // No deja validar hasta rellenar los 2 campos (usuario y password)
    private Boolean isAllCorrect () {

        Username = usernameTil.getEditText().getText().toString();
        Password = passwordTil.getEditText().getText().toString();

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
            Correct = true;
        }

        return Correct;
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