package com.example.pramadani.projekti_v21;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private ImageButton ibSend;
    private EditText etMessage;
    private DatabaseReference databaseMessage;
    private String senderId,chatId;
    private boolean showSentTime=true;
    private boolean showReceivedTime=true;


    private TextView sentTimeShow;
    private TextView sent_user;
    private TextView receivedTimeShow;

    ListView listViewMessage;

    List<ChatMessage> chatMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        ChatRoom clickedChatRoom;

        // Get the extras from the previous Activity
        Bundle extras = getIntent().getExtras();
        clickedChatRoom = (ChatRoom) extras.getSerializable("clickedChatRoom");

        Toolbar toolbar = (Toolbar) findViewById(R.id.messages_activity_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(clickedChatRoom.getChatName());

        ibSend=findViewById(R.id.ib_Send);

        etMessage=findViewById(R.id.etMessage);

        databaseMessage= FirebaseDatabase.getInstance().getReference("Messages");


                    chatId="awokdekokeeko123451111";

         listViewMessage= findViewById(R.id.lvMessage);

         chatMessageList=new ArrayList<>();

        etMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage();
            }
        });

        listViewMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                receivedTimeShow = view.findViewById(R.id.received_time);
                sentTimeShow = view.findViewById(R.id.sent_time);
                sent_user=view.findViewById(R.id.sent_user);
                if(sent_user!=null){

                if(showSentTime){
                    sentTimeShow.setVisibility(View.VISIBLE);
                    showSentTime=false;
                }
                else {
                    sentTimeShow.setVisibility(View.GONE);
                    showSentTime=true;
                }
                }
                else{
                    if(showReceivedTime){
                        receivedTimeShow.setVisibility(View.VISIBLE);
                        showReceivedTime=false;
                    }
                    else {
                        receivedTimeShow.setVisibility(View.GONE);
                        showReceivedTime=true;
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        databaseMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatMessageList.clear();
                for(DataSnapshot messageSnapshot :dataSnapshot.getChildren()){
                    ChatMessage chatMessage=messageSnapshot.getValue(ChatMessage.class);
                    if(chatMessage.getChatID().equals(chatId))
                    chatMessageList.add(chatMessage);

                }
                MessagesAdapter adapter=new MessagesAdapter(MessageActivity.this,chatMessageList);
                listViewMessage.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addMessage() {
        String message =etMessage.getText().toString().trim();
        senderId = FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+"test";
        if(!message.isEmpty()){
            String id = databaseMessage.push().getKey();

            ChatMessage chatMessage=new ChatMessage(message,senderId,chatId);

            databaseMessage.child(id).setValue(chatMessage);

            etMessage.setText("");

//            Toast.makeText(this,"Message Added",Toast.LENGTH_SHORT).show();
        }

    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Leave the chat
        if (id == R.id.nav_home) {
            Toast.makeText(getApplicationContext(),"Clicked LEAVE", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
