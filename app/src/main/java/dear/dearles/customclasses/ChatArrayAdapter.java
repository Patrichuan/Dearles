package dear.dearles.customclasses;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.client.Query;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.glide.CropSquareTransformation;

public class ChatArrayAdapter extends Custom_FireBaseListAdapter<ChatBubbleMessage> {

    // El username del usuario actualmente logeado
    String mUsername;
    List<ParseUser> ParseUserList;
    DearApp app;
    ArrayList<User> UserList;
    Activity activity;
    ImageView Myprofilepic, Herprofilepic;

    public ChatArrayAdapter(Query ref, Activity activity, int textViewResourceId, String mUsername) {
        super(ref, ChatBubbleMessage.class, textViewResourceId, activity);
        this.mUsername = mUsername;
        this.activity = activity;
        app = (DearApp) activity.getApplication();
        //new RemoteDataTask().execute();
    }

    @Override
    protected void populateView(View view, ChatBubbleMessage chat) {

        LinearLayout singleMessageContainer = (LinearLayout) view.findViewById(R.id.singleMessageContainer);
        Myprofilepic = (ImageView) view.findViewById(R.id.MyProfileImage);
        Herprofilepic = (ImageView) view.findViewById(R.id.HerProfileImage);

        String username = chat.getName();
        String message = chat.getText();

        TextView chatText = (TextView) view.findViewById(R.id.singleMessage);
        chatText.setText(mUsername.toUpperCase() + "\n"+ message);

        if (username != null && username.equals(mUsername)) {
            Herprofilepic.setVisibility(View.INVISIBLE);
            if (!Myprofilepic.isShown()) {
                Myprofilepic.setVisibility(View.VISIBLE);
            }
            Myprofilepic.setBackgroundResource(R.color.distance_near);
            chatText.setBackgroundResource(R.drawable.bubble_left);
            singleMessageContainer.setGravity(Gravity.LEFT);
        } else {
            Myprofilepic.setVisibility(View.INVISIBLE);
            if (!Herprofilepic.isShown()) {
                Herprofilepic.setVisibility(View.VISIBLE);
            }
            Herprofilepic.setBackgroundResource(R.color.distance_far);
            chatText.setBackgroundResource(R.drawable.bubble_right);
            singleMessageContainer.setGravity(Gravity.RIGHT);
        }
    }




    /*
    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Inicializo el ArrayList de Users
            UserList = new ArrayList<User>();
            ParseQuery<ParseUser> query;
            try {
                    query = ParseUser.getQuery();
                    query.whereEqualTo("username", mUsername);
                    ParseUserList = query.find();
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                for (ParseUser pUser : ParseUserList) {
                    UserList.add(app.ParseUsertoUser(pUser));
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set the profile image
            Glide.with(activity)
                    .load(UserList.get(0).getProfilePicture())
                    .asBitmap()
                    .transform(new CropSquareTransformation(activity))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            userprofilepic.setImageBitmap(resource);
                        }
                    });


        }
    }
    */

}