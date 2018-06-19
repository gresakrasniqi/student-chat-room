package com.example.pramadani.projekti_v21;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CreateNewChatActivity extends AppCompatActivity {

    private EditText etchatName, etchatFaculty, etchatClass;
    private Button btnCreateChat;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_chat);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Create Chat");

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        etchatName = findViewById(R.id.etChatName);
        etchatFaculty = findViewById(R.id.etChatFaculty);
        etchatClass = findViewById(R.id.etChatClass);
        btnCreateChat = findViewById(R.id.btnCreateChat);

        btnCreateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
                final String chatID = databaseReference.push().getKey();

                //format the data
                Date currentDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String formattedDate = formatter.format(currentDate);
                HashMap<String, String>  users = new HashMap<String, String>();
                users.put(uid, username);
                final ChatRoom newChatRoom = new ChatRoom(etchatName.getText().toString(), etchatClass.getText().toString(),
                        formattedDate, username, etchatFaculty.getText().toString(), users);

                //save values on database
                databaseReference.child(chatID).setValue(newChatRoom).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateNewChatActivity.this, "Chat Created", Toast.LENGTH_LONG).show();

                            newChatRoom.setChatID(chatID);

                            Intent intent = new Intent();
                            intent.putExtra("AddedChatRoom", newChatRoom);
                            setResult(1, intent);
                            finish();
                        } else {
                            Toast.makeText(CreateNewChatActivity.this, "Error "+task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
