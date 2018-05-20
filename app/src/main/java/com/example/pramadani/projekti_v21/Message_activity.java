package com.example.pramadani.projekti_v21;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class Message_activity extends AppCompatActivity {
    private ListView test2;
    private MessagesAdapter adapter;
    private List<ChatMessage> mTextList;
    private boolean showTime=true;
    private ImageButton btnSend;
    private EditText input;
    private String receiverId,senderId;
    private DatabaseReference mMessagesDbRef;
    private DatabaseReference mUsersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_activity);

        test2=findViewById(R.id.lvMessage);
        btnSend=(ImageButton)findViewById(R.id.ib_Send);
        input=(EditText)findViewById(R.id.input);
        receiverId="6FA33TByRzWlW1oURAbi07v81rS2";
        mTextList=new ArrayList<>();

        mMessagesDbRef = FirebaseDatabase.getInstance().getReference().child("Messages");

        String[] showNameArray= LoginActivity.name.split(" ");
        String showName=showNameArray[0].substring(0,1).toUpperCase()+showNameArray[0].substring(1);

        User premtim3 = new User("1","teper palidhje","bllablla");

        ChatMessage message4 = new ChatMessage("forti", premtim3);


        mTextList.add(message4);

        adapter=new MessagesAdapter(getApplicationContext(),mTextList);

        test2.setAdapter(adapter);


        test2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message =input.getText().toString();
                senderId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(!message.isEmpty())
                    sendMessageToFirebase(message,senderId,receiverId);
            }
        });

    }

    private void sendMessageToFirebase(String message, String senderId, String receiverId) {
        //input.clear(); clear e bon listen idk

        ChatMessage newMsg = new ChatMessage(message,senderId,receiverId);
        mMessagesDbRef.push().setValue(newMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful())
                    Toast.makeText(Message_activity.this,"Error "+task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                else{
                    input.setText(null);
                }
            }
        });

    }
    private void queryMessagesBetweenThisUserAndClickedUser(){
        mMessagesDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    ChatMessage chatMessage=snap.getValue(ChatMessage.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
