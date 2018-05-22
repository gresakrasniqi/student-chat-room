package com.example.pramadani.projekti_v21;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EditProfileActivity extends Fragment {

    private EditText etEditName, etEditFaculty, etEditEmail;
    private TextView username;
    private Button btnUpdate;
    private Button btnCancel;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        return inflater.inflate(R.layout.activity_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.username);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        username.setText(currentUser.getDisplayName());

        etEditName = view.findViewById(R.id.etEditName);
        etEditFaculty = view.findViewById(R.id.etEditFaculty);
        etEditEmail = view.findViewById(R.id.etEditEmail);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnCancel = view.findViewById(R.id.btnCancel);

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
                            Toast.makeText(getContext(),"sucess", Toast.LENGTH_LONG);
                        } else {
                            Toast.makeText(getContext(), "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
//    private void goToMainActivity(){
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
//    }
}
