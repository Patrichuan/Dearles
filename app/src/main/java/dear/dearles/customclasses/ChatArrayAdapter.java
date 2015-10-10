package dear.dearles.customclasses;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Query;

import dear.dearles.R;

public class ChatArrayAdapter extends Custom_FireBaseListAdapter<ChatBubbleMessage> {

    // El username del usuario actualmente logeado
    private String mUsername;

    public ChatArrayAdapter(Query ref, Activity activity, int textViewResourceId, String mUsername) {
        super(ref, ChatBubbleMessage.class, textViewResourceId, activity);
        this.mUsername = mUsername;
    }

    @Override
    protected void populateView(View view, ChatBubbleMessage chat) {

        LinearLayout singleMessageContainer = (LinearLayout) view.findViewById(R.id.singleMessageContainer);

        String username = chat.getName();
        String message = chat.getText();

        TextView chatText = (TextView) view.findViewById(R.id.singleMessage);
        chatText.setText(username + "dice: " + message);

        if (username != null && username.equals(mUsername)) {
            chatText.setBackgroundResource(R.drawable.bubble_b);
            singleMessageContainer.setGravity(Gravity.LEFT);
        } else {
            chatText.setBackgroundResource(R.drawable.bubble_a);
            singleMessageContainer.setGravity(Gravity.RIGHT);
        }
    }

}