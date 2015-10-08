package dear.dearles.firebase;

import android.content.Context;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

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
        myFirebaseRef.createUser(user.getEmail(), user.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid") + " en FireBase");
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }

    public void SignInUser (User user) {
        System.out.println("Logeado con exito en FireBase con el email "+ user.getEmail());
        myFirebaseRef.authWithPassword(user.getEmail(), user.getPassword(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + " logeado con exito en FireBase");
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }

    public Firebase getFireBaseRef () {
        return myFirebaseRef;
    }

    public Firebase getFireChild(String str) {
        return myFirebaseRef.getRoot().child(str);
    }

}