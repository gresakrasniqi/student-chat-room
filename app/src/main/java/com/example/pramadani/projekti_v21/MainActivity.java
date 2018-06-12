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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends Fragment {

    final private ArrayList<String> alChatRooms = new ArrayList<String>();
    final private ArrayList<ChatRoom> chatRoomInfo = new ArrayList<>();
    private ArrayAdapter<String> allItemsAdapter;
    private boolean listViewHasChats = false;

    public MainActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_main, container, false);
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ListView lvChatRoom = (ListView) v.findViewById(R.id.chats_list);

        allItemsAdapter  = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.list_view_row, alChatRooms);
        lvChatRoom.setAdapter(allItemsAdapter);

        // If the listView has data, don't update the ListView.
        if (!listViewHasChats) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot chat: dataSnapshot.getChildren()) {
                        if (chat.child("users").child(uid).exists()){
                            Toast.makeText(getContext(), "YES", Toast.LENGTH_LONG).show();
                            alChatRooms.add(chat.child("chatName").getValue(String.class));
                            chatRoomInfo.add(chat.getValue(ChatRoom.class));
                        }
                    }
                    allItemsAdapter.notifyDataSetChanged();
                    if (!alChatRooms.isEmpty()) {
                        listViewHasChats = true;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        lvChatRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Send the ChatRoom object to the MessageActivity
                ChatRoom clickedChatRoom = chatRoomInfo.get(position);
                Intent i = new Intent(getActivity(), MessageActivity.class);
                i.putExtra("clickedChatRoom", clickedChatRoom);
                startActivity(i);
            }
        });

        return v;
    }

    public void addChatToListView(String chatId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(), "New user joined", Toast.LENGTH_LONG).show();
                alChatRooms.add(dataSnapshot.child("chatName").getValue(String.class));
                chatRoomInfo.add(dataSnapshot.getValue(ChatRoom.class));

                allItemsAdapter.notifyDataSetChanged();
                if (!alChatRooms.isEmpty()) {
                    listViewHasChats = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        actionJoinChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Enter chat ID");

                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                // Create the input for the AlertDialog
                final EditText input;
                input = new EditText(getContext());
                input.setSingleLine();

                // Set the container for the input field, and configure the left and right margin
                FrameLayout container = new FrameLayout(getActivity());
                FrameLayout.LayoutParams layoutParams = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = 50;
                layoutParams.rightMargin = 50;

                // Set the layoutParams of the input and add it to the container.
                input.setLayoutParams(layoutParams);
                container.addView(input);
                alertDialog.setView(container);

                // Define what "Join" does.
                alertDialog.setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Connected firebase
                        final String joinChatID = input.getText().toString();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(joinChatID);
                        databaseReference.child("users").child(uid).setValue(username, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(getContext(), "You successfully joined the chat", Toast.LENGTH_LONG).show();

                                    // Add the new joined chat to the ListView.
                                    addChatToListView(joinChatID);
                                } else {
                                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
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

        actionNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), CreateNewChatActivity.class));
            }
        });
    }
}