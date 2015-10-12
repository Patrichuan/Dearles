package dear.dearles.customclasses;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.client.Query;

import dear.dearles.R;
import dear.dearles.glide.CropSquareTransformation;

public class ChatArrayAdapter extends Custom_FireBaseListAdapter<ChatBubbleMessage> {

    String MyUsername, HerUsername;
    String MyProfilePictureURL, HerProfilePictureURL;
    ImageView MyProfilePictureImageView, HerProfilePictureImageView;
    Bitmap MyProfilePictureBMP, HerProfilePictureBMP;
    Activity activity;

    public ChatArrayAdapter(Query ref, Activity activity, int textViewResourceId, String MyUsername, String MyProfilePictureURL, String HerUsername, String HerProfilePictureURL) {
        super(ref, ChatBubbleMessage.class, textViewResourceId, activity);
        this.MyUsername = MyUsername;
        this.HerUsername = HerUsername;
        this.HerProfilePictureURL = HerProfilePictureURL;
        this.MyProfilePictureURL = MyProfilePictureURL;
        this.activity = activity;
        setProfileImageBMPs();
    }


    @Override
    protected void populateView(View view, ChatBubbleMessage chat) {
        LinearLayout singleMessageContainer = (LinearLayout) view.findViewById(R.id.singleMessageContainer);
        MyProfilePictureImageView = (ImageView) view.findViewById(R.id.Myprofilepic_iv);
        HerProfilePictureImageView = (ImageView) view.findViewById(R.id.Herprofilepic_iv);
        String username = chat.getName();
        String message = chat.getText();
        TextView chatText = (TextView) view.findViewById(R.id.singleMessage);

        // En caso de ser yo quien hablo
        if (username != null && username.equals(MyUsername)) {
            chatText.setText(message);
            HerProfilePictureImageView.setVisibility(View.INVISIBLE);
            if (!MyProfilePictureImageView.isShown()) {
                MyProfilePictureImageView.setVisibility(View.VISIBLE);
            }
            MyProfilePictureImageView.setImageBitmap(MyProfilePictureBMP);
            chatText.setBackgroundResource(R.drawable.bubble_left);
            singleMessageContainer.setGravity(Gravity.START);

        // En caso de ser la otra persona
        } else {
            chatText.setText(message);
            MyProfilePictureImageView.setVisibility(View.INVISIBLE);
            if (!HerProfilePictureImageView.isShown()) {
                HerProfilePictureImageView.setVisibility(View.VISIBLE);
            }
            HerProfilePictureImageView.setImageBitmap(HerProfilePictureBMP);
            chatText.setBackgroundResource(R.drawable.bubble_right);
            singleMessageContainer.setGravity(Gravity.END);
        }
    }


    // Get My and Her BMPs from Picture URLs
    public void setProfileImageBMPs () {
        // Get BMP from Set the profile image
        Glide.with(activity)
                .load(MyProfilePictureURL)
                .asBitmap()
                .transform(new CropSquareTransformation(activity))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        MyProfilePictureBMP = resource;
                    }
                });

        Glide.with(activity)
                .load(HerProfilePictureURL)
                .asBitmap()
                .transform(new CropSquareTransformation(activity))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        HerProfilePictureBMP = resource;
                    }
                });
    }


}