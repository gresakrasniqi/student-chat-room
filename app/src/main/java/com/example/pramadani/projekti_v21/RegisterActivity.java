package com.example.pramadani.projekti_v21;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etFaculty, etUsername, etEmail, etPassword, etRetypePassword;
    private TextView tvInfo;
    private Button btnRegister;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Register");

        etName = findViewById(R.id.etRegisterName);
        etFaculty = findViewById(R.id.etRegisterFaculty);
        etUsername = findViewById(R.id.etRegisterUsername);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        etRetypePassword = findViewById(R.id.etRegisterRetypePassword);
        tvInfo = findViewById(R.id.tvInfo);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String faculty = etFaculty.getText().toString().trim();
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String rePassword = etRetypePassword.getText().toString().trim();
                if (name.isEmpty()|| faculty.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Fill all the fields!", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(rePassword)) {
                        User userData = new User(name, faculty, username, email);
                        // If the passwords match, try to sign up
                        signUpUserWithFirebase(userData, password);
                    } else {
                        tvInfo.setText("Passwords do not match!");
                    }
                }
            }
        });
    }

    // Use firebaseAuth to create new use
    private void signUpUserWithFirebase(final User userData, String password) {
        firebaseAuth = FirebaseAuth.getInstance();
        //Use firebaseAuth to create user with email and password and add OnCompleteListener
        firebaseAuth.createUserWithEmailAndPassword(userData.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Get the created user
                    final FirebaseUser newUser = task.getResult().getUser();

                    // Set the userName as the displayName in farebaseAuth
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(userData.getUsername()).build();
                    newUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Set the other user data
                                updateUserInDb(newUser.getUid(), userData);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Error " + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Insert the other user data in FirebaseDatabase
    private void updateUserInDb(String userId, User userData){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(userId).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //success adding user to db as well
                    //go to users chat list
                    loginAndGoToMainActivity(etEmail.getText().toString(), etPassword.getText().toString());
                } else {
                    Toast.makeText(RegisterActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Login and go to main activity
    private void loginAndGoToMainActivity(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(RegisterActivity.this, MainSwipeActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Finish activity if back button is clicked
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
