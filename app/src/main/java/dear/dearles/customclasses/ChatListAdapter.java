package dear.dearles.customclasses;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
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

        String author = chat.getName();
        String message = chat.getText();

        TextView name_tv = (TextView) view.findViewById(R.id.author);
        name_tv.setText(author + ": ");

        if (author != null && author.equals(mUsername)) {
            // Si el usuario actual es el que envia el mensaje entonces el texto aparece en Rojo
            name_tv.setTextColor(Color.RED);
        } else {
            // Y si no entonces aparece en azul
            name_tv.setTextColor(Color.BLUE);
        }

        TextView message_tv = (TextView) view.findViewById(R.id.message);
        message_tv.setText(message);
    }

}