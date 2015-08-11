package dear.dearles.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import dear.dearles.R;
import dear.dearles.customclasses.ImageLoader;
import dear.dearles.customclasses.User;

public class SingleItemView extends ActionBarActivity {

    // Declare Variables
    String Username;
    String Age;
    String Description;
    String Thumbnail;

    ImageLoader imageLoader = new ImageLoader(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleitemview);


        Intent i = getIntent();
        // Get the result of rank
        Username = i.getStringExtra("username");
        // Get the result of country
        Age = i.getStringExtra("age");
        // Get the result of population
        Description = i.getStringExtra("Description");
        // Get the result of flag
        Thumbnail = i.getStringExtra("Thumbnail");

        // Locate the TextViews in singleitemview.xml
        TextView txtUsername = (TextView) findViewById(R.id.Username);
        TextView txtAge = (TextView) findViewById(R.id.Age);
        TextView txtDescription = (TextView) findViewById(R.id.Description);

        // Locate the ImageView in singleitemview.xml
        ImageView imgThumbnail = (ImageView) findViewById(R.id.Thumbnail);

        // Set results to the TextViews
        txtUsername.setText(Username);
        txtAge.setText(Age);
        txtDescription.setText(Description);

        // Capture position and set results to the ImageView
        // Passes Thumbnail images URL into ImageLoader.class
        imageLoader.DisplayImage(Thumbnail, imgThumbnail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_item_view, menu);
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
