package dear.dearles.customclasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
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
    String MyUsername, MyProfilepic, HerUsername, HerProfilePicture;
    ImageView Myprofilepic_iv, Herprofilepic_iv;
    Activity activity;

    public ChatArrayAdapter(Query ref, Activity activity, int textViewResourceId, String MyUsername, String MyProfilepic, String HerUsername, String HerProfilePicture) {
        super(ref, ChatBubbleMessage.class, textViewResourceId, activity);
        this.MyUsername = MyUsername;
        this.HerUsername = HerUsername;
        this.HerProfilePicture = HerProfilePicture;
        this.MyProfilepic = MyProfilepic;
        this.activity = activity;
    }


    @Override
    protected void populateView(View view, ChatBubbleMessage chat) {

        LinearLayout singleMessageContainer = (LinearLayout) view.findViewById(R.id.singleMessageContainer);

        Myprofilepic_iv = (ImageView) view.findViewById(R.id.Myprofilepic_iv);
        Herprofilepic_iv = (ImageView) view.findViewById(R.id.Herprofilepic_iv);

        String username = chat.getName();
        String message = chat.getText();

        TextView chatText = (TextView) view.findViewById(R.id.singleMessage);

        if (MyUsername != null && username.equals(MyUsername)) {
            // Aqui va nuestro username
            chatText.setText(MyUsername.toUpperCase() + "\n" + message);
            Herprofilepic_iv.setVisibility(View.INVISIBLE);
            if (!Myprofilepic_iv.isShown()) {
                Myprofilepic_iv.setVisibility(View.VISIBLE);
            }
            setMyProfileImage(MyProfilepic);
            chatText.setBackgroundResource(R.drawable.bubble_left);
            singleMessageContainer.setGravity(Gravity.LEFT);
        } else {
            // Aqui va el username del usuario al que hemos abierto el chat
            chatText.setText(HerUsername + "\n" + message);
            Myprofilepic_iv.setVisibility(View.INVISIBLE);
            if (!Herprofilepic_iv.isShown()) {
                Herprofilepic_iv.setVisibility(View.VISIBLE);
            }
            setHerProfileImage(HerProfilePicture);
            chatText.setBackgroundResource(R.drawable.bubble_right);
            singleMessageContainer.setGravity(Gravity.RIGHT);
        }
    }


    public void setMyProfileImage (String ProfilePicture) {
        // Set the profile image
        Glide.with(activity)
                .load(ProfilePicture)
                .asBitmap()
                .transform(new CropSquareTransformation(activity))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        System.out.println("READYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY !!!!");
                        Myprofilepic_iv.setImageBitmap(resource);
                    }
                });
    }

    public void setHerProfileImage (String ProfilePicture) {
        // Set the profile image
        Glide.with(activity)
                .load(ProfilePicture)
                .asBitmap()
                .transform(new CropSquareTransformation(activity))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        System.out.println("READYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY !!!!");
                        Herprofilepic_iv.setImageBitmap(resource);
                    }
                });
    }



}