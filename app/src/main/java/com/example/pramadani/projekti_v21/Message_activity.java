package com.example.pramadani.projekti_v21;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class Message_activity extends AppCompatActivity {

    private ImageButton ibSend;
    private EditText etMessage;
    private DatabaseReference databaseMessage;
    private DatabaseReference databaseUser;
    private String senderId,chatId;
    private boolean showTime=true;

    ListView listViewMessage;

    List<ChatMessage> chatMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_activity);

        ibSend=(ImageButton) findViewById(R.id.ib_Send);

        etMessage=findViewById(R.id.etMessage);

        databaseMessage= FirebaseDatabase.getInstance().getReference("Messages");
        databaseUser= FirebaseDatabase.getInstance().getReference("Users");


                    chatId="awokdekokeeko12345";

         listViewMessage=(ListView) findViewById(R.id.lvMessage);

         chatMessageList=new ArrayList<>();


        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage();
            }
        });

        listViewMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView timeShow = view.findViewById(R.id.message_time);
                if(showTime){
                    timeShow.setVisibility(View.VISIBLE);
                    showTime=false;
                }
                else {
                    timeShow.setVisibility(View.GONE);
                    showTime=true;
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
                    chatMessageList.add(chatMessage);

                }
                MessagesAdapter adapter=new MessagesAdapter(Message_activity.this,chatMessageList);
                listViewMessage.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addMessage() {
        String message =etMessage.getText().toString();
        senderId = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if(!message.isEmpty()){
            String id = databaseMessage.push().getKey();

            ChatMessage chatMessage=new ChatMessage(message,senderId,chatId);

            databaseMessage.child(id).setValue(chatMessage);

            etMessage.setText("");

            Toast.makeText(this,"Message Added",Toast.LENGTH_SHORT).show();
        }

    }
}
