package com.example.pramadani.projekti_v21;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SettingsActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_settings, container, false);

        ListView lvSettings = (ListView)v.findViewById(R.id.settings_list);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        String username = firebaseAuth.getCurrentUser().getDisplayName();

        ArrayList<String> alSettings = new ArrayList<String>();
        alSettings.add("Edit profile");
        alSettings.add("About us");
        alSettings.add("Log out");

        // Set the header of the Settings ListView
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.row_header, lvSettings, false);
        TextView txtHeader = (TextView) header.findViewById(R.id.header_username);
        txtHeader.setText(username);
        lvSettings.addHeaderView(header, null, false);

        ArrayAdapter<String> allItemsAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.list_view_row, alSettings);

        lvSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        getActivity().startActivity(new Intent(getContext(), EditProfileActivity.class));
                        break;
                    case 2:
                        getActivity().startActivity(new Intent(getContext(), AboutUsActivity.class));
                        break;
                    case 3:
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Are you sure you want to log out?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                firebaseAuth.signOut();
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                }
            }
        });

        // this will be called when there is change in firebase user session
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                }
            }
        });

        lvSettings.setAdapter(allItemsAdapter);
        return v;
    }
}
