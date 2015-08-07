package dear.dearles.parse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import dear.dearles.activities.SignUp1;

public class ParseHelper {

    Context context;

    public ParseHelper (Context context) {
        this.context = context;
    }

    public void Initialize () {
        // Inicializamos PARSE
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "lDDd2LJ3svpj9w0OmjTWH5pKuOHduHPkOVwssvis", "TxamZobM8yshdA1QvmaM96ICxTCpV06iE8FVEBNC");
    }

    public void Test () {
        // Parse Test
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }

    // Todo Refactorizar
    public void SignUpUser(final String[] UserData, final String Description, Context context) {

        if (UserData[4] != null) {
           Glide.with(context)
                .load(Uri.parse(UserData[4]))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] data = stream.toByteArray();
                        final ParseFile pFile = new ParseFile("profile_thumbnail.jpg", data);
                        System.out.println("HOLAAAA, VOY AL SAVEINBG");

                        pFile.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                // If successful add file to user and signUpInBackground
                                if (null == e) {
                                    System.out.println("HE ENTRADO AL SAVEUSERTOPARSE");
                                    ParseUser user = new ParseUser();

                                    // Para evitar el bug del invalid token
                                    ParseUser.enableRevocableSessionInBackground();

                                    user.setUsername(UserData[0]);
                                    user.setPassword(UserData[1]);
                                    user.put("Age", UserData[2]);
                                    user.setEmail(UserData[3]);
                                    user.put("Description", Description);
                                    user.put("Thumbnail", pFile);

                                    user.signUpInBackground(new SignUpCallback() {
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                // Hooray! Let them use the app now.
                                                System.out.println("DONE");
                                            } else {
                                                // Sign up didn't succeed. Look at the ParseException to figure out what went wrong
                                                System.out.println("FAIL -> " + e);
                                            }
                                        }
                                    });
                                } else {
                                    System.out.println("e VALE: " + e);
                                }
                            }
                        });
                    }
                });
        } else {
            // Lo mismo pero con una foto estandar de profile




        }
    }

}
