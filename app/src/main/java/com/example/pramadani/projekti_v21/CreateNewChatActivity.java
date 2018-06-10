package com.example.pramadani.projekti_v21;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        final String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        etchatName = findViewById(R.id.etChatName);
        etchatFaculty = findViewById(R.id.etChatFaculty);
        etchatClass = findViewById(R.id.etChatClass);
        btnCreateChat = findViewById(R.id.btnCreateChat);

        btnCreateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
                String chatID = databaseReference.push().getKey();

                //format the data
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date currentDate = new Date();
                String formattedDate = formatter.format(currentDate);
                ArrayList<String> users = new ArrayList<>();
                users.add("gresa");
                ChatRoom newChatRoom = new ChatRoom(etchatName.getText().toString(), etchatClass.getText().toString(),
                        formattedDate, username, etchatFaculty.getText().toString(), users);

                //save values on database
                databaseReference.child(chatID).setValue(newChatRoom);
                Toast.makeText(CreateNewChatActivity.this, "Chat Created", Toast.LENGTH_LONG).show();
            }
        });
    }
}
