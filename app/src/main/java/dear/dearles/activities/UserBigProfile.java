package dear.dearles.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.glide.FullHeightMinusStatusToolbarTransformation;

public class UserBigProfile extends AppCompatActivity {

    String Username, Age, Description, ProfilePicture;
    TextView txtUsername,txtAge, txtDescription;
    ImageView imgProfilePicture;

    protected DearApp app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userbigprofile_layout);

        Intent i = getIntent();
        Username = i.getStringExtra("username");
        Age = i.getStringExtra("age");
        Description = i.getStringExtra("description");
        ProfilePicture = i.getStringExtra("profilePicture");
        System.out.println("USER.SETPROFILEPICTURE EN BIGPROFILE --------> " + ProfilePicture);

        app = (DearApp) getApplication();

        // Setups
        setupToolbar();

        imgProfilePicture = (ImageView)findViewById(R.id.ProfilePicture);
        Glide.with(UserBigProfile.this)
                .load(ProfilePicture)
                .asBitmap()
                .transform(new FullHeightMinusStatusToolbarTransformation(this, app))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imgProfilePicture.setImageBitmap(resource);
                    }
                });

        // Locate the TextViews in userbigprofile_layout_layout.xml
        txtUsername = (TextView) findViewById(R.id.Username);
        txtAge = (TextView) findViewById(R.id.Age);
        txtDescription = (TextView) findViewById(R.id.Description);

        // Set results to the TextViews
        txtUsername.setText(Username);
        txtAge.setText(Age);
        txtDescription.setText(Description);
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
        getMenuInflater().inflate(R.menu.menu_userbigprofile, menu);
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