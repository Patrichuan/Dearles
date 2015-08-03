package dear.dearles.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.client.UserTokenHandler;

import dear.dearles.DearApp;
import dear.dearles.R;

public class SignUp1 extends AppCompatActivity {

    protected DearApp app;

    Button Nextbtn;
    TextView Username, Password, RePassword, Email, Age;
    TextInputLayout usernameTil, passwordTil, repasswordTil, emailTil, ageTil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup1_layout);

        // For let bg behind status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        usernameTil = (TextInputLayout) findViewById(R.id.usernameTil);
        emailTil = (TextInputLayout) findViewById(R.id.emailTil);
        ageTil = (TextInputLayout) findViewById(R.id.ageTil);
        passwordTil = (TextInputLayout) findViewById(R.id.passwordTil);
        repasswordTil = (TextInputLayout) findViewById(R.id.repasswordTil);

        app = (DearApp) getApplication();

        // Setups
        setupToolbar();
        setupEditTexts();

        Nextbtn = (Button) findViewById(R.id.Nextbtn);
        Nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp1.this, SignUp2.class);
                if (isAllCorrect()) {

                    app.setUserData(Username.getText().toString(),
                            Password.getText().toString(),
                            Age.getText().toString(),
                            Email.getText().toString());

                    startActivity(intent);
                }
            }
        });
    }




    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Arrow menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }




    private void setupEditTexts () {
        // I define programatically all the edittexts
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Username = new EditText(this);
        Username.setId(R.id.usernametv);
        Username.setLayoutParams(params);
        Username.setSingleLine(true);
        Username.setMaxLines(1);
        Username.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Username.setHint(R.string.signup_username);

        Password = new EditText(this);
        Password.setId(R.id.passwordtv);
        Password.setLayoutParams(params);
        Password.setSingleLine(true);
        Password.setMaxLines(1);
        Password.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Password.setHint(R.string.signup_password);

        RePassword = new EditText(this);
        RePassword.setId(R.id.repasswordtv);
        RePassword.setLayoutParams(params);
        RePassword.setSingleLine(true);
        RePassword.setMaxLines(1);
        RePassword.setHintTextColor(getResources().getColor(R.color.primary_dark));
        RePassword.setHint(R.string.signup_repassword);

        Email = new EditText(this);
        Email.setId(R.id.emailtv);
        Email.setLayoutParams(params);
        Email.setSingleLine(true);
        Email.setMaxLines(1);
        Email.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Email.setHint(R.string.signup_email);

        Age = new EditText(this);
        Age.setId(R.id.agetv);
        Age.setLayoutParams(params);
        Age.setSingleLine(true);
        Age.setMaxLines(1);
        Age.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Age.setHint(R.string.signup_age);
        Age.setInputType(InputType.TYPE_CLASS_NUMBER);

        // And i check if they had previous information for setText or not
        // later i add them to their TextInputLayouts
        String[] UserData = app.getUserData();

        if (UserData[0]!=null) {
            Username.setText(UserData[0]);
            usernameTil.addView(Username);
        }

        if (UserData[1]!=null) {
            Password.setText(UserData[1]);
            passwordTil.addView(Password);
        }

        if (UserData[1]!=null) {
            RePassword.setText(UserData[1]);
            repasswordTil.addView(RePassword);
        }

        if (UserData[2]!=null) {
            Age.setText(UserData[2]);
            ageTil.addView(Age);
        }

        if (UserData[3]!=null) {
            Email.setText(UserData[3]);
            emailTil.addView(Email);
        }
    }




    // Aqui validare todos los TextViews
    private Boolean isAllCorrect () {
        return true;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up1, menu);
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
