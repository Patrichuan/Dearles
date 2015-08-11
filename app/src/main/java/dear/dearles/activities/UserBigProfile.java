package dear.dearles.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import dear.dearles.R;
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

        // Locate the TextViews in userbigprofile_layout_layout.xml
        txtUsername = (TextView) findViewById(R.id.Username);
        txtAge = (TextView) findViewById(R.id.Age);
        txtDescription = (TextView) findViewById(R.id.Description);
        imgProfilePicture = (ImageView) findViewById(R.id.ProfilePicture);

        // Set results to the TextViews
        txtUsername.setText(Username);
        txtAge.setText(Age);
        txtDescription.setText(Description);
        Glide.with(this)
                .load(ProfilePicture)
                .asBitmap()
                //.override(1080, 1920)
                .transform(new CropSquareTransformation(this))
                //.transform(new FullHeightTransformation(this))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imgProfilePicture.setImageBitmap(resource);
                    }
                });
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
