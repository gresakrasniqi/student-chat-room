package com.example.pramadani.projekti_v21;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail,etPassword;
    private Button btnLogin,register;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        etEmail=findViewById(R.id.email);
        etPassword=findViewById(R.id.password);
        btnLogin=findViewById(R.id.btn_login);
        register=findViewById(R.id.btn_signup);
        progressBar=findViewById(R.id.indeterminateBar);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= etEmail.getText().toString().trim();
                String password=etPassword.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this, "You must provide email", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "You must provide password", Toast.LENGTH_SHORT).show();
                }else{
                    // Show the progress bar and try to login
                    progressBar.setVisibility(View.VISIBLE);
                    logInUsers(email, password);
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(register);
            }
        });
    }

    private void logInUsers(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if(!task.isSuccessful()){
                    //error loging
                    Toast.makeText(LoginActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent editprofile =new Intent(LoginActivity.this, EditProfileActivity.class);
                    startActivity(editprofile);
//                    Intent maini=new Intent(LoginActivity.this,MainActivity.class);
//                    startActivity(maini);
                }
            }
        });
    }
    //Disable click events if the progress bar is visible
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (progressBar.getVisibility() != View.VISIBLE) {
            return super.dispatchTouchEvent(ev);
        }
        return true;
    }
}
