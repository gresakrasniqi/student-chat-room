package com.example.pramadani.projekti_v21;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends Fragment {

    // Holds all the ChatRooms that the user has joined.
    final private ArrayList<ChatRoom> alChatRoomInfo = new ArrayList<>();

    // The adapter for the ListView.
    private ChatRoomAdapter chatRoomAdapter;

    // Use this to tell if the ChatRoom where downloaded, initially the app doesn't have the ChatRooms.
    private boolean listViewHasChatRooms = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_main, container, false);

        ListView lvChatRoom = (ListView) v.findViewById(R.id.chats_list);

        //Set adapter for the ListView.
        chatRoomAdapter = new ChatRoomAdapter(getActivity().getBaseContext(), alChatRoomInfo);
        lvChatRoom.setAdapter(chatRoomAdapter);

        // Set on clicklistener that starts the MessageActivity.
        lvChatRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Send the ChatRoom object to the MessageActivity
                ChatRoom clickedChatRoom = alChatRoomInfo.get(position);
                Intent i = new Intent(getActivity(), MessageActivity.class);
                i.putExtra("clickedChatRoom", clickedChatRoom);
                startActivityForResult(i, 2);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the ChatRooms that the user has joined.
        getTheUsersChatRooms();

        // Get the tablayout, the listview, and the FloatingActionsMenu
        final TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        final ListView lv = view.findViewById(R.id.chats_list);
        FloatingActionsMenu floatingActionsMenu = view.findViewById(R.id.floating_action_menu);
        FloatingActionButton actionJoinChat = view.findViewById(R.id.action_join_chat);
        FloatingActionButton actionNewChat = view.findViewById(R.id.action_new_chat);

        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                lv.setAlpha((float) 0.5);
                tabLayout.setAlpha((float) 0.5);
            }

            @Override
            public void onMenuCollapsed() {
                lv.setAlpha(1);
                tabLayout.setAlpha(1);
            }
        });

        // When the user clicks New Chat, start CreateNewChatActivity
        actionNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateNewChatActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // When the user clicks Join Chat, show AlertDialog for user to enter the chat Id.
        actionJoinChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Enter chat ID");

                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                // Create the input for the AlertDialog
                final EditText input = new EditText(getContext());
                input.setSingleLine();
                alertDialog.setView(input);

                // Define what "Join" does.
                alertDialog.setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Get input text
                        final String joinChatID = input.getText().toString();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(joinChatID);
                        databaseReference.child("users").child(uid).setValue(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "You successfully joined the chat", Toast.LENGTH_LONG).show();

                                    // Add the new joined chat to the ListView.
                                    addChatRoomToListView(joinChatID);
                                } else {
                                    Toast.makeText(getContext(), "Error " + task.getException(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                // Define what "Cancel" does.
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void getTheUsersChatRooms() {
        // If the listView has data, don't update the ListView.
        if (!listViewHasChatRooms) {
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Get every chat room that the user has joined.
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot chat: dataSnapshot.getChildren()) {
                        if (chat.child("users").child(uid).exists()){
                            //Toast.makeText(getContext(), "YES", Toast.LENGTH_LONG).show();

                            ChatRoom chatInformation = chat.getValue(ChatRoom.class);
                            chatInformation.setChatID(chat.getKey());

                            alChatRoomInfo.add(chatInformation);
                        }
                    }
                    chatRoomAdapter.notifyDataSetChanged();
                    listViewHasChatRooms = true;
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    // Add the new chat to the ListView after the user has joined a new ChatRoom.
    public void addChatRoomToListView(final String chatId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get all the data of the joined ChatRoom, and set it's ChatID
                ChatRoom addedChatRoom = dataSnapshot.getValue(ChatRoom.class);
                addedChatRoom.setChatID(chatId);

                // Add the joined ChatRoom to the alChatRoomInfo
                alChatRoomInfo.add(addedChatRoom);

                // Notify the adapter that the data has changed.
                chatRoomAdapter.notifyDataSetChanged();
                listViewHasChatRooms = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Create new chat and message activity request code
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null){
                //Get the chatRoom that was created in CreateNewChat
                ChatRoom addedChatRoom = (ChatRoom) data.getSerializableExtra("AddedChatRoom");

                //Added in alChatRoomInfo
                alChatRoomInfo.add(addedChatRoom);

                chatRoomAdapter.notifyDataSetChanged();
                listViewHasChatRooms = true;
            }
        }
        if (requestCode == 2) {
            if (data != null){
                ChatRoom deletedChatRoom = (ChatRoom) data.getSerializableExtra("DeletedChatRoom");

                for (int i = 0; i < alChatRoomInfo.size(); i++) {
                    if (alChatRoomInfo.get(i).getChatID().equals(deletedChatRoom.getChatID())) {
                        alChatRoomInfo.remove(i);
                    }
                }

                chatRoomAdapter.notifyDataSetChanged();
                listViewHasChatRooms = true;
            }
        }
    }
}