package com.example.pramadani.projekti_v21;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etEditName, etEditFaculty, etEditEmail;
    private TextView username;
    private Button btnUpdate;
    private Button btnCancel;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Edit profile");

        username = findViewById(R.id.username);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        username.setText(currentUser.getDisplayName());

        etEditName = findViewById(R.id.etEditName);
        etEditFaculty = findViewById(R.id.etEditFaculty);
        etEditEmail = findViewById(R.id.etEditEmail);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

        final String userID = currentUser.getUid();
        final User user = new User();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.setName(dataSnapshot.child("name").getValue(String.class));
                user.setFaculty(dataSnapshot.child("faculty").getValue(String.class));
                user.setEmail(dataSnapshot.child("email").getValue(String.class));
                etEditName.setText(user.getName());
                etEditEmail.setText(user.getEmail());
                etEditFaculty.setText(user.getFaculty());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //show error
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etEditName.getText().toString().equals(user.getName())){
                    user.setName(etEditName.getText().toString());
                }
                if(!etEditFaculty.getText().toString().equals(user.getFaculty())){
                    user.setFaculty(etEditFaculty.getText().toString());
                }
                if(!etEditEmail.getText().toString().equals(user.getName())){
                    user.setEmail(etEditEmail.getText().toString());
                }
                databaseReference.child(userID).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //success adding user to db as well
                            //go to users chat list
                            Toast.makeText(EditProfileActivity.this,"Success", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
