package dear.dearles.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;

import dear.dearles.DearApp;
import dear.dearles.R;

public class SignUp1 extends AppCompatActivity {

    protected DearApp app;

    Button Nextbtn;
    TextView Username, Password, Email, Age;
    TextInputLayout usernameTil, passwordTil, emailTil, ageTil;

    private ImageView ProfilePictureView;

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    final int IMAGE_PICKER_SELECT = 2;

    // Redimensiones que sufriran las imagenes que haga con la camara o tome de la galeria
    private static final int MAX_WIDTH = 300;
    private static final int MAX_HEIGHT = 300;
    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

    ImageView ProgressCircle;
    Animation a;

    String[] UserData;

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
        UserData = app.getUserData();

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
                    app.setUserData(Username.getText().toString(),
                            Password.getText().toString(),
                            Age.getText().toString(),
                            Email.getText().toString(),
                            UserData[4]);

                    startActivity(intent);
                }
            }
        });



        ProfilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aqui deberia de salir 3 botones (hacer foto, carrete, ver)

                /*
                // Hacer foto
                try {
                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(captureIntent, CAMERA_CAPTURE);
                } catch(ActivityNotFoundException anfe){
                    //display an error message
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast toast = Toast.makeText(SignUp1.this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
                */


                // Carrete
                try {
                    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, IMAGE_PICKER_SELECT);
                } catch(ActivityNotFoundException anfe){
                    //display an error message
                    String errorMessage = "Whoops - your device doesn't support pick images!";
                    Toast toast = Toast.makeText(SignUp1.this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }

                // Ver
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            Uri picUri = data.getData();
            /*
            Bundle extras = data.getExtras();
            Bitmap thePic = extras.getParcelable("data");

            ProfilePictureView.setImageBitmap(thePic);
            UserData[4]=(picUri.toString());




            //user is returning from capturing an image using the camera
            if(requestCode == CAMERA_CAPTURE){

                // ESTE BITMAP HAY QUE GUARDARLO PARA LA PAGINA DE PERFIL PANTALLA COMPLETA
                // ------------------------------------------------------------------------
                // Bundle extras = data.getExtras();
                // Bitmap thePic = extras.getParcelable("data");
                // Drawable drawable = new BitmapDrawable(getApplicationContext().getResources(), thePic);

                Picasso.with(this)
                        .load(picUri)
                        .transform(new CropSquareTransformation())
                        .fit()
                        .into(ProfilePictureView);
                UserData[4]=(picUri.toString());

            } else if (requestCode == IMAGE_PICKER_SELECT) {



                Picasso.with(this)
                        .load(picUri)
                        .transform(new CropSquareTransformation())
                        .into(ProfilePictureView);
                UserData[4]=(picUri.toString());

            */

                // Loads given image (LO LEO EN EL IMAGEVIEW)
                Picasso.with(this)
                        .load(picUri)
                        .transform(new CropSquareTransformation())
                        .resize(size, size)
                        .centerInside()
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        File file = new File(Environment.getExternalStorageDirectory().getPath() +"/profilepicture.jpg");
                                        if (file.exists()) file.delete();

                                        try
                                        {
                                            file.createNewFile();
                                            FileOutputStream ostream = new FileOutputStream(file);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                                            ostream.close();
                                            // Guardo la URI a la imagen reducida pequeña hecha ya thumbnail
                                            UserData[4] = Uri.fromFile(file).toString();

                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }

                                    }
                                }).start();

                                ProgressCircle.clearAnimation();
                                ProfilePictureView.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                ProgressCircle.startAnimation(a);
                                //ProgressCircle.setVisibility(View.VISIBLE);
                            }
                });
        }
    }



    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Arrow menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }




    // Crea programaticamente los floating edittext y los inicializa o rellena con información si ya
    // existia esta previamente. Tambien aplicable para la Profile Picture.
    private void setupEditTextsAndPicture () {
        // I define programatically all the edittexts
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Username = new EditText(this);
        Username.setId(R.id.usernametv);
        Username.setLayoutParams(params);
        Username.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        Username.setSingleLine(true);
        Username.setMaxLines(1);
        Username.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Username.setHint(R.string.username);
        usernameTil.setErrorEnabled(true);
        if (UserData[0]!=null) {
            Username.setText(UserData[0]);
        }


        Password = new EditText(this);
        Password.setId(R.id.passwordtv);
        Password.setLayoutParams(params);
        Password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        Password.setSingleLine(true);
        Password.setMaxLines(1);
        Password.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Password.setHint(R.string.password);
        passwordTil.setErrorEnabled(true);
        if (UserData[1] != null) {
            Password.setText(UserData[1]);
        }


        Age = new EditText(this);
        Age.setId(R.id.agetv);
        Age.setLayoutParams(params);
        Age.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        Age.setSingleLine(true);
        Age.setMaxLines(1);
        Age.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Age.setHint(R.string.age);
        Age.setRawInputType(Configuration.KEYBOARD_12KEY);
        ageTil.setErrorEnabled(true);
        if (UserData[2]!=null) {
            Age.setText(UserData[2]);
        }


        Email = new EditText(this);
        Email.setId(R.id.emailtv);
        Email.setLayoutParams(params);
        Email.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        Email.setSingleLine(true);
        Email.setMaxLines(1);
        Email.setHintTextColor(getResources().getColor(R.color.primary_dark));
        Email.setHint(R.string.email);
        Email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailTil.setErrorEnabled(true);
        if (UserData[3]!=null) {
            Email.setText(UserData[3]);
        }

        usernameTil.addView(Username);
        passwordTil.addView(Password);
        ageTil.addView(Age);
        emailTil.addView(Email);


        if (UserData[4]!=null){
            System.out.println("4 no es null " + UserData[4]);

            /*
            Picasso.with(this)
                    .load(Uri.parse(UserData[4]))
                    .transform(new CropSquareTransformation())
                    .skipMemoryCache()
                    .resize(size, size)
                    .centerInside()
                    .into(ProfilePictureView);
            */

            // Porque coño no da vueltas antes de cargar y las da al cargar????

            Picasso.with(this)
                    .load(Uri.parse(UserData[4]))
                    .skipMemoryCache()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ProgressCircle.clearAnimation();
                            ProfilePictureView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            ProgressCircle.startAnimation(a);
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
            usernameTil.setErrorEnabled(true);
        } else {
            usernameTil.setError("");
            UsernameCorrect = true;
        }


        if (passwordTil.getEditText().getText().toString().equals("")) {
            passwordTil.setError(getString(R.string.password_required));
            passwordTil.setErrorEnabled(true);
        } else {
            passwordTil.setError("");
            PasswordCorrect = true;
        }


        if (emailTil.getEditText().getText().toString().equals("")) {
            emailTil.setError(getString(R.string.email_required));
            emailTil.setErrorEnabled(true);
        } else {
            emailTil.setError("");
            EmailCorrect = true;
        }


        // && Condicion de que sea un numero
        // Falta la validación de la edad (si son digitos o no)
        // Tunear teclado numerico
        if (ageTil.getEditText().getText().toString().equals("")) {
            ageTil.setError(getString(R.string.age_required));
            ageTil.setErrorEnabled(true);
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



    // Custom transformation for PICASSO profile picture
    public class CropSquareTransformation implements Transformation {

        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);

            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() {
            return "square()";
        }
    }

}