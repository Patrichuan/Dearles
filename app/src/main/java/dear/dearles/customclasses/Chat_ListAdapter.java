package dear.dearles.customclasses;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import dear.dearles.R;

public class Chat_ListAdapter extends FireBase_ListAdapter<Chat> {

    // El username del usuario actualmente logeado
    private String mUsername;

    public Chat_ListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
    }

    @Override
    protected void populateView(View view, Chat chat) {

        String author = chat.getAuthor();
        String message = chat.getMessage();

        TextView authorText = (TextView) view.findViewById(R.id.author);
        authorText.setText(author + ": ");

        if (author != null && author.equals(mUsername)) {
            // Si el usuario actual es el que envia el mensaje entonces el texto aparece en Rojo
            authorText.setTextColor(Color.RED);
        } else {
            // Y si no entonces aparece en azul
            authorText.setTextColor(Color.BLUE);
        }

        ((TextView) view.findViewById(R.id.message)).setText(message);
    }

}