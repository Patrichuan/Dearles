package dear.dearles.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.UserTokenHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import bolts.Bolts;
import dear.dearles.DearApp;
import dear.dearles.R;

public class SignUp1 extends AppCompatActivity {

    protected DearApp app;

    Button Nextbtn;
    TextView Username, Password, Email, Age;
    TextInputLayout usernameTil, passwordTil, emailTil, ageTil;

    private ImageView ProfilePictureView;


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


        ProfilePictureView = (ImageView) findViewById(R.id.SignUpProfileiv);
        ProfilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        // Este bp es el que habria que guardar al final
        ProfilePictureView.setImageBitmap(bp);
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
        Username.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        Username.setSingleLine(true);
        Username.setMaxLines(1);
        Username.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Username.setHint(R.string.signup_username);

        Password = new EditText(this);
        Password.setId(R.id.passwordtv);
        Password.setLayoutParams(params);
        Password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        Password.setSingleLine(true);
        Password.setMaxLines(1);
        Password.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Password.setHint(R.string.signup_password);

        Email = new EditText(this);
        Email.setId(R.id.emailtv);
        Email.setLayoutParams(params);
        Email.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        Email.setSingleLine(true);
        Email.setMaxLines(1);
        Email.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Email.setHint(R.string.signup_email);
        Email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        Age = new EditText(this);
        Age.setId(R.id.agetv);
        Age.setLayoutParams(params);
        Age.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        Age.setSingleLine(true);
        Age.setMaxLines(1);
        Age.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Age.setHint(R.string.signup_age);
        Age.setRawInputType(Configuration.KEYBOARD_12KEY);

        // And i check if they had previous information for setText or not,
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

        if (UserData[2]!=null) {
            Age.setText(UserData[2]);
            ageTil.addView(Age);
        }

        if (UserData[3]!=null) {
            Email.setText(UserData[3]);
            emailTil.addView(Email);
        }
    }


    private Boolean isAllCorrect () {

        // Falta la validación de la foto y de la edad (si son digitos o no)

        Boolean Correct = false;
        Boolean UsernameCorrect = false;
        Boolean PasswordCorrect = false;
        Boolean EmailCorrect = false;
        Boolean AgeCorrect = false;

        if (usernameTil.getEditText().getText().toString().equals("")) {
            usernameTil.setError(getString(R.string.username_required));
            usernameTil.setErrorEnabled(true);
        } else {
            usernameTil.setErrorEnabled(false);
            UsernameCorrect = true;
        }


        if (passwordTil.getEditText().getText().toString().equals("")) {
            passwordTil.setError(getString(R.string.password_required));
            passwordTil.setErrorEnabled(true);
        } else {
            passwordTil.setErrorEnabled(false);
            PasswordCorrect = true;
        }


        if (emailTil.getEditText().getText().toString().equals("")) {
            emailTil.setError(getString(R.string.email_required));
            emailTil.setErrorEnabled(true);
        } else {
            emailTil.setErrorEnabled(false);
            EmailCorrect = true;
        }


        // && Condicion de que sea un numero
        if (ageTil.getEditText().getText().toString().equals("")) {
            ageTil.setError(getString(R.string.age_required));
            ageTil.setErrorEnabled(true);
        } else {
            ageTil.setErrorEnabled(false);
            AgeCorrect = true;
        }


        if (UsernameCorrect && PasswordCorrect && EmailCorrect && AgeCorrect) {
            Correct = true;
        }

        return Correct;
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