package dear.dearles.customclasses;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Query;

import dear.dearles.R;

public class ChatListAdapter extends Custom_FireBaseListAdapter<ChatMessage> {

    // El username del usuario actualmente logeado
    private String mUsername;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, ChatMessage.class, layout, activity);
        this.mUsername = mUsername;
    }

    @Override
    protected void populateView(View view, ChatMessage chat) {

        LinearLayout messageLayout = (LinearLayout) view.findViewById(R.id.Llmessage);

        String username = chat.getName();
        String message = chat.getText();

        TextView username_tv = (TextView) view.findViewById(R.id.username_tv);
        username_tv.setText(username + ": ");

        LayoutParams username_tv_params = (LayoutParams) username_tv.getLayoutParams();
        LayoutParams messageLayout_params = (LayoutParams) messageLayout.getLayoutParams();

        if (username != null && username.equals(mUsername)) {
            username_tv_params.addRule(RelativeLayout.ALIGN_PARENT_START);
            username_tv.setLayoutParams(username_tv_params);
            messageLayout_params.addRule(RelativeLayout.END_OF, username_tv.getId());

            // Si el usuario actual es el que envia el mensaje entonces el texto aparece en Rojo
            username_tv.setTextColor(Color.RED);
        } else {
            username_tv_params.addRule(RelativeLayout.ALIGN_PARENT_END);
            username_tv.setLayoutParams(username_tv_params);
            messageLayout_params.addRule(RelativeLayout.START_OF, username_tv.getId());
            // Y si no entonces aparece en azul
            username_tv.setTextColor(Color.BLUE);
        }

        TextView message_tv = (TextView) view.findViewById(R.id.message);
        message_tv.setText(message);
    }

}