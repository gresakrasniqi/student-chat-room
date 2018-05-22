package com.example.pramadani.projekti_v21;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                String name = etName.getText().toString();
                String faculty = etFaculty.getText().toString();
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String rePassword = etRetypePassword.getText().toString();
                if (name.equals("") || faculty.equals("") || username.equals("") || email.equals("") || password.equals("") || rePassword.equals("")) {
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

    private void signUpUserWithFirebase(final User userData, String password) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(userData.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser newUser = task.getResult().getUser();
                    updateUserInDb(newUser.getUid(), userData);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(userData.getUsername()).build();
                    newUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
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

    private void updateUserInDb(String userId, User user){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //success adding user to db as well
                    //go to users chat list
                    goToMainActivity();
                } else {
                    Toast.makeText(RegisterActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToMainActivity(){
        startActivity(new Intent(this, MainSwipeActivity.class));
        finish();
    }
}
