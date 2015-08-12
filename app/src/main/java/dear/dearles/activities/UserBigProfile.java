package dear.dearles.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.parse.ParseFile;

import java.util.Arrays;

import dear.dearles.R;
import dear.dearles.customclasses.Imagecompressor;
import dear.dearles.glide.CropSquareTransformation;
import dear.dearles.glide.FullHeightTransformation;

public class UserBigProfile extends AppCompatActivity {

    String Username, Age, Description, ProfilePicture;
    TextView txtUsername,txtAge, txtDescription;
    ImageView imgProfilePicture;

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

        // Setups
        setupToolbar();

        // Locate the TextViews in userbigprofile_layout_layout.xml
        txtUsername = (TextView) findViewById(R.id.Username);
        txtAge = (TextView) findViewById(R.id.Age);
        txtDescription = (TextView) findViewById(R.id.Description);
        imgProfilePicture = (ImageView) findViewById(R.id.ProfilePicture);

        // Set results to the TextViews
        txtUsername.setText(Username);
        txtAge.setText(Age);
        txtDescription.setText(Description);


        Glide.with(UserBigProfile.this)
            .load(ProfilePicture)
            .asBitmap()
            .transform(new FullHeightTransformation(this))
            .into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    imgProfilePicture.setImageBitmap(resource);
                }
            });




        /*
        imgProfilePicture.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("Height del imgview: " + imgProfilePicture.getHeight()); //height is ready
                System.out.println("Width del imgview: " + imgProfilePicture.getWidth()); //height is ready

                Glide.with(UserBigProfile.this)
                    .load(ProfilePicture)
                    .asBitmap()
                    .transform(new FullHeightTransformation(UserBigProfile.this, imgProfilePicture.getWidth(), imgProfilePicture.getHeight()))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            imgProfilePicture.setImageBitmap(resource);
                        }
                    });
            }
        });
        */
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
