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
    EditText etName,etEmail,etPassword,etRetypePassword;
    TextView tvInfo;
    Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDBref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName=findViewById(R.id.etRegisterName);
        etPassword=findViewById(R.id.etRegisterPassword);
        etEmail=findViewById(R.id.etRegisterEmail);
        etRetypePassword=findViewById(R.id.etRegisterRetypePassword);
        tvInfo=findViewById(R.id.tvInfo);
        btnRegister=findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=etName.getText().toString();
                String email=etEmail.getText().toString();
                String password=etPassword.getText().toString();
                String rePassword=etRetypePassword.getText().toString();
                if(password.equals("")||email.equals("")||name.equals("")||rePassword.equals(""))
                    Toast.makeText(RegisterActivity.this,"Fill all the fields!",Toast.LENGTH_SHORT).show();
                else{
                    //nese passwordat nuk perputhen mos e lejo me vazhdu
                    if(!password.equals(rePassword))
                        tvInfo.setText("Passwords do not match!");
                    else{
                        signUpUserWithFirebase(name, email, password);
                    }
                }
            }
        });

    }

    private void signUpUserWithFirebase(final String name, String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                    Toast.makeText(RegisterActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                else{
                    final FirebaseUser newUser =task.getResult().getUser();
                    UserProfileChangeRequest profileUpdates =new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    newUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"User profile updated",Toast.LENGTH_SHORT).show();
                                etName.setText("");
                                etPassword.setText("");
                                etEmail.setText("");
                                etRetypePassword.setText("");
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                createUserInDb(newUser.getUid(),newUser.getDisplayName(),newUser.getEmail());
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
                }
            }
        });
    }
    private void createUserInDb(String userId, String displayName, String email){
        mUsersDBref = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(userId, displayName, email);
        mUsersDBref.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    //error
                    Toast.makeText(RegisterActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    //success adding user to db as well
                    //go to users chat list
                    //goToChartUsersActivity();
                }
            }
        });
    }
    private void goToChartUsersActivity(){
       // startActivity(new Intent(this, ChatUsersActivity.class));
        finish();
    }
}
