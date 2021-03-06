package dear.dearles.firebase;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.ParseUser;

import dear.dearles.R;
import dear.dearles.activities.Main;
import dear.dearles.customclasses.User;

public class FirebaseHelper {

    Context context;
    Firebase myFirebaseRef;

    public FirebaseHelper (Context context) {
        this.context = context;
    }

    public void setFireBase () {
        Firebase.setAndroidContext(context);
        myFirebaseRef = new Firebase("https://blazing-fire-9511.firebaseio.com/");
        //TestFireBase ();
    }

    public void TestFireBase () {
        // WRITE "message" TO DB
        myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");
        // READ "message" FROM DB
        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

    public void SignUpUser (User user) {
        final String email = user.getEmail();
        final String password = user.getPassword();
        System.out.println("Estoy en FireBaseHelper SignUpUser y el email vale: " + email);
        myFirebaseRef.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                myFirebaseRef.authWithPassword(email, password, null);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                myFirebaseRef.authWithPassword(email, password, null);
            }
        });
    }

    public void SignInUser (User user) {








        System.out.println("Estoy en FireBaseHelper SignInUser y el email vale: " + user.getEmail());
        myFirebaseRef.authWithPassword(user.getEmail(), user.getPassword(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + " logeado con exito en FireBase");
                System.out.println("CREDENCIALES CORRECTAS en PARSE y en FIREBASE");
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }

    public void SignOut () {
        myFirebaseRef.unauth();
    }

    public Firebase getFireBaseRef () {
        return myFirebaseRef;
    }

    public Firebase getFireChild(String str) {
        return myFirebaseRef.child(str);
        //return myFirebaseRef.getRoot().child(str);
    }

}