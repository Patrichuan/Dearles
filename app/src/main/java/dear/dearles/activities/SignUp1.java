package dear.dearles.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.client.UserTokenHandler;

import dear.dearles.DearApp;
import dear.dearles.R;

public class SignUp1 extends AppCompatActivity {

    protected DearApp app;

    Button Nextbtn;
    TextView Username, Password, RePassword, Email, Age;

    String[] UserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup1_layout);

        // For let bg behind status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setupToolbar();

        app = (DearApp) getApplication();

        Username = (TextView) findViewById(R.id.usernametv);
        Password = (TextView) findViewById(R.id.passwordtv);
        RePassword = (TextView) findViewById(R.id.confirmpasswordtv);
        Email = (TextView) findViewById(R.id.emailtv);
        Age = (TextView) findViewById(R.id.agetv);

        Nextbtn = (Button) findViewById(R.id.Nextbtn);


        UserData = new String[5];
        UserData = app.getUserData();

        // Si no existe userdata....
        if (UserData[0]!=null)Username.setText(UserData[0]);
        if (UserData[1]!=null)Password.setText(UserData[1]);
        if (UserData[1]!=null)RePassword.setText(UserData[1]);
        if (UserData[2]!=null)Age.setText(UserData[2]);
        if (UserData[3]!=null)Email.setText(UserData[3]);

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
