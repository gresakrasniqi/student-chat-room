package com.example.pramadani.projekti_v21;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private DatabaseReference databaseReference;
    private String senderUsername = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    private static String chatId;
    private boolean showSentTime = true;
    private boolean showReceivedTime = true;

    private TextView sentTimeShow;
    private TextView sent_user;
    private TextView receivedTimeShow;
    private ChatRoom clickedChatRoom;

    ListView listViewMessage;

    List<ChatMessage> alChatMessageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // Get the extras from the previous Activity
        Bundle extras = getIntent().getExtras();
        clickedChatRoom = (ChatRoom) extras.getSerializable("clickedChatRoom");

        Toolbar toolbar = (Toolbar) findViewById(R.id.messages_activity_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(clickedChatRoom.getChatName());

        ibSend = findViewById(R.id.ib_Send);
        etMessage = findViewById(R.id.etMessage);
        listViewMessage = findViewById(R.id.lvMessage);

        databaseReference = FirebaseDatabase.getInstance().getReference("Messages");
        chatId = clickedChatRoom.getChatID();

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
                sent_user = view.findViewById(R.id.sent_user);
                if (sent_user != null) {
                    if (showSentTime) {
                        sentTimeShow.setVisibility(View.VISIBLE);
                        showSentTime = false;
                    } else {
                        sentTimeShow.setVisibility(View.GONE);
                        showSentTime = true;
                    }
                } else {
                    if (showReceivedTime) {
                        receivedTimeShow.setVisibility(View.VISIBLE);
                        showReceivedTime = false;
                    } else {
                        receivedTimeShow.setVisibility(View.GONE);
                        showReceivedTime = true;
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alChatMessageList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = messageSnapshot.getValue(ChatMessage.class);
                    if (chatMessage.getChatID() != null)
                        if (chatMessage.getChatID().equals(chatId))
                            alChatMessageList.add(chatMessage);
                }
                MessagesAdapter adapter = new MessagesAdapter(MessageActivity.this, alChatMessageList);
                listViewMessage.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addMessage() {
        String message = etMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            // Vendosim id per messazhin e ri
            String id = databaseReference.push().getKey();
            //Ruajm ne chatMessage te dhenat e mesazhit te ri
            ChatMessage chatMessage = new ChatMessage(message, senderUsername, chatId);
            //vendosim ne databaze te dhenat ne id-n e mesazhit te ri
            databaseReference.child(id).setValue(chatMessage);
            etMessage.setText("");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            // Leave the chat
            case R.id.leave_chat:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MessageActivity.this);
                alertDialog.setMessage("Are you sure you want to leave chat?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats");
                        databaseReference.child(chatId).child("users").child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "You left the chat", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent();
                                    intent.putExtra("DeletedChatRoom", clickedChatRoom);
                                    setResult(2, intent);
                                    MessageActivity.this.finish();
                                }
                            }
                        });
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                return true;

            case R.id.copy_chatID:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Data", clickedChatRoom.getChatID());
                clipboardManager.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(), "Copied ChatID", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
