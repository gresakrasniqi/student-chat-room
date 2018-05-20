package com.example.pramadani.projekti_v21;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private EditText etEditName, etEditFaculty, etEditEmail, etEditPassword, etRetypeEditPassword;
    private TextView username;
    private Button btnUpdate;
    private Button btnCancel;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        username = findViewById(R.id.username);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        username.setText(currentUser.getDisplayName());

    }

    private void updateUserInDb(String userId, User user) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //success adding user to db as well
                    //go to users chat list
                    goToMainActivity();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void goToMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
