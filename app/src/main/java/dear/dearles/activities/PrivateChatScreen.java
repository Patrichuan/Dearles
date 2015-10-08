package dear.dearles.activities;

import android.app.ListActivity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import dear.dearles.DearApp;
import dear.dearles.R;
import dear.dearles.customclasses.Chat;
import dear.dearles.customclasses.Chat_ListAdapter;

public class PrivateChatScreen extends ListActivity {

    DearApp app;

    private ValueEventListener mConnectedListener;
    private Chat_ListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privatechatscreen_layout);

        app = (DearApp) getApplication();

        // TODO - El nombre debe de ser el del destinatario
        setTitle("Chatting as " + app.getCurrentUserName());

        // Listener para el boton 'send' del teclado
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        // Listener para el boton 'send' del layout
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new Chat_ListAdapter(app.getFireChild("chat").limitToFirst(50), this, R.layout.chat_message, app.getCurrentUserName());
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // READ "connected" status FROM DB
        mConnectedListener = app.getFireChild(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(PrivateChatScreen.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PrivateChatScreen.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }



    @Override
    public void onStop() {
        super.onStop();
        app.getFireChild(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }



    // Leo el texto del edittex, lo paso a String, creo un objeto Chat y lo subo al servidor, luego vacio el edittext de nuevo
    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create un objeto chat (message, author)
            Chat chat = new Chat(input, app.getCurrentUserName());
            // Create a new, auto-generated child of that chat location, and save our chat data there
            app.getFireBaseRef().push().setValue(chat);
            inputText.setText("");
        }
    }
}