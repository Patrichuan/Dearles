package dear.dearles.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.customclasses.User;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUp2 extends AppCompatActivity {

    protected DearApp app;

    int HASHTAG_LENGTH = 120;
    TextView TipDescription, TextCount;
    EditText Description;
    ScrollView ScrollLayout;
    TextInputLayout DescriptionTil;

    Button Finishbtn;

    User user;

    int NumLineas = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup2_layout);

        // For let bg behind status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setupToolbar();

        app = (DearApp) getApplication();

        user = new User();
        user = app.getUserFromSharedpref();

        TipDescription = (TextView) findViewById(R.id.TipDescription);
        DescriptionTil = (TextInputLayout) findViewById(R.id.DescriptionTil);
        TextCount = (TextView) findViewById(R.id.Text_count);
        ScrollLayout = (ScrollView) findViewById(R.id.ScrollLayout);
        Finishbtn = (Button) findViewById(R.id.Finishbtn);


        TextCount.setText(String.valueOf(HASHTAG_LENGTH));

        // Declaro el EditText para la descripción
        Description = new EditText(this);
        Description.setId(R.id.Description);
        Description.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Description.setLayoutParams(params);
        Description.setSingleLine(false);
        Description.setFilters(new InputFilter[]{new InputFilter.LengthFilter(HASHTAG_LENGTH)});
        Description.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Description.setHint(R.string.description);
        // Y establezco el contenido de esta si ya existia previamente
        if (user.getDescription()!=null) {
            Description.setText(user.getDescription());
        }
        // Y lo añado
        DescriptionTil.addView(Description);


        // I transform a normal string to html string (for blue hashtags)
        String str = getString(R.string.TipForDescription);
        CharSequence styledText = Html.fromHtml(str);
        TipDescription.setText(styledText);


        // Listener for get an additional scroll in the edittext (we need the character count always visible)
        Description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ScrollLayout.scrollTo(0, TextCount.getHeight() * (int) (NumLineas*0.9));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                TextCount.setText(Integer.toString(HASHTAG_LENGTH - Description.length()));
                if (NumLineas == Description.getLineCount()) {
                    // do nothing because we are in the first line
                } else {
                    // the number of lines have changed so we count them and update NumLineas
                    NumLineas = Description.getLineCount();
                    // and we scroll up TextCount height dps
                    ScrollLayout.scrollTo(0, TextCount.getHeight() * (int) (NumLineas*0.9));
                }
            }
        });


        Finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Y aqui es donde he de subir a Parse los datos del usuario
                user.setDescription(Description.getText().toString());
                app.SignUpUser(user);

                // Y me vuelvo a la pantalla de Login
                Intent intent = new Intent(SignUp2.this, Login.class);
                startActivity(intent);
            }
        });
    }




    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Arrow menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup2, menu);
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
}