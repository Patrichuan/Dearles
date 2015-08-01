package dear.dearles.activities;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import dear.dearles.DearApp;
import dear.dearles.R;

public class SignUp2 extends AppCompatActivity {

    protected DearApp app;

    TextView TipDescription, TextCount;
    EditText Description;
    ScrollView ScrollLayout;

    Button Finishbtn;

    int NumLineas = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup2_layout);

        // For let bg behind status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setupToolbar();

        app = (DearApp) getApplication();

        TipDescription = (TextView) findViewById(R.id.TipDescription);
        Description = (EditText) findViewById(R.id.Description);
        TextCount = (TextView) findViewById(R.id.Text_count);
        ScrollLayout = (ScrollView) findViewById(R.id.ScrollLayout);
        Finishbtn = (Button) findViewById(R.id.Finishbtn);

        // Si no existe userdata....
        if (app.getUserDescription()!=null)Description.setText(app.getUserDescription());

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
                TextCount.setText(Integer.toString(150 - Description.length()));
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
                app.setUserDescription(Description.getText().toString());
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





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up2, menu);
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