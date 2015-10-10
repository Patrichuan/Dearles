package dear.dearles.activities;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import dear.dearles.R;
import dear.dearles.DearApp;

import dear.dearles.customclasses.ChatArrayAdapter;
import dear.dearles.customclasses.ChatBubbleMessage;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChatBubbleActivity extends AppCompatActivity {

    DearApp app;

    private static final String FIREBASE_URL = "https://blazing-fire-9511.firebaseio.com/";

    EditText chatText;

    String mUsername;
    Firebase mFirebaseRef;
    ListView listView;
    ImageView buttonSend;
    ValueEventListener mConnectedListener;
    ChatArrayAdapter chatArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        app = (DearApp) getApplication();

        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat");

        mUsername = app.getCurrentUserName();

        listView = (ListView) findViewById(R.id.listView1);

        chatText = (EditText) findViewById(R.id.chatText);
        //Listener para el boton 'find' del teclado
        chatText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (chatText.getText().length() >= 1) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        sendChatMessage();
                    }
                }
                return false;
            }
        });

        buttonSend = (ImageView) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        // final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        chatArrayAdapter = new ChatArrayAdapter(mFirebaseRef.limit(50), this, R.layout.activity_chat_singlemessage, mUsername);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);
        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(ChatBubbleActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatBubbleActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });

        // Setups
        setupToolbar();

    }


    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        chatArrayAdapter.cleanup();
    }

    private void sendChatMessage(){
        String input = chatText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            ChatBubbleMessage chat = new ChatBubbleMessage(mUsername, input);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            chatText.setText("");
        }
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(app.getCurrentUserName());
        setSupportActionBar(toolbar);
        // Arrow menu icon
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chatbubble_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                return true;
            case R.id.logout:
                if (app.isUserLoggedIn()) {
                    app.LogOutUser();
                    intent = new Intent(this, Login.class);
                    startActivity(intent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase, R.attr.customFont));
    }

}