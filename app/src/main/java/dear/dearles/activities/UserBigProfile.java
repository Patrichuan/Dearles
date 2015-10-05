package dear.dearles.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.glide.FullHeightMinusStatusToolbarTransformation;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UserBigProfile extends AppCompatActivity {

    String Username, Age, Description, ProfilePicture;
    TextView txtHashtag1, txtHashtag2,txtHashtag3,txtHashtag4,txtHashtag5;
    ImageView imgProfilePicture;
    ArrayList<String> UserHashtags;

    protected DearApp app;
    Pattern MY_PATTERN = Pattern.compile("#(\\w+)");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userbigprofile_layout);
        app = (DearApp) getApplication();

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        Username = i.getStringExtra("username");
        ProfilePicture = i.getStringExtra("profilePicture");
        Description = i.getStringExtra("description");
        /*
        UserHashtags = new ArrayList<String>();
        if (bundle != null) {
            UserHashtags = bundle.getStringArrayList("hashtags"); // declare temp as ArrayList
        }
        */

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

        txtHashtag1 = (TextView) findViewById(R.id.Hashtag1);
        txtHashtag2 = (TextView) findViewById(R.id.Hashtag2);
        txtHashtag3 = (TextView) findViewById(R.id.Hashtag3);
        txtHashtag4 = (TextView) findViewById(R.id.Hashtag4);
        txtHashtag5 = (TextView) findViewById(R.id.Hashtag5);


        Matcher mat = MY_PATTERN.matcher(Description);
        int n = 0;
        // TODO - Hacerlo con un listview y sin limite con scroll
        while ((mat.find())&&(n<=4)) {
            if (n==0) txtHashtag1.setText(mat.group());
            if (n==1) txtHashtag2.setText(mat.group());
            if (n==2) txtHashtag3.setText(mat.group());
            if (n==3) txtHashtag4.setText(mat.group());
            if (n==4) txtHashtag5.setText(mat.group());
            n++;
        }


        /*
        // Set hashtags
        if (UserHashtags!=null) {
            int pos;
            for (String hashtag : UserHashtags) {
                pos = UserHashtags.indexOf(hashtag);
                System.out.println("Acabo de recoger: " + hashtag + " de la posicion " + pos);
                if (pos==0) txtHashtag1.setText(UserHashtags.get(0));
                if (pos==1) txtHashtag2.setText(UserHashtags.get(1));
                if (pos==2) txtHashtag3.setText(UserHashtags.get(2));
                if (pos==3) txtHashtag4.setText(UserHashtags.get(3));
                if (pos==4) txtHashtag5.setText(UserHashtags.get(4));
            }
        }
        */
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
        getMenuInflater().inflate(R.menu.menu_userbigprofile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                return true;
            case R.id.logout:
                if (app.isUserLoggedIn()) {
                    app.LogOutUser();
                    intent = new Intent(this, Login.class);
                    startActivity(intent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase, R.attr.customFont));
    }
}