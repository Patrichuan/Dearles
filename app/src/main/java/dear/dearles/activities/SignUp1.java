package dear.dearles.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.customclasses.User;
import dear.dearles.glide.CropSquareTransformation;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUp1 extends AppCompatActivity {

    DearApp app;

    Button Nextbtn;
    TextView usernametv, passwordtv, emailtv, agetv;
    TextInputLayout usernameTil, passwordTil, emailTil, ageTil;
    ImageView ProfilePictureView;

    ImageView ProgressCircle;
    Animation a;
    Uri picUri;
    User user;

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    final int IMAGE_PICKER_SELECT = 2;

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
        ProfilePictureView = (ImageView) findViewById(R.id.ProfilePictureView);

        app = (DearApp) getApplication();

        user = new User();
        user = app.getUserFromSharedpref();

        ProgressCircle = (ImageView) findViewById(R.id.ProgressCircle);
        a = AnimationUtils.loadAnimation(this, R.anim.progress_anim);
        a.setDuration(1000);

        // Setups
        setupToolbar();
        setupEditTextsAndPicture();

        Nextbtn = (Button) findViewById(R.id.Nextbtn);
        Nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp1.this, SignUp2.class);
                if (isAllCorrect()) {
                    user.setUsername(usernametv.getText().toString());
                    user.setPassword(passwordtv.getText().toString());
                    user.setAge(agetv.getText().toString());
                    user.setEmail(emailtv.getText().toString());
                    app.saveUserToSharedpref(user);
                    startActivity(intent);
                }
            }
        });


        ProfilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog d = new Dialog(SignUp1.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.camera_gallery_or_see);
                WindowManager.LayoutParams p = d.getWindow().getAttributes();
                p.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                d.getWindow().setAttributes(p);
                d.show();

                Button Camerabtn = (Button) d.findViewById(R.id.Camerabtn);
                Camerabtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            d.dismiss();
                            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(captureIntent, CAMERA_CAPTURE);
                        } catch(ActivityNotFoundException e){
                            //display an error message
                            String errorMessage = "Whoops - your device doesn't support capturing images!";
                            Toast toast = Toast.makeText(SignUp1.this, errorMessage, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });


                Button Gallerybtn = (Button) d.findViewById(R.id.Gallerytn);
                Gallerybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            d.dismiss();
                            Intent captureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(captureIntent, IMAGE_PICKER_SELECT);
                        } catch(ActivityNotFoundException e){
                            //display an error message
                            String errorMessage = "Whoops - your device doesn't support pick images!";
                            Toast toast = Toast.makeText(SignUp1.this, errorMessage, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
        });
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            ProgressCircle.startAnimation(a);
            picUri = data.getData();

            // Loads given image (LO LEO EN EL IMAGEVIEW)
            Glide.with(this)
                    .load(picUri)
                    .asBitmap()
                    .transform(new CropSquareTransformation(this))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            user.setProfilePicture(picUri.toString());
                            ProgressCircle.clearAnimation();
                            ProfilePictureView.setImageBitmap(resource);
                        }
                    });
        }
    }




    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Arrow menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }



    // Crea programaticamente los floating edittext y los inicializa o rellena con información si ya
    // existia esta previamente. Tambien aplicable para la Profile Picture.
    private void setupEditTextsAndPicture () {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        usernametv = new EditText(this);
        usernametv.setId(R.id.usernametv);
        usernametv.setLayoutParams(params);
        usernametv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        usernametv.setSingleLine(true);
        usernametv.setMaxLines(1);
        usernametv.setHintTextColor(getResources().getColor(R.color.primary_dark));
        usernametv.setHint(R.string.username);
        usernameTil.setErrorEnabled(true);
        if (user.getUsername()!=null) {
            usernametv.setText(user.getUsername());
        }


        passwordtv = new EditText(this);
        passwordtv.setId(R.id.passwordtv);
        passwordtv.setLayoutParams(params);
        passwordtv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        passwordtv.setSingleLine(true);
        passwordtv.setMaxLines(1);
        passwordtv.setHintTextColor(getResources().getColor(R.color.primary_dark));
        passwordtv.setHint(R.string.password);
        passwordTil.setErrorEnabled(true);
        if (user.getPassword()!= null) {
            passwordtv.setText(user.getPassword());
        }


        agetv = new EditText(this);
        agetv.setId(R.id.agetv);
        agetv.setLayoutParams(params);
        agetv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        agetv.setSingleLine(true);
        agetv.setMaxLines(1);
        agetv.setHintTextColor(getResources().getColor(R.color.primary_dark));
        agetv.setHint(R.string.age);
        agetv.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_NUMBER);
        ageTil.setErrorEnabled(true);
        if (user.getAge()!=null) {
            agetv.setText(user.getAge());
        }


        emailtv = new EditText(this);
        emailtv.setId(R.id.emailtv);
        emailtv.setLayoutParams(params);
        emailtv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        emailtv.setSingleLine(true);
        emailtv.setMaxLines(1);
        emailtv.setHintTextColor(getResources().getColor(R.color.primary_dark));
        emailtv.setHint(R.string.email);
        emailtv.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailTil.setErrorEnabled(true);
        if (user.getEmail()!=null) {
            emailtv.setText(user.getEmail());
        }


        usernameTil.addView(usernametv);
        passwordTil.addView(passwordtv);
        ageTil.addView(agetv);
        emailTil.addView(emailtv);


        if (user.getProfilePicture()!=null){
            ProgressCircle.startAnimation(a);
            Glide.with(this)
                    .load(Uri.parse(user.getProfilePicture()))
                    .asBitmap()
                    .transform(new CropSquareTransformation(this))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ProgressCircle.clearAnimation();
                            ProfilePictureView.setImageBitmap(resource);
                        }
                    });
        }
    }



    // No deja validar hasta rellenar los 4 campos (username, email, age, password)
    private Boolean isAllCorrect () {

        Boolean Correct = false;
        Boolean UsernameCorrect = false;
        Boolean PasswordCorrect = false;
        Boolean EmailCorrect = false;
        Boolean AgeCorrect = false;

        if (usernameTil.getEditText().getText().toString().equals("")) {
            usernameTil.setError(getString(R.string.username_required));
        } else {
            usernameTil.setError("");
            UsernameCorrect = true;
        }

        if (passwordTil.getEditText().getText().toString().equals("")) {
            passwordTil.setError(getString(R.string.password_required));
        } else {
            passwordTil.setError("");
            PasswordCorrect = true;
        }

        if (emailTil.getEditText().getText().toString().equals("")) {
            emailTil.setError(getString(R.string.email_required));
        } else {
            emailTil.setError("");
            EmailCorrect = true;
        }

        if (ageTil.getEditText().getText().toString().equals("")) {
            ageTil.setError(getString(R.string.age_required));
        } else {
            ageTil.setError("");
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
        getMenuInflater().inflate(R.menu.menu_signup1, menu);
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